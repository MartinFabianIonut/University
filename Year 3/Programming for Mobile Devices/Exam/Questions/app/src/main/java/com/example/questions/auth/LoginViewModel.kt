package com.example.questions.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.questions.QuestionStoreAndroid
import com.example.questions.auth.data.AuthRepository
import com.example.questions.auth.data.remote.TokenHolder
import com.example.questions.core.TAG
import com.example.questions.core.data.UserPreferences
import com.example.questions.core.data.UserPreferencesRepository
import com.example.questions.todo.data.QuestionRepository
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val isAuthenticating: Boolean = false,
    val authenticationError: Throwable? = null,
    val authenticationCompleted: Boolean = false,
    val token: String = "",
    val questionIds: List<Int> = emptyList(),
    val downloading: Boolean = false,
    val downloadedQuestions: Int = 0,
    val totalQuestions: Int = 0,
    val downloadFailed: Boolean = false,
    val downloadCanBeResumed: Boolean = false
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val questionRepository: QuestionRepository
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


                Log.d(TAG, "login - result: ${result.getOrNull()?.token} and ${result.getOrNull()?.questionIds}")

                uiState = uiState.copy(
                    isAuthenticating = false,
                    token = result.getOrNull()?.token ?: "",
                    questionIds = result.getOrNull()?.questionIds ?: emptyList(),
                    totalQuestions = result.getOrNull()?.questionIds?.size ?: 0,
                    downloading = true
                )

                downloadQuestions(result.getOrNull()?.questionIds ?: emptyList())
                if (uiState.downloadFailed) {
                    Log.d(TAG, "login - downloadQuestions failed at ${System.currentTimeMillis()}")
                    uiState = uiState.copy(downloadCanBeResumed = true)
                }
                else {
                    Log.d(TAG, "login - downloadQuestions succeeded at ${System.currentTimeMillis()}")
                    uiState = uiState.copy(downloadCanBeResumed = false)
                    userPreferencesRepository.save(
                        UserPreferences(
                            username,
                            result.getOrNull()?.token ?: ""
                        )
                    )
                    uiState = uiState.copy(authenticationCompleted = true)
                }

            } else {
                uiState = uiState.copy(
                    isAuthenticating = false,
                    authenticationError = result.exceptionOrNull()
                )
            }
        }
    }

    private var lastDownloadedIndex: Int = 0

    private suspend fun downloadQuestions(questionIds: List<Int>) {
        try {
            if (!uiState.downloadCanBeResumed) {
                questionRepository.clearDao()
            }
            uiState = uiState.copy(downloading = true, downloadCanBeResumed = false)

            uiState = uiState.copy(totalQuestions = questionIds.size)

            Log.d(TAG, "downloadQuestions - questionIds are $questionIds and lastDownloadedIndex is $lastDownloadedIndex and size is ${questionIds.size}")

            while (lastDownloadedIndex < questionIds.size) {
                val questionId = questionIds[lastDownloadedIndex]
                Log.d(TAG, "downloadQuestions - downloading question $questionId")
                questionRepository.get(questionId)
                //delay(1000)
                Log.d(TAG, "downloadQuestions - downloaded question $questionId")
                uiState = uiState.copy(downloadedQuestions = lastDownloadedIndex + 1)
                lastDownloadedIndex++
            }

            uiState = uiState.copy(downloading = false, downloadFailed = false)
        } catch (e: Exception) {
            Log.e(TAG, "downloadQuestions - downloaded question FAILED")
            uiState = uiState.copy(downloading = true, downloadFailed = true, downloadCanBeResumed = true)
        }
    }

    suspend fun resumeDownload() {
        uiState = uiState.copy(downloadCanBeResumed = true)
        uiState = uiState.copy(downloading = true)

        downloadQuestions(uiState.questionIds)

        if (uiState.downloadFailed) {
            Log.d(TAG, "resumeDownload - downloadQuestions failed at ${System.currentTimeMillis()}")
        }
        else {
            Log.d(TAG, "resumeDownload - downloadQuestions succeeded at ${System.currentTimeMillis()}")
            uiState = uiState.copy(downloadCanBeResumed = false)
            userPreferencesRepository.save(
                UserPreferences(
                    uiState.username,
                    authResult?.getOrNull()?.token ?: ""
                )
            )
            uiState = uiState.copy(authenticationCompleted = true)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuestionStoreAndroid)
                LoginViewModel(
                    app.container.authRepository,
                    app.container.userPreferencesRepository,
                    app.container.questionRepository
                )
            }
        }
    }
}