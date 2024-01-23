package com.example.messages.todo.ui.message

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
import com.example.messages.core.DateUtils
import com.example.messages.core.Result
import com.example.messages.core.TAG
import com.example.messages.todo.data.Message
import com.example.messages.todo.data.MessageRepository
import kotlinx.coroutines.launch


data class MessageUiState(
    val messageId: Int? = null,
    val message: Message = Message(),
    var loadResult: Result<Message>? = null,
    var submitResult: Result<Message>? = null,
)

class MessageViewModel(private val messageId: Int?, private val messageRepository: MessageRepository) :
    ViewModel() {

    var uiState: MessageUiState by mutableStateOf(MessageUiState(loadResult = Result.Loading))
        private set

    init {
        Log.d(TAG, "init")
        if (messageId != null) {
            loadMessage()
        } else {
            uiState = uiState.copy(loadResult = Result.Success(Message()))
        }
    }

    private fun loadMessage() {
        viewModelScope.launch {
            messageRepository.messageStream.collect { messages ->
                Log.d(TAG, "Collecting messages")
                if (uiState.loadResult !is Result.Loading) {
                    return@collect
                }
                Log.d(TAG, "Collecting messages - loadResult is loading, attempting to find message with id: $messageId")
                val message = messages.find { it.id == messageId } ?: Message()
                Log.d(TAG, "Collecting messages - message: $message")
                uiState = uiState.copy(message = message, loadResult = Result.Success(message))
            }
        }
    }

    fun saveOrUpdateMessage(
        id: Int,
        text: String,
        read: Boolean,
        sender: String,
        created: Long,
    ) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(submitResult = Result.Loading)

                // Convert publicationDate to a valid date format if needed
//                val formattedDate = DateUtils.parseDDMMYYYY(publicationDate)
//                if (formattedDate == null) {
//                    uiState = uiState.copy(submitResult = Result.Error(Exception("Invalid date format")))
//                    return@launch
//                }
//                val formattedDateStr = formattedDate.toString()

                val message = uiState.message.copy(
                    id = id,
                    text = text,
                    read = read,
                    sender = sender,
                    created = created
                )

                val savedMessage: Message = if (messageId == null) {
                    messageRepository.save(message)
                } else {
                    messageRepository.update(message)
                }

                uiState = uiState.copy(submitResult = Result.Success(savedMessage))
            } catch (e: Exception) {
                uiState = uiState.copy(submitResult = Result.Error(e))
            }
        }
    }


    companion object {
        fun Factory(messageId: Int?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MessageStoreAndroid)
                MessageViewModel(messageId, app.container.messageRepository)
            }
        }
    }
}
