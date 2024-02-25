package com.example.items.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val code: Int = 0,
    val name: String = "",
    val dirty: Boolean? = false,
)

@Entity(tableName = "items")
data class Item(
    val id: Int = 0,
    @PrimaryKey
    val code: Int = 0,
    val quantity: Int = 0,
    val dirty: Boolean? = false,
)