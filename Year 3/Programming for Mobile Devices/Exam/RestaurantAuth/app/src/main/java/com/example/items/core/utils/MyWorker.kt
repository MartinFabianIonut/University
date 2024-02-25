package com.example.items.core.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.items.ItemStoreAndroidDatabase
import com.example.items.todo.data.remote.ItemApi

class MyWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val itemDao = ItemStoreAndroidDatabase.getDatabase(applicationContext).itemDao()
        val dirtyItems = itemDao.getDirtyItems()
        dirtyItems.forEach {dirtyItem ->
            Log.d("MyWorker do work", dirtyItem.toString())
            val newItem = dirtyItem.copy(dirty = false)
            if (dirtyItem.code.toInt() > 0) {
                ItemApi.updateItem(newItem)
            } else {
                ItemApi.createItem(newItem)
                Log.d(
                    "Update from WorkManager",
                    newItem.toString()
                )
                Thread.sleep(1000)
                itemDao.deleteItem(dirtyItem)
                Log.d("Delete from WorkManager",
                    "SUCCESS : $dirtyItem deleted"
                )
            }
        }
        if (dirtyItems.isNotEmpty()){
            showSimpleNotificationWithTapAction(
                context,
                "Items Channel",
                0,
                "Synchronization complete",
                "Your list of items has been synchronized with the server."
            )
        }
        return Result.success()
    }
}