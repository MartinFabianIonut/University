package com.example.bookstoreandroid.todo.data.remote

import com.example.bookstoreandroid.todo.data.Book
data class BookEvent(val type: String, val payload: Book)