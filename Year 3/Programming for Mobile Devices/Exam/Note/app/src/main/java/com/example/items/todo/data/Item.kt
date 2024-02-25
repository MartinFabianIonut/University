package com.example.items.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "items")
data class Item(
    @PrimaryKey
    val id: Int = 0,
    val text: String = "",
    val date: String = "",
    val dirty: Boolean? = false
) {
    override fun toString(): String {
        return "Item(id=$id, text='$text', date=$date, dirty=$dirty)"
    }
}

data class NotesResponse(
    val page: Int,
    val notes: List<Item>,
    val more: Boolean
)
//{
//    override fun toString(): String {
//        return "NotesResponse(page=$page, notes=$notes, more=$more)"
//    }
//}