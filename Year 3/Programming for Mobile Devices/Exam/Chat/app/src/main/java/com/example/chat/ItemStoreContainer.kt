package com.example.chat


import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.example.chat.core.TAG
import com.example.chat.auth.data.AuthRepository
import com.example.chat.auth.data.remote.AuthDataSource
import com.example.chat.core.data.UserPreferencesRepository
import com.example.chat.core.data.remote.Api
import com.example.chat.core.utils.ConnectivityManagerNetworkMonitor
import com.example.chat.todo.data.ItemRepository
import com.example.chat.todo.data.remote.ItemService
import com.example.chat.todo.data.remote.ItemWsClient
import com.example.chat.todo.data.remote.ItemApi

val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class ItemStoreContainer(val context: Context) {
    init {
        Log.d(TAG, "init")
    }

    private val itemService: ItemService = Api.retrofit.create(ItemService::class.java)
    private val itemWsClient: ItemWsClient = ItemWsClient(Api.okHttpClient)
    private val authDataSource: AuthDataSource = AuthDataSource()

    private val database: ItemStoreAndroidDatabase by lazy { ItemStoreAndroidDatabase.getDatabase(context) }

    val itemRepository: ItemRepository by lazy {
        ItemRepository(itemService, itemWsClient, database.itemDao(), ConnectivityManagerNetworkMonitor(context))
    }


    val authRepository: AuthRepository by lazy {
        AuthRepository(authDataSource)
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.userPreferencesDataStore)
    }
    // init ItemApi ItemService
    init {
        ItemApi.itemRepository = itemRepository
    }
}