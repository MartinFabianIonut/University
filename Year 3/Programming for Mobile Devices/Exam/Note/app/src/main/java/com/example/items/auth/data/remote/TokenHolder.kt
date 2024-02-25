package com.example.items.auth.data.remote

data class TokenHolder(
    val token: String,
    val itemIds : List<Int>
)
