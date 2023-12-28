package com.example.bookstoreandroid.todo.ui.book

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreandroid.R
import com.example.bookstoreandroid.core.DateUtils
import com.example.bookstoreandroid.core.Result
import com.example.bookstoreandroid.core.ui.MyLocation
import com.example.bookstoreandroid.core.ui.MyLocationViewModel


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
    var lat by rememberSaveable { mutableStateOf(bookUiState.book.lat) }
    var lng by rememberSaveable { mutableStateOf(bookUiState.book.lng) }
    Log.d("BookScreen", "title = $title, author = $author, publicationDate = $publicationDate, isAvailable = $isAvailable, price = $price, lat = $lat, lng = $lng")

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
        if(bookUiState.loadResult !is Result.Loading) {
            title = bookUiState.book.title
            author = bookUiState.book.author
            publicationDate = bookUiState.book.publicationDate
            isAvailable = bookUiState.book.isAvailable
            price = bookUiState.book.price.toString()
            lat = bookUiState.book.lat
            lng = bookUiState.book.lng
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
                            price.toDoubleOrNull() ?: 0.0,
                            lat,
                            lng
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
                StyledTextField(value = title, onValueChange = {title = it}, label = "Title")
            }
            Row {
                StyledTextField(value = author, onValueChange = {author = it}, label = "Author")
            }
            Row {
                StyledTextField(value = DateUtils.convertToDDMMYYYY(publicationDate) ?: publicationDate,
                    onValueChange = { publicationDate = it },
                    label = "Publication Date" )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        shape = RoundedCornerShape(50), brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.tertiary,
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    ) ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isAvailable,
                    onCheckedChange = { isChecked -> isAvailable = isChecked },
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                )
                Text(text = if (isAvailable) "Available" else "Not Available", color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.fillMaxWidth())
            }
            Row {
                StyledTextField(value = price, onValueChange = {price = it}, label = "Price")
            }
            Row {
                StyledTextField(value = lat.toString(), onValueChange = { lat = it.toDoubleOrNull() ?: 0.0 }, label = "Latitude")
            }
            Row {
                StyledTextField(value = lng.toString(), onValueChange = { lng = it.toDoubleOrNull() ?: 0.0 }, label = "Longitude")
            }
            Row {
                val myLocationViewModel = viewModel<MyLocationViewModel>(
                    factory = MyLocationViewModel.Factory(
                        LocalContext.current.applicationContext as Application
                    )
                )
                val location = myLocationViewModel.uiState
                if (lat != 0.0 && lng != 0.0) {
                    MyLocation(lat, lng) { newLatLng ->
                        lat = newLatLng.latitude
                        lng = newLatLng.longitude
                    }
                } else if (location != null) {
                    MyLocation(location.latitude, location.longitude) { newLatLng ->
                        lat = newLatLng.latitude
                        lng = newLatLng.longitude
                    }
                    lat = location.latitude
                    lng = location.longitude
                } else {
                    LinearProgressIndicator()
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.tertiary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            ),
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        ),
        label = { Text(label, color = MaterialTheme.colorScheme.onSecondary) },
        placeholder = { Text(label, color = MaterialTheme.colorScheme.onSecondary) },
        colors = TextFieldDefaults.textFieldColors(
            disabledPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            errorPlaceholderColor = MaterialTheme.colorScheme.error,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent,
        )
    )
}