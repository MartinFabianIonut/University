package com.example.messages.core.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.messages.MessageStoreAndroidDatabase
import com.example.messages.todo.data.remote.MessageApi

class MyWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val messageDao = MessageStoreAndroidDatabase.getDatabase(applicationContext).messageDao()
        val dirtyMessages = messageDao.getDirtyMessages()
        dirtyMessages.forEach {dirtyMessage ->
            Log.d("MyWorker do work", dirtyMessage.toString())
            val newMessage = dirtyMessage.copy(dirty = false)
            if (dirtyMessage.id.toInt() > 0) {
                MessageApi.updateMessage(newMessage)
            } else {
                MessageApi.createMessage(newMessage)
                Log.d(
                    "Update from WorkManager",
                    newMessage.toString()
                )
                Thread.sleep(1000)
                messageDao.deleteMessage(dirtyMessage)
                Log.d("Delete from WorkManager",
                    "SUCCESS : $dirtyMessage deleted"
                )
            }
        }
        if (dirtyMessages.isNotEmpty()){
            showSimpleNotificationWithTapAction(
                context,
                "Messages Channel",
                0,
                "Synchronization complete",
                "Your list of messages has been synchronized with the server."
            )
        }
        return Result.success()
    }
}