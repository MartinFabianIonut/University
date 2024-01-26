package com.example.questions.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey
    val id: Int = 0,
    val text: String = "",
    val options: List<Int> = listOf(),
    val indexCorrectOption: Int = 0,
    val dirty: Boolean? = false,
    var selectedIndex: Int? = null
) {
    override fun toString(): String {
        return "Question(id=$id, text='$text', options=$options, indexCorrectOption=$indexCorrectOption, dirty=$dirty, selectedIndex=$selectedIndex)"
    }
}