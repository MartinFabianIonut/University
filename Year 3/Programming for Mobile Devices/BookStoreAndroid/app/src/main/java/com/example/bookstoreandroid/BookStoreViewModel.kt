package com.example.bookstoreandroid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookstoreandroid.core.TAG
import com.example.bookstoreandroid.core.data.UserPreferences
import com.example.bookstoreandroid.core.data.UserPreferencesRepository
import com.example.bookstoreandroid.todo.data.BookRepository
import kotlinx.coroutines.launch

class BookStoreViewModel (
    private val userPreferencesRepository: UserPreferencesRepository,
    private val bookRepository: BookRepository
) :
    ViewModel() {

    init {
        Log.d(TAG, "init")
    }

    fun logout() {
        viewModelScope.launch {
            bookRepository.deleteAll()
            userPreferencesRepository.save(UserPreferences())
        }
    }

    fun setToken(token: String) {
        bookRepository.setToken(token)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookStoreAndroid)
                BookStoreViewModel(
                    app.container.userPreferencesRepository,
                    app.container.bookRepository
                )
            }
        }
    }
}
