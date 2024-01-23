package com.example.messages.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    val id: Int = 0,
    val text: String = "",
    val read: Boolean = false,
    val sender: String = "",
    val created: Long = 0,
    val dirty: Boolean? = false
) {
    override fun toString(): String {
        return "Message(id=$id, text='$text', read=$read, sender='$sender', created=$created, dirty=$dirty)"
    }
}
