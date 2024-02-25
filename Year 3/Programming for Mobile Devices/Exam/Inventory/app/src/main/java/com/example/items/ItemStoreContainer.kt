package com.example.items


import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.example.items.auth.data.AuthRepository
import com.example.items.auth.data.remote.AuthDataSource
import com.example.items.core.TAG
import com.example.items.core.data.UserPreferencesRepository
import com.example.items.core.data.remote.Api
import com.example.items.core.utils.ConnectivityManagerNetworkMonitor
import com.example.items.todo.data.ItemRepository
import com.example.items.todo.data.remote.ItemApi
import com.example.items.todo.data.remote.ItemService
import com.example.items.todo.data.remote.ItemWsClient

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
        ItemRepository(itemService, itemWsClient, database.productDao(), database.itemDao(), ConnectivityManagerNetworkMonitor(context))
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