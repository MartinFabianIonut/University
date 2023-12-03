package com.example.bookstoreandroid.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(@PrimaryKey
                val id: String = "",
                val title : String = "",
                val author : String = "",
                val publicationDate: String = "",
                val isAvailable: Boolean = false,
                val price : Double = 0.0,
                val dirty: Boolean? = false,
                val photo : String = "",
                val lat: Double = 0.0,
                val lng: Double = 0.0)
