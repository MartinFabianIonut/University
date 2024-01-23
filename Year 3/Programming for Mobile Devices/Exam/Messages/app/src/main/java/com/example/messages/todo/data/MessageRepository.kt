package com.example.messages.todo.data

import android.content.Context
import android.util.Log
import com.example.messages.core.TAG
import com.example.messages.core.data.remote.Api
import com.example.messages.core.utils.ConnectivityManagerNetworkMonitor
import com.example.messages.core.utils.showSimpleNotificationWithTapAction
import com.example.messages.todo.data.local.MessageDao
import com.example.messages.todo.data.remote.MessageEvent
import com.example.messages.todo.data.remote.MessageService
import com.example.messages.todo.data.remote.MessageWsClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class MessageRepository (
    private val messageService: MessageService,
    private val messageWsClient: MessageWsClient,
    private val messageDao: MessageDao,
    private val connectivityManagerNetworkMonitor: ConnectivityManagerNetworkMonitor
) {
    val messageStream by lazy {
        Log.d(TAG, "Perform a getAll query")
        val flow = messageDao.getAll()
        Log.d(TAG, "Get all messages from the database SUCCEEDED")
        flow
    }

    private lateinit var context: Context

    init {
        Log.d(TAG, "init")
    }

    private fun getBearerToken() = "Bearer ${Api.tokenInterceptor.token}"

    suspend fun refresh() {
        Log.d(TAG, "refresh started")
        try {
            //val authorization = getBearerToken()
            val messages = messageService.find()
            messageDao.deleteAll()
            messages.forEach { messageDao.insert(it) }
            Log.d(TAG, "refresh succeeded")
            for (message in messages) {
                Log.d(TAG, message.toString())
            }
        } catch (e: Exception) {
            Log.w(TAG, "refresh failed", e)
        }
    }

    suspend fun openWsClient() {
        Log.d(TAG, "openWsClient")
        withContext(Dispatchers.IO) {
            getMessageEvents().collect {
                Log.d(TAG, "Message event collected $it")
                if (it.isSuccess) {
                    val messageEvent = it.getOrNull();
                    when (messageEvent?.type) {
                        "created" -> handleMessageCreated(messageEvent.payload)
                        "updated" -> handleMessageUpdated(messageEvent.payload)
                        "deleted" -> handleMessageDeleted(messageEvent.payload)
                        null -> handleMessageCreated(messageEvent?.payload ?: Message())
                    }
                    Log.d(TAG, "Message event handled $messageEvent, notify the user")
                    showSimpleNotificationWithTapAction(
                        context,
                        "Messages Channel",
                        0,
                        "External change detected",
                        "Your list of messages has been updated. Tap to refresh."
                    )
                    Log.d(TAG, "Message event handled $messageEvent, notify the user SUCCEEDED")
                }
            }
        }
    }

    suspend fun closeWsClient() {
        Log.d(TAG, "closeWsClient")
        withContext(Dispatchers.IO) {
            messageWsClient.closeSocket()
        }
    }

    private suspend fun getMessageEvents(): Flow<Result<MessageEvent>> = callbackFlow {
        Log.d(TAG, "getMessageEvents started")
        messageWsClient.openSocket(
            onEvent = {
                Log.d(TAG, "onEvent $it")
                if (it != null) {
                    trySend(kotlin.Result.success(it))
                }
            },
            onClosed = { close() },
            onFailure = { close() });
        awaitClose { messageWsClient.closeSocket() }
    }

    suspend fun update(message: Message): Message {
        Log.d(TAG, "update $message...")
        return if (isOnline()) {
            try {
                val updatedMessage = messageService.update(messageId = message.id, message = message)
                Log.d(TAG, "update on SERVER -> $message SUCCEEDED")
                handleMessageUpdated(updatedMessage)
                updatedMessage
            } catch (e: Exception) {
                Log.d(TAG, "update on SERVER -> $message FAILED")
                message
            }
        } else {
            Log.d(TAG, "update $message locally")
            val dirtyMessage = message.copy(dirty = true)
            handleMessageUpdated(dirtyMessage)
            dirtyMessage
        }
    }

    suspend fun save(message: Message): Message {
        Log.d(TAG, "save $message...")
        return if (isOnline()) {
            val createdMessage = messageService.create(message = message)
            Log.d(TAG, "save ON SERVER the message $createdMessage SUCCEEDED")
            handleMessageCreated(createdMessage)
            createdMessage
        } else {
            Log.d(TAG, "save $message locally")
            val dirtyMessage = message.copy(dirty = true)
            handleMessageCreated(dirtyMessage)
            dirtyMessage
        }
    }

    private suspend fun isOnline(): Boolean {
        Log.d(TAG, "verify online state...")
        return connectivityManagerNetworkMonitor.isOnline.first()
    }

    private suspend fun handleMessageDeleted(message: Message) {
        Log.d(TAG, "handleMessageDeleted - todo $message")
    }

    private suspend fun handleMessageUpdated(message: Message) {
        Log.d(TAG, "handleMessageUpdated... $message")
        val updated = messageDao.update(message)
        Log.d("handleMessageUpdated exited with code = ", updated.toString())
    }

    private suspend fun handleMessageCreated(message: Message) {
        Log.d(TAG, "handleMessageCreated... for message $message")
        if (message.id >= 0) {
            Log.d(TAG, "handleMessageCreated - insert/update an existing message")
            messageDao.insert(message)
            Log.d(TAG, "handleMessageCreated - insert/update an existing message SUCCEEDED")
        } else {
            val randomNumber = (-10000000..-1).random()
            val localMessage = message.copy(id = randomNumber)
            Log.d(TAG, "handleMessageCreated - create a new message locally $localMessage")
            messageDao.insert(localMessage)
            Log.d(TAG, "handleMessageCreated - create a new message locally SUCCEEDED")
        }
    }

    suspend fun deleteAll() {
        messageDao.deleteAll()
    }

    fun setToken(token: String) {
        messageWsClient.authorize(token)
    }

    fun setContext(context: Context) {
        this.context = context
    }
}