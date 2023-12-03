package com.example.bookstoreandroid.todo.ui.books

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreandroid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(onBookClick: (id: String?) -> Unit, onAddBook: () -> Unit, onLogout: () -> Unit) {
    Log.d("BooksScreen", "recompose")
    val booksViewModel = viewModel<BooksViewModel>(factory = BooksViewModel.Factory)
    val booksUiState by booksViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.books)) },
                actions = {
                    Button(onClick = onLogout) { Text("Logout") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d("BooksScreen", "add")
                    onAddBook()
                },
            ) { Icon(Icons.Rounded.Add, "Add") }
        }
    ) {
        BookList(
            bookList = booksUiState,
            onBookClick = onBookClick,
            modifier = Modifier.padding(it)
        )
    }
}

@Preview
@Composable
fun PreviewBooksScreen() {
    BooksScreen(onBookClick = {}, onAddBook = {}, onLogout = {})
}
