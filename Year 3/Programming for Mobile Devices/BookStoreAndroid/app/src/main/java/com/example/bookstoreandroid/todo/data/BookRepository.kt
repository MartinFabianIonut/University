package com.example.bookstoreandroid.todo.data

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.bookstoreandroid.MainActivity
import com.example.bookstoreandroid.core.TAG
import com.example.bookstoreandroid.core.data.remote.Api
import com.example.bookstoreandroid.core.utils.ConnectivityManagerNetworkMonitor
import com.example.bookstoreandroid.core.utils.showSimpleNotificationWithTapAction
import com.example.bookstoreandroid.todo.data.local.BookDao
import com.example.bookstoreandroid.todo.data.remote.BookEvent
import com.example.bookstoreandroid.todo.data.remote.BookService
import com.example.bookstoreandroid.todo.data.remote.BookWsClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class BookRepository (
    private val bookService: BookService,
    private val bookWsClient: BookWsClient,
    private val bookDao: BookDao,
    private val connectivityManagerNetworkMonitor: ConnectivityManagerNetworkMonitor
) {
    val bookStream by lazy {
        Log.d(TAG, "Perform a getAll query")
        val flow = bookDao.getAll()
        Log.d(TAG, "Get all books from the database SUCCEEDED")
        flow
    }

    private lateinit var context: Context

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
                    Log.d(TAG, "Book event handled $bookEvent, notify the user")
                    showSimpleNotificationWithTapAction(
                        context,
                        "Books Channel",
                        0,
                        "External change detected",
                        "Your list of books has been updated. Tap to refresh."
                    )
                    Log.d(TAG, "Book event handled $bookEvent, notify the user SUCCEEDED")
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

    private suspend fun getBookEvents(): Flow<kotlin.Result<BookEvent>> = callbackFlow {
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
        return if (isOnline()) {
            val updatedBook = bookService.update(bookId = book.id, book = book, authorization = getBearerToken())
            Log.d(TAG, "update $book SUCCEEDED")
            handleBookUpdated(updatedBook)
            updatedBook
        } else {
            Log.d(TAG, "update $book locally")
            val dirtyBook = book.copy(dirty = true)
            handleBookUpdated(dirtyBook)
            dirtyBook
        }
    }

    suspend fun save(book: Book): Book {
        Log.d(TAG, "save $book...")
        return if (isOnline()) {
            val createdBook = bookService.create(book = book, authorization = getBearerToken())
            Log.d(TAG, "save ON SERVER the book $createdBook SUCCEEDED")
            handleBookCreated(createdBook)
            createdBook
        } else {
            Log.d(TAG, "save $book locally")
            val dirtyBook = book.copy(dirty = true)
            handleBookCreated(dirtyBook)
            dirtyBook
        }
    }

    private suspend fun isOnline(): Boolean {
        Log.d(TAG, "verify online state...")
        return connectivityManagerNetworkMonitor.isOnline.first()
    }

    private suspend fun handleBookDeleted(book: Book) {
        Log.d(TAG, "handleBookDeleted - todo $book")
    }

    private suspend fun handleBookUpdated(book: Book) {
        Log.d(TAG, "handleBookUpdated...")
        val updated = bookDao.update(book)
        Log.d("handleBookUpdated exited with code = ", updated.toString())
    }

    private suspend fun handleBookCreated(book: Book) {
        Log.d(TAG, "handleBookCreated... for book $book")
        if (book.id.isNotEmpty()) {
            Log.d(TAG, "handleBookCreated - insert/update an existing book")
            bookDao.insert(book)
            Log.d(TAG, "handleBookCreated - insert/update an existing book SUCCEEDED")
        } else {
            val randomNumber = (-10000000..-1).random()
            val localBook = book.copy(id = randomNumber.toString())
            Log.d(TAG, "handleBookCreated - create a new book locally $localBook")
            bookDao.insert(localBook)
            Log.d(TAG, "handleBookCreated - create a new book locally SUCCEEDED")
        }
    }

    suspend fun deleteAll() {
        bookDao.deleteAll()
    }

    fun setToken(token: String) {
        bookWsClient.authorize(token)
    }

    fun setContext(context: Context) {
        this.context = context
    }
}