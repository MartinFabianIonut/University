package com.example.bookstoreandroid.core.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bookstoreandroid.BookStoreAndroidDatabase
import com.example.bookstoreandroid.todo.data.remote.BookApi

class MyWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val bookDao = BookStoreAndroidDatabase.getDatabase(applicationContext).bookDao()
        val dirtyBooks = bookDao.getDirtyBooks()
        dirtyBooks.forEach {dirtyBook ->
            Log.d("MyWorker do work", "book title: ${dirtyBook.title}")
            val newBook = dirtyBook.copy(dirty = false)
            if (dirtyBook.id.toInt() > 0) {
                BookApi.updateBook(newBook)
            } else {
                BookApi.createBook(newBook)
                Log.d(
                    "Update from WorkManager",
                    "book title: ${newBook.title} and id: ${newBook.id}" +
                            " result: ${newBook.id}"
                )
                Thread.sleep(1000)
                bookDao.deleteBook(dirtyBook)
                Log.d("Delete from WorkManager",
                    "SUCCESS book title: ${dirtyBook.title} and id: ${dirtyBook.id}" )
            }
        }
        if (dirtyBooks.isNotEmpty()){
            showSimpleNotificationWithTapAction(
                context,
                "Books Channel",
                0,
                "Synchronization complete",
                "Your list of books has been synchronized with the server."
            )
        }
        return Result.success()
    }
}