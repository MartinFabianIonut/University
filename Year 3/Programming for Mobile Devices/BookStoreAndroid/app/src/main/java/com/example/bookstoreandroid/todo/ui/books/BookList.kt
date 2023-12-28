package com.example.bookstoreandroid.todo.ui.books


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookstoreandroid.core.DateUtils
import com.example.bookstoreandroid.todo.data.Book

typealias OnBookFn = (id: String?) -> Unit

@Composable
fun BookList(bookList: List<Book>, onBookClick: OnBookFn, modifier: Modifier) {
    Log.d("BookList", "recompose")
    for (book in bookList){
        Log.d("BookList", book.title)
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        items(bookList) { book ->
            BookDetail(book, onBookClick)
        }
    }
}
@Composable
fun BookDetail(book: Book, onBookClick: OnBookFn) {
    var isExpanded by remember { mutableStateOf(false) }
    val gradientColors = listOf(
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary
    )
    val textColor = MaterialTheme.colorScheme.onPrimary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(
                shape = RoundedCornerShape(
                    topStart = CornerSize(5.dp),
                    topEnd = CornerSize(30.dp),
                    bottomStart = CornerSize(30.dp),
                    bottomEnd = CornerSize(5.dp)
                )
            )
            .background(brush = Brush.linearGradient(gradientColors))
            .clickable(onClick = {
                isExpanded = !isExpanded
            })
            .padding(12.dp)
    ) {
        val formattedPublicationDate = DateUtils.convertToDDMMYYYY(book.publicationDate)

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Title: ${book.title}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = textColor),
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clickable {
                            onBookClick(book.id)
                        }
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Expand/Collapse",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Text(
                text = "Author: ${book.author}",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                modifier = Modifier.padding(bottom = 6.dp)
            )
            if (isExpanded) {
                Text(
                    text = "Publication Date: $formattedPublicationDate",
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Is Available: ${if (book.isAvailable) "yes" else "no"}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Price: ${book.price}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text (
                    text = "Lat: ${book.lat.toString().take(7)}, Lng: ${book.lng.toString().take(7)}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                )
            }
        }
    }
}
