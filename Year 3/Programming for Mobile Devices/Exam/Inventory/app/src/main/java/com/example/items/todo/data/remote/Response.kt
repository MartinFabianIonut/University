package com.example.items.todo.data.remote

import com.example.items.todo.data.Product

data class Response (
    val total: Int,
    val page: Int,
    val products: List<Product>
)