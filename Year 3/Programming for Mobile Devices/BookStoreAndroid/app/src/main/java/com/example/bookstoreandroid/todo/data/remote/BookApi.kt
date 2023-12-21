package com.example.bookstoreandroid.todo.data.remote

import android.util.Log
import com.example.bookstoreandroid.todo.data.Book
import com.example.bookstoreandroid.todo.data.BookRepository

object BookApi {

    lateinit var bookRepository: BookRepository

    suspend fun createBook(book: Book): Boolean {
        return try {
            Log.d("Create from WorkManager", "book title: ${book.title}")
            bookRepository.save(book)
            true
        } catch (e: Exception) {
            Log.i("Create failed from WorkManager", e.toString())
            false
        }
    }

    suspend fun updateBook(book: Book): Boolean {
        return try {
            Log.d("Update from WorkManager", "book title: ${book.title}")
            bookRepository.update(book)
            true
        } catch (e: Exception) {
            Log.i("Update failed from WorkManager", e.toString())
            false
        }
    }
}
