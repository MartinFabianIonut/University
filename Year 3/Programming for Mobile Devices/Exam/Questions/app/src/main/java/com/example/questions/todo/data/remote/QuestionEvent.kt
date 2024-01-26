package com.example.questions.todo.data.remote

import com.example.questions.todo.data.Question
data class QuestionEvent(val type: String, val payload: Question)