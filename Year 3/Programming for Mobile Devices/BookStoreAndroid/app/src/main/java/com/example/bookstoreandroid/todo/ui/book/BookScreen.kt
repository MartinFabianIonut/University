package com.example.bookstoreandroid.todo.ui.book

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreandroid.R
import com.example.bookstoreandroid.core.Result
import com.example.bookstoreandroid.core.DateUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(bookId: String?, onClose: () -> Unit) {
    Log.d("BookScreen", "recompose")
    Log.d("BookScreen", "bookId = $bookId")
    val bookViewModel = viewModel<BookViewModel>(factory = BookViewModel.Factory(bookId))
    val bookUiState = bookViewModel.uiState
    var title by rememberSaveable { mutableStateOf(bookUiState.book.title) }
    var author by rememberSaveable { mutableStateOf(bookUiState.book.author) }
    var publicationDate by rememberSaveable { mutableStateOf(bookUiState.book.publicationDate) }
    var isAvailable by rememberSaveable { mutableStateOf(bookUiState.book.isAvailable) }
    var price by rememberSaveable { mutableStateOf(bookUiState.book.price.toString()) }
    Log.d("BookScreen", "title = $title, author = $author, publicationDate = $publicationDate, isAvailable = $isAvailable, price = $price")

    LaunchedEffect(bookUiState.submitResult) {
        Log.d("BookScreen", "Submit = ${bookUiState.submitResult}")
        if (bookUiState.submitResult is Result.Success) {
            Log.d("BookScreen", "Closing screen")
            onClose()
        }
    }

    var textInitialized by remember { mutableStateOf(bookId == null) }
    LaunchedEffect(bookId, bookUiState.loadResult) {
        if (textInitialized) {
            return@LaunchedEffect
        }
        if(!(bookUiState.loadResult is Result.Loading)) {
            title = bookUiState.book.title
            author = bookUiState.book.author
            publicationDate = bookUiState.book.publicationDate
            isAvailable = bookUiState.book.isAvailable
            price = bookUiState.book.price.toString()
            textInitialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.book)) },
                actions = {
                    Button(onClick = {
                        bookViewModel.saveOrUpdateBook(
                            title,
                            author,
                            DateUtils.convertToDDMMYYYY(publicationDate) ?: publicationDate,
                            isAvailable,
                            price.toDoubleOrNull() ?: 0.0
                        )
                    }) { Text("Save") }
                }
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // UI for loading state
            if (bookUiState.loadResult is Result.Loading) {
                CircularProgressIndicator()
                return@Scaffold
            }
            if (bookUiState.submitResult is Result.Loading) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { LinearProgressIndicator() }
            }
            if (bookUiState.loadResult is Result.Error) {
                Text(text = "Failed to load book - ${(bookUiState.loadResult as Result.Error).exception?.message}")
            }

            // UI for loaded state
            Row {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row {
                TextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row {
                TextField(
                    value = DateUtils.convertToDDMMYYYY(publicationDate) ?: publicationDate,
                    onValueChange = { publicationDate = it },
                    label = { Text("Publication Date") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isAvailable,
                    onCheckedChange = { isChecked -> isAvailable = isChecked },
                    modifier = Modifier.padding(end = 8.dp) // Add padding for spacing
                )
                Text(text = if (isAvailable) "Available" else "Not Available", modifier = Modifier.fillMaxWidth())
            }
            Row {
                TextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (bookUiState.submitResult is Result.Error) {
                Text(
                    text = "Failed to submit book - ${(bookUiState.submitResult as Result.Error).exception?.message}",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewBookScreen() {
    BookScreen(bookId = "0", onClose = {})
}
