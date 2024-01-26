package com.example.orders.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey
    val code: Int = 0,
    val name: String = "",
    val quantity: Int? = null,
    val dirty: Boolean? = false
) {
    override fun toString(): String {
        return "Order(code=$code, name='$name', quantity=$quantity, dirty=$dirty)"
    }
}

@Entity(tableName = "items")
data class Item(
    @PrimaryKey
    val code: Int = 0,
    val quantity: Int = 0
) {
    override fun toString(): String {
        return "Item(code=$code, quantity=$quantity)"
    }
}