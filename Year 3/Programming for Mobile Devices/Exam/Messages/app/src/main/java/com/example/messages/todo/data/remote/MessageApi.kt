package com.example.messages.todo.data.remote

import android.annotation.SuppressLint
import android.util.Log
import com.example.messages.todo.data.Message
import com.example.messages.todo.data.MessageRepository

object MessageApi {

    @SuppressLint("StaticFieldLeak")
    lateinit var messageRepository: MessageRepository

    suspend fun createMessage(message: Message): Boolean {
        return try {
            Log.d("Create from WorkManager", "message title: ${message.text}")
            messageRepository.save(message)
            true
        } catch (e: Exception) {
            Log.i("Create failed from WorkManager", e.toString())
            false
        }
    }

    suspend fun updateMessage(message: Message): Boolean {
        return try {
            Log.d("Update from WorkManager", "message title: ${message.text}")
            messageRepository.update(message)
            true
        } catch (e: Exception) {
            Log.i("Update failed from WorkManager", e.toString())
            false
        }
    }
}
