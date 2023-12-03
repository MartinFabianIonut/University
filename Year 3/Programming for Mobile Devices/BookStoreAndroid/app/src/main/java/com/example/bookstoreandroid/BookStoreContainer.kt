package com.example.bookstoreandroid


import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.example.bookstoreandroid.core.TAG
import com.example.bookstoreandroid.auth.data.AuthRepository
import com.example.bookstoreandroid.auth.data.remote.AuthDataSource
import com.example.bookstoreandroid.core.data.UserPreferencesRepository
import com.example.bookstoreandroid.core.data.remote.Api
import com.example.bookstoreandroid.todo.data.BookRepository
import com.example.bookstoreandroid.todo.data.remote.BookService
import com.example.bookstoreandroid.todo.data.remote.BookWsClient

val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class BookStoreContainer(val context: Context) {
    init {
        Log.d(TAG, "init")
    }

    private val bookService: BookService = Api.retrofit.create(BookService::class.java)
    private val bookWsClient: BookWsClient = BookWsClient(Api.okHttpClient)
    private val authDataSource: AuthDataSource = AuthDataSource()

    private val database: BookStoreAndroidDatabase by lazy { BookStoreAndroidDatabase.getDatabase(context) }

    val bookRepository: BookRepository by lazy {
        BookRepository(bookService, bookWsClient, database.bookDao())
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(authDataSource)
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.userPreferencesDataStore)
    }
}