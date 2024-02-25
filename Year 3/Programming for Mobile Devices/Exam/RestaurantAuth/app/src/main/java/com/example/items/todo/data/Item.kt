package com.example.items.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey
    val code: Int = 0,
    val name: String = "",
    val dirty: Boolean? = false,
    val specialOffer: Boolean? = false,
) {
    override fun toString(): String {
        return "Item(code=$code, name='$name', dirty=$dirty, specialOffer=$specialOffer)"
    }
}

@Entity(tableName = "order_items")
data class OrderItem(
    val id: Int? = -1,
    @PrimaryKey
    val code: Int = 0,
    val quantity: Int = 0,
    val table: Int = 0,
    val free: Boolean? = false,
    val dirty: Boolean? = false
) {
    override fun toString(): String {
        return "OrderItem(code=$code, quantity=$quantity, free=$free)"
    }
}
