package com.example.messages.todo.ui.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.messages.MessageStoreAndroid
import com.example.messages.core.TAG
import com.example.messages.todo.data.Message
import com.example.messages.todo.data.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MessagesViewModel(private val messageRepository: MessageRepository) : ViewModel() {
    val uiState: Flow<List<Message>> = messageRepository.messageStream

    init {
        Log.d(TAG, "init")
        loadMessages()
    }

    private fun loadMessages() {
        Log.d(TAG, "loadMessages...")
        viewModelScope.launch {
            messageRepository.refresh()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MessageStoreAndroid)
                MessagesViewModel(app.container.messageRepository)
            }
        }
    }
}
