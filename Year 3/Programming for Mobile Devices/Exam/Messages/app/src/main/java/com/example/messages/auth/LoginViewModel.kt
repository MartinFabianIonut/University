package com.example.messages.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.messages.MessageStoreAndroid
import com.example.messages.auth.data.AuthRepository
import com.example.messages.core.TAG
import com.example.messages.core.data.UserPreferences
import com.example.messages.core.data.UserPreferencesRepository
import kotlinx.coroutines.launch

data class LoginUiState(
    val isAuthenticating: Boolean = false,
    val authenticationError: Throwable? = null,
    val authenticationCompleted: Boolean = false,
    val token: String = ""
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var uiState: LoginUiState by mutableStateOf(LoginUiState())

    init {
        Log.d(TAG, "init")
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            Log.v(TAG, "login...");
            uiState = uiState.copy(isAuthenticating = true, authenticationError = null)
            val result = authRepository.login(username, password)
            if (result.isSuccess) {
                userPreferencesRepository.save(
                    UserPreferences(
                        username,
                        result.getOrNull()?.token ?: ""
                    )
                )
                uiState = uiState.copy(isAuthenticating = false, authenticationCompleted = true)
            } else {
                uiState = uiState.copy(
                    isAuthenticating = false,
                    authenticationError = result.exceptionOrNull()
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MessageStoreAndroid)
                LoginViewModel(
                    app.container.authRepository,
                    app.container.userPreferencesRepository
                )
            }
        }
    }
}