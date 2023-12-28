package com.example.bookstoreandroid.todo.ui.book

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookstoreandroid.BookStoreAndroid
import com.example.bookstoreandroid.core.DateUtils
import com.example.bookstoreandroid.core.Result
import com.example.bookstoreandroid.core.TAG
import com.example.bookstoreandroid.todo.data.Book
import com.example.bookstoreandroid.todo.data.BookRepository
import kotlinx.coroutines.launch


data class BookUiState(
    val bookId: String? = null,
    val book: Book = Book(),
    var loadResult: Result<Book>? = null,
    var submitResult: Result<Book>? = null,
)

class BookViewModel(private val bookId: String?, private val bookRepository: BookRepository) :
    ViewModel() {

    var uiState: BookUiState by mutableStateOf(BookUiState(loadResult = Result.Loading))
        private set

    init {
        Log.d(TAG, "init")
        if (bookId != null) {
            loadBook()
        } else {
            uiState = uiState.copy(loadResult = Result.Success(Book()))
        }
    }

    private fun loadBook() {
        viewModelScope.launch {
            bookRepository.bookStream.collect { books ->
                Log.d(TAG, "Collecting books")
                if (uiState.loadResult !is Result.Loading) {
                    return@collect
                }
                Log.d(TAG, "Collecting books - loadResult is loading, attempting to find book with id: $bookId")
                val book = books.find { it.id == bookId } ?: Book()
                Log.d(TAG, "Collecting books - book: $book")
                uiState = uiState.copy(book = book, loadResult = Result.Success(book))
            }
        }
    }

    fun saveOrUpdateBook(
        title: String,
        author: String,
        publicationDate: String,
        isAvailable: Boolean,
        price: Double,
        lat: Double,
        lng: Double
    ) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(submitResult = Result.Loading)

                // Convert publicationDate to a valid date format if needed
                val formattedDate = DateUtils.parseDDMMYYYY(publicationDate)
                if (formattedDate == null) {
                    uiState = uiState.copy(submitResult = Result.Error(Exception("Invalid date format")))
                    return@launch
                }
                val formattedDateStr = formattedDate.toString()

                val book = uiState.book.copy(
                    title = title,
                    author = author,
                    publicationDate = formattedDateStr,
                    isAvailable = isAvailable,
                    price = price,
                    lat = lat,
                    lng = lng
                )

                val savedBook: Book
                savedBook = if (bookId == null) {
                    bookRepository.save(book)
                } else {
                    bookRepository.update(book)
                }

                uiState = uiState.copy(submitResult = Result.Success(savedBook))
            } catch (e: Exception) {
                uiState = uiState.copy(submitResult = Result.Error(e))
            }
        }
    }


    companion object {
        fun Factory(bookId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookStoreAndroid)
                BookViewModel(bookId, app.container.bookRepository)
            }
        }
    }
}
