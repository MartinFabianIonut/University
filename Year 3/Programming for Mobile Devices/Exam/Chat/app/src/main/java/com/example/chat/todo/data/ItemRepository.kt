package com.example.chat.todo.data

import android.content.Context
import android.util.Log
import com.example.chat.core.TAG
import com.example.chat.core.data.remote.Api
import com.example.chat.core.utils.ConnectivityManagerNetworkMonitor
import com.example.chat.core.utils.showSimpleNotificationWithTapAction
import com.example.chat.todo.data.local.ItemDao
import com.example.chat.todo.data.remote.ItemEvent
import com.example.chat.todo.data.remote.ItemService
import com.example.chat.todo.data.remote.ItemWsClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class ItemRepository (
    private val itemService: ItemService,
    private val itemWsClient: ItemWsClient,
    private val itemDao: ItemDao,
    private val connectivityManagerNetworkMonitor: ConnectivityManagerNetworkMonitor,
) {
    val itemStream by lazy {
        Log.d(TAG, "Perform a getAll query")
        val flow = itemDao.getAll()
        Log.d(TAG, "Get all items from the database SUCCEEDED")
        flow
    }

    private lateinit var context: Context

    init {
        Log.d(TAG, "init")
    }

    private fun getBearerToken() = "${Api.tokenInterceptor.token}"

    suspend fun clearDao() {
        itemDao.deleteAll()
    }

    suspend fun refresh() {
        Log.d(TAG, "refresh started")
        try {
            val authorization = getBearerToken()
            val created : String = itemDao.getAll().first().maxBy { it.created }.created ?: "0"
            Log.d(TAG, "refresh with authorization $authorization and created $created")
            val items = itemService.findWithAuth(getBearerToken(),created)
            Log.d(TAG, "refresh with authorization $authorization and created $created SUCCEEDED")
            //itemDao.deleteAll()
            items.forEach { itemDao.insert(it) }
            Log.d(TAG, "refresh succeeded")
            for (item in items) {
                Log.d(TAG, item.toString())
            }
        } catch (e: Exception) {
            Log.w(TAG, "refresh failed", e)
        }
    }

    suspend fun openWsClient() {
        Log.d(TAG, "openWsClient")
        withContext(Dispatchers.IO) {
            getItemEvents().collect {
                Log.d(TAG, "Item event collected $it")
                if (it.isSuccess) {
                    val itemEvent = it.getOrNull()
                    if (itemEvent != null) {
                        handleItemCreated(itemEvent.payload)
                    }
                    Log.d(TAG, "Item event handled $itemEvent, notify the user")
                    showSimpleNotificationWithTapAction(
                        context,
                        "Items Channel",
                        0,
                        "External change detected",
                        "Your list of items has been updated. Tap to refresh."
                    )
                    Log.d(TAG, "Item event handled $itemEvent, notify the user SUCCEEDED")
                }
            }
        }
    }

    suspend fun closeWsClient() {
        Log.d(TAG, "closeWsClient")
        withContext(Dispatchers.IO) {
            itemWsClient.closeSocket()
        }
    }

    private suspend fun getItemEvents(): Flow<Result<ItemEvent>> = callbackFlow {
        Log.d(TAG, "getItemEvents started")
        itemWsClient.openSocket(
            onEvent = {
                Log.d(TAG, "onEvent $it")
                if (it != null) {
                    trySend(kotlin.Result.success(it))
                }
            },
            onClosed = { close() },
            onFailure = { close() });
        awaitClose { itemWsClient.closeSocket() }
    }

    suspend fun sendMessage(message: String, room: String) {
        var item = Item(text = message, room = room)
        Log.d(TAG, "sendMessage $item...")
        if (isOnline()) {
            Log.d(TAG, "sendMessage $item on SERVER")
            try{
                val result=itemService.createWithAuth(getBearerToken(),item = item)
                handleItemCreated(result)
            }
            catch (e: Exception){
                Log.d(TAG, "sendMessage $item on SERVER FAILED")
                throw e
            }
            Log.d(TAG, "sendMessage $item on SERVER SUCCEEDED")
        } else {
            Log.d(TAG, "sendMessage $item locally")
//            val dirtyItem = item.copy(dirty = true)
//            handleItemCreated(dirtyItem)
            throw Exception("Not online")
            Log.d(TAG, "sendMessage $item locally SUCCEEDED")
        }
    }

    suspend fun addDirtyMessage(message: String, room: String){
        var item = Item(text = message, room = room, dirty = true)
        Log.d(TAG, "addDirtyMessage $item...")
        handleItemCreated(item)
        Log.d(TAG, "addDirtyMessage $item locally SUCCEEDED")
    }

    suspend fun removeDirtyMessage(message: String, room: String){
        var item = Item(text = message, room = room)
        Log.d(TAG, "removeDirtyMessage $item...")
        handleItemDeleted(item)
        Log.d(TAG, "removeDirtyMessage $item locally SUCCEEDED")
    }

    suspend fun update(item: Item): Item {
        Log.d(TAG, "update $item...")
        return if (isOnline()) {
            try {
                val updatedItem = itemService.update(itemId = item.id, item = item)
                Log.d(TAG, "update on SERVER -> $item SUCCEEDED")
                handleItemUpdated(updatedItem)
                updatedItem
            } catch (e: Exception) {
                Log.d(TAG, "update on SERVER -> $item FAILED")
                item
            }
        } else {
            Log.d(TAG, "update $item locally")
            val dirtyItem = item.copy(dirty = true)
            handleItemUpdated(dirtyItem)
            dirtyItem
        }
    }

    suspend fun save(item: Item): Item {
        Log.d(TAG, "save $item...")
        return if (isOnline()) {
            val createdItem = itemService.create(item = item)
            Log.d(TAG, "save ON SERVER the item $createdItem SUCCEEDED")
            handleItemCreated(createdItem)
            createdItem
        } else {
            Log.d(TAG, "save $item locally")
            val dirtyItem = item.copy(dirty = true)
            handleItemCreated(dirtyItem)
            dirtyItem
        }
    }

    suspend fun get (itemId: Int) {
        Log.d(TAG, "get $itemId...")
        try {
            val item = itemService.read(itemId = itemId)
            Log.d(TAG, "get $itemId on SERVER -> $item SUCCEEDED")
            itemDao.insert(item)
            Log.d(TAG, "item $itemId saved in the local database")
        } catch (e: Exception) {
            Log.d(TAG, "get $itemId on SERVER -> FAILED with exception $e")
            throw e
        }
    }

    private suspend fun isOnline(): Boolean {
        Log.d(TAG, "verify online state...")
        return connectivityManagerNetworkMonitor.isOnline.first()
    }

    private suspend fun handleItemDeleted(item: Item) {
        Log.d(TAG, "handleItemDeleted - todo $item")
        itemDao.deleteItem(item)
        Log.d(TAG, "handleItemDeleted - todo $item SUCCEEDED")
    }

    private suspend fun handleItemUpdated(item: Item) {
        Log.d(TAG, "handleItemUpdated... $item")
        val updated = itemDao.update(item)
        Log.d("handleItemUpdated exited with code = ", updated.toString())
    }

    private suspend fun handleItemCreated(item: Item) {
        Log.d(TAG, "handleItemCreated... for item $item")
        if (item.id >= 0) {
            Log.d(TAG, "handleItemCreated - insert/update an existing item")
            itemDao.insert(item)
            Log.d(TAG, "handleItemCreated - insert/update an existing item SUCCEEDED")
        } else {
            val randomNumber = (-10000000..-1).random()
            val localItem = item.copy(id = randomNumber)
            Log.d(TAG, "handleItemCreated - create a new item locally $localItem")
            itemDao.insert(localItem)
            Log.d(TAG, "handleItemCreated - create a new item locally SUCCEEDED")
        }
    }

    suspend fun deleteAll() {
        itemDao.deleteAll()
    }

    fun setToken(token: String) {
        itemWsClient.authorize(token)
    }

    fun setContext(context: Context) {
        this.context = context
    }


}