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
    val token: String = "",
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
            Log.d(TAG, "login - downloadItems succeeded at ${System.currentTimeMillis()}")
            userPreferencesRepository.save(
                UserPreferences(
                    username,
                    username
                )
            )
            uiState= uiState.copy(isAuthenticating = false, authenticationCompleted = true)

            }
        }

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