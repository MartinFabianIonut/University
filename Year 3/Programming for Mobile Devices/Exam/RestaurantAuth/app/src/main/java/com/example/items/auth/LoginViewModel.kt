package com.example.items.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.items.ItemStoreAndroid
import com.example.items.auth.data.AuthRepository
import com.example.items.auth.data.remote.TokenHolder
import com.example.items.core.TAG
import com.example.items.core.data.UserPreferences
import com.example.items.core.data.UserPreferencesRepository
import com.example.items.todo.data.ItemRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val isAuthenticating: Boolean = false,
    val authenticationError: Throwable? = null,
    val authenticationCompleted: Boolean = false,
    val token: Int = -1,
    val itemIds: List<Int> = emptyList(),
    val downloading: Boolean = false,
    val downloadedItems: Int = 0,
    val totalItems: Int = 0,
    val downloadFailed: Boolean = false,
    val downloadResumable: Boolean = false
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {
    var uiState: LoginUiState by mutableStateOf(LoginUiState())

    private var authResult: Result<TokenHolder>? = null

    init {
        Log.d(TAG, "init")
    }

    fun login(username: String) {
        viewModelScope.launch {
            Log.v(TAG, "login...");
            uiState = uiState.copy(username = username)
            uiState = uiState.copy(isAuthenticating = true, authenticationError = null)
            val result = authRepository.login(username)
            authResult = result
            if (result.isSuccess) {
                Log.d(TAG, "login - result: ${result.getOrNull()?.token}")

                uiState = uiState.copy(
                    isAuthenticating = false,
                    token = result.getOrNull()?.token ?: 0,
                )
                userPreferencesRepository.save(
                    UserPreferences(
                        username,
                        result.getOrNull()?.token ?: -1
                    )
                )
                uiState = uiState.copy(authenticationCompleted = true)
            } else {
                uiState = uiState.copy(
                    isAuthenticating = false,
                    authenticationError = result.exceptionOrNull()
                )
            }
        }
    }

    private var lastDownloadedIndex: Int = 0

//    private suspend fun downloadItems(itemIds: List<Int>) {
//        try {
//            if (!uiState.downloadResumable) {
//                itemRepository.clearDao()
//            }
//            uiState = uiState.copy(downloading = true, downloadResumable = false)
//
//            uiState = uiState.copy(totalItems = itemIds.size)
//
//            Log.d(TAG, "downloadItems - itemIds are $itemIds and lastDownloadedIndex is $lastDownloadedIndex and size is ${itemIds.size}")
//
//            while (lastDownloadedIndex < itemIds.size) {
//                val itemId = itemIds[lastDownloadedIndex]
//                Log.d(TAG, "downloadItems - downloading item $itemId")
//                itemRepository.get(itemId)
//                //delay(1000)
//                Log.d(TAG, "downloadItems - downloaded item $itemId")
//                uiState = uiState.copy(downloadedItems = lastDownloadedIndex + 1)
//                lastDownloadedIndex++
//            }
//
//            uiState = uiState.copy(downloading = false, downloadFailed = false)
//        } catch (e: Exception) {
//            Log.e(TAG, "downloadItems - downloaded item FAILED")
//            uiState = uiState.copy(downloading = true, downloadFailed = true, downloadResumable = true)
//        }
//    }

//    fun downloadItem(itemId: Int) {
//        viewModelScope.launch {
//            try {
//                if (!uiState.downloadResumable) {
//                    itemRepository.clearDao()
//                }
//                uiState = uiState.copy(downloadResumable = true)
//
//                Log.d(TAG, "downloadItem - downloading item $itemId at ${System.currentTimeMillis()}")
//                itemRepository.get(itemId)
//
//                uiState = uiState.copy(downloadedItems = lastDownloadedIndex + 1)
//                lastDownloadedIndex++
//                if (lastDownloadedIndex == uiState.totalItems) {
//                    Log.d(TAG, " lastDownloadedIndex downloadItem - downloaded all items at ${System.currentTimeMillis()}")
//                    uiState = uiState.copy(downloading = false)
//                }
//
//                Log.d(TAG, "downloadItem - downloaded item $itemId at ${System.currentTimeMillis()}")
//            } catch (e: Exception) {
//                Log.e(TAG, "downloadItem - downloaded item $itemId FAILED at ${System.currentTimeMillis()}")
//                uiState = uiState.copy(downloading = false, downloadFailed = true, downloadResumable = true)
//            }
//        }
//    }

//    suspend fun resumeDownload() {
//        uiState = uiState.copy(downloadResumable = true)
//        uiState = uiState.copy(downloading = true)
//
//        downloadItems(uiState.itemIds)
//
//        if (uiState.downloadFailed) {
//            Log.d(TAG, "resumeDownload - downloadItems failed at ${System.currentTimeMillis()}")
//        }
//        else {
//            Log.d(TAG, "resumeDownload - downloadItems succeeded at ${System.currentTimeMillis()}")
//            uiState = uiState.copy(downloadResumable = false)
//            userPreferencesRepository.save(
//                UserPreferences(
//                    uiState.username,
//                    authResult?.getOrNull()?.token ?: ""
//                )
//            )
//            uiState = uiState.copy(authenticationCompleted = true)
//        }
//    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ItemStoreAndroid)
                LoginViewModel(
                    app.container.authRepository,
                    app.container.userPreferencesRepository,
                    app.container.itemRepository
                )
            }
        }
    }
}