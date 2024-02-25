package com.example.chat.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey
    val id: Int = 0,
    val text: String ="",
    val created: String = "0",
    val room: String = "",
    val username: String = "",
    val dirty: Boolean = false

) {
    override fun toString(): String {
        return "Item(id=$id, text='$text', created=$created, room='$room', username='$username')"
    }
}