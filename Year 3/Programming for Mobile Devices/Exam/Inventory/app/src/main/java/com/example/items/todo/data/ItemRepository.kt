package com.example.items.todo.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.items.core.TAG
import com.example.items.core.data.remote.Api
import com.example.items.core.utils.ConnectivityManagerNetworkMonitor
import com.example.items.core.utils.showSimpleNotificationWithTapAction
import com.example.items.todo.data.local.ItemDao
import com.example.items.todo.data.local.ProductDao
import com.example.items.todo.data.remote.ItemEvent
import com.example.items.todo.data.remote.ItemService
import com.example.items.todo.data.remote.ItemWsClient
import com.example.items.todo.data.remote.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


data class DownloadState(
    val show: Boolean = false,
)

class ItemRepository(
    private val itemService: ItemService,
    private val itemWsClient: ItemWsClient,
    private val productDao: ProductDao,
    private val itemDao: ItemDao,
    private val connectivityManagerNetworkMonitor: ConnectivityManagerNetworkMonitor,
) {
    val productStream by lazy {
        Log.d(TAG, "Perform a getAll query")
        val flow = productDao.getAll()
        Log.d(TAG, "Get all items from the database SUCCEEDED")
        flow
    }

    val itemStream by lazy {
        Log.d(TAG, "Perform a getAll query")
        val flow = itemDao.getAll()
        Log.d(TAG, "Get all items from the database SUCCEEDED")
        flow
    }

    var downloadState: DownloadState by mutableStateOf(DownloadState())

    private lateinit var context: Context

    init {
        Log.d(TAG, "init")
    }

    private fun getBearerToken() = "Bearer ${Api.tokenInterceptor.token}"

    suspend fun clearDao() {
        productDao.deleteAll()
    }

    suspend fun refresh(page: Int): Response? {
        Log.d(TAG, "refresh started")
        return try {
            val response = itemService.find(page)
            val products = response.products
            products.forEach { productDao.insert(it) }
            Log.d(TAG, "refresh succeeded")
            for (product in products) {
                Log.d(TAG, product.toString())
            }
            response
        } catch (e: Exception) {
            Log.w(TAG, "refresh failed", e)
            null
        }
    }

    suspend fun search(query: String): List<Product> {
        Log.d(TAG, "search started")
        return try {
            var products = productDao.getAll().first()
            Log.d(TAG, "search before filter $products")
            products = products.filter { it.name.contains(query) }
            Log.d(TAG, "search after filter$products")
            products.take(5)
        } catch (e: Exception) {
            Log.w(TAG, "search failed", e)
            emptyList()
        }
    }

    suspend fun openWsClient() {
        Log.d(TAG, "openWsClient")
        withContext(Dispatchers.IO) {
            getItemEvents().collect {
                Log.d(TAG, "Item event collected $it")
                if (it.isSuccess) {
                    val itemEvent = it.getOrNull();
                    when (itemEvent?.type) {
                        "productsChanged" -> handleProductsChanged()
                        null -> throw Exception("Mars n-ai voie")
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

    suspend fun saveToServer(item: Item): Item {
        Log.d(TAG, "update $item...")
        return if (isOnline()) {
            try {
                var updatedItem = itemService.create(item)
                Log.d(TAG, "update on SERVER -> $item SUCCEEDED")
                updatedItem = updatedItem.copy(dirty = false)
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
        Log.d(TAG, "save $item locally")
        val dirtyItem = item.copy(dirty = true)
        handleItemCreated(dirtyItem)
        return dirtyItem
    }

    suspend fun get(itemId: Int) {
        Log.d(TAG, "get $itemId...")
        try {
            val item = itemService.read(itemId = itemId)
            Log.d(TAG, "get $itemId on SERVER -> $item SUCCEEDED")
            productDao.insert(item)
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

    private suspend fun handleItemDeleted(product: Product) {
        Log.d(TAG, "handleItemDeleted - todo $product")
    }

    private suspend fun handleProductsChanged() {
        downloadState = downloadState.copy(show = true)
    }

    private suspend fun handleItemUpdated(item: Item) {
        Log.d(TAG, "handleItemUpdated... $item")
        val updated = itemDao.update(item)
        Log.d("handleItemUpdated exited with code = ", updated.toString())
    }

    private suspend fun handleItemCreated(item: Item) {
        Log.d(TAG, "handleItemCreated... for item $item")
        if (item.code >= 0) {
            Log.d(TAG, "handleItemCreated - insert/update an existing item")
            itemDao.insert(item)
            Log.d(TAG, "handleItemCreated - insert/update an existing item SUCCEEDED")
        } else {
            val randomNumber = (-10000000..-1).random()
            val localItem = item.copy(code = randomNumber)
            Log.d(TAG, "handleItemCreated - create a new item locally $localItem")
            itemDao.insert(localItem)
            Log.d(TAG, "handleItemCreated - create a new item locally SUCCEEDED")
        }
    }

    suspend fun deleteAll() {
        productDao.deleteAll()
    }

    fun setToken(token: String) {
        itemWsClient.authorize(token)
    }

    fun setContext(context: Context) {
        this.context = context
    }

    suspend fun getItemsToUpdate(): List<Item> {
        Log.d(TAG, "updateAllItems...")
        return itemDao.getAll().first().filter { it.dirty == true }
//        for (item in items) {
//            Log.d(TAG, "updateAllItems - updating item $item")
//            val updatedItem = saveToServer(item)
//            Log.d(TAG, "updateAllItems - updating item $item SUCCEEDED")
//        }
    }
}