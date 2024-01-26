package com.example.orders.todo.data.remote

import com.example.orders.todo.data.Order
data class OrderEvent(val type: String, val payload: List<Order>)