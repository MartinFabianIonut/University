package com.example.bookstoreandroid.todo.data

import android.util.Log
import com.example.bookstoreandroid.core.TAG
import com.example.bookstoreandroid.core.data.remote.Api
import com.example.bookstoreandroid.todo.data.local.BookDao
import com.example.bookstoreandroid.todo.data.remote.BookEvent
import com.example.bookstoreandroid.todo.data.remote.BookService
import com.example.bookstoreandroid.todo.data.remote.BookWsClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class BookRepository (
    private val bookService: BookService,
    private val bookWsClient: BookWsClient,
    private val bookDao: BookDao
) {
    val bookStream by lazy { bookDao.getAll() }

    init {
        Log.d(TAG, "init")
    }

    private fun getBearerToken() = "Bearer ${Api.tokenInterceptor.token}"

    suspend fun refresh() {
        Log.d(TAG, "refresh started")
        try {
            val books = bookService.find(authorization = getBearerToken())
            bookDao.deleteAll()
            books.forEach { bookDao.insert(it) }
            Log.d(TAG, "refresh succeeded")
            // Log the books to the console - title, author, and publicationDate
            for (book in books) {
                Log.d(TAG, "Book: ${book.title}, ${book.author}, ${book.publicationDate}")
            }
        } catch (e: Exception) {
            Log.w(TAG, "refresh failed", e)
        }
    }

    suspend fun openWsClient() {
        Log.d(TAG, "openWsClient")
        withContext(Dispatchers.IO) {
            getBookEvents().collect {
                Log.d(TAG, "Book event collected $it")
                if (it.isSuccess) {
                    val bookEvent = it.getOrNull();
                    when (bookEvent?.type) {
                        "created" -> handleBookCreated(bookEvent.payload)
                        "updated" -> handleBookUpdated(bookEvent.payload)
                        "deleted" -> handleBookDeleted(bookEvent.payload)
                    }
                }
            }
        }
    }

    suspend fun closeWsClient() {
        Log.d(TAG, "closeWsClient")
        withContext(Dispatchers.IO) {
            bookWsClient.closeSocket()
        }
    }

    suspend fun getBookEvents(): Flow<kotlin.Result<BookEvent>> = callbackFlow {
        Log.d(TAG, "getBookEvents started")
        bookWsClient.openSocket(
            onEvent = {
                Log.d(TAG, "onEvent $it")
                if (it != null) {
                    trySend(kotlin.Result.success(it))
                }
            },
            onClosed = { close() },
            onFailure = { close() });
        awaitClose { bookWsClient.closeSocket() }
    }

    suspend fun update(book: Book): Book {
        Log.d(TAG, "update $book...")
        val updatedBook =
            bookService.update(bookId = book.id, book = book, authorization = getBearerToken())
        Log.d(TAG, "update $book succeeded")
        handleBookUpdated(updatedBook)
        return updatedBook
    }

    suspend fun save(book: Book): Book {
        Log.d(TAG, "save $book...")
        val createdBook = bookService.create(book = book, authorization = getBearerToken())
        Log.d(TAG, "save $book succeeded")
        handleBookCreated(createdBook)
        return createdBook
    }

    private suspend fun handleBookDeleted(book: Book) {
        Log.d(TAG, "handleBookDeleted - todo $book")
    }

    private suspend fun handleBookUpdated(book: Book) {
        Log.d(TAG, "handleBookUpdated...")
        bookDao.update(book)
    }

    private suspend fun handleBookCreated(book: Book) {
        Log.d(TAG, "handleBookCreated...")
        bookDao.insert(book)
    }

    suspend fun deleteAll() {
        bookDao.deleteAll()
    }

    fun setToken(token: String) {
        bookWsClient.authorize(token)
    }
}