package com.example.items.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey
    val id: Int = 0,
    val number: String ="",
    val status: String = "free",
    val takenBy: String = "",
    val dirty: Boolean = false
) {
    override fun toString(): String {
        return "Item(id=$id, number='$number', text='$status', takenBy='$takenBy')"
    }
}