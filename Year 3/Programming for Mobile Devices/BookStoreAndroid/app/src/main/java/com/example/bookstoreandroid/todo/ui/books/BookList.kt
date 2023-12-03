package com.example.bookstoreandroid.todo.ui.books


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreandroid.todo.data.Book
import java.text.SimpleDateFormat
import java.util.*
import com.example.bookstoreandroid.core.DateUtils


typealias OnBookFn = (id: String?) -> Unit

@Composable
fun BookList(bookList: List<Book>, onBookClick: OnBookFn, modifier: Modifier) {
    Log.d("BookList", "recompose")
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(12.dp)
    ) {
        val formattedPublicationDate = DateUtils.convertToDDMMYYYY(book.publicationDate)

        ClickableText(text = AnnotatedString("Title: ${book.title} \nAuthor: ${book.author} \nPublication Date: $formattedPublicationDate"),
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ), onClick = { onBookClick(book.id) })
    }
}