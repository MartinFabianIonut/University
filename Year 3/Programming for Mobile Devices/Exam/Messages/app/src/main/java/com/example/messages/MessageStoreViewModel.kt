package com.example.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.messages.core.TAG
import com.example.messages.core.data.UserPreferences
import com.example.messages.core.data.UserPreferencesRepository
import com.example.messages.todo.data.MessageRepository
import kotlinx.coroutines.launch

class MessageStoreViewModel (
    private val userPreferencesRepository: UserPreferencesRepository,
    private val messageRepository: MessageRepository
) :
    ViewModel() {

    init {
        Log.d(TAG, "init")
    }

    fun logout() {
        viewModelScope.launch {
            messageRepository.deleteAll()
            userPreferencesRepository.save(UserPreferences())
        }
    }

    fun setToken(token: String) {
        messageRepository.setToken(token)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MessageStoreAndroid)
                MessageStoreViewModel(
                    app.container.userPreferencesRepository,
                    app.container.messageRepository
                )
            }
        }
    }
}
