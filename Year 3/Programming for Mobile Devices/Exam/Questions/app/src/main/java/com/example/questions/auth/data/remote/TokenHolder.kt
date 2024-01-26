package com.example.questions.auth.data.remote

data class TokenHolder(
    val token: String,
    val questionIds : List<Int>
)
