package com.example.messages


import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.example.messages.core.TAG
import com.example.messages.auth.data.AuthRepository
import com.example.messages.auth.data.remote.AuthDataSource
import com.example.messages.core.data.UserPreferencesRepository
import com.example.messages.core.data.remote.Api
import com.example.messages.core.utils.ConnectivityManagerNetworkMonitor
import com.example.messages.todo.data.MessageRepository
import com.example.messages.todo.data.remote.MessageService
import com.example.messages.todo.data.remote.MessageWsClient
import com.example.messages.todo.data.remote.MessageApi

val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class MessageStoreContainer(val context: Context) {
    init {
        Log.d(TAG, "init")
    }

    private val messageService: MessageService = Api.retrofit.create(MessageService::class.java)
    private val messageWsClient: MessageWsClient = MessageWsClient(Api.okHttpClient)
    private val authDataSource: AuthDataSource = AuthDataSource()

    private val database: MessageStoreAndroidDatabase by lazy { MessageStoreAndroidDatabase.getDatabase(context) }

    val messageRepository: MessageRepository by lazy {
        MessageRepository(messageService, messageWsClient, database.messageDao(), ConnectivityManagerNetworkMonitor(context))
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(authDataSource)
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.userPreferencesDataStore)
    }
    // init MessageApi MessageService
    init {
        MessageApi.messageRepository = messageRepository
    }
}