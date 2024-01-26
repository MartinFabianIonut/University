package com.example.orders.todo.data.remote

import android.annotation.SuppressLint
import android.util.Log
import com.example.orders.todo.data.Order
import com.example.orders.todo.data.OrderRepository

object OrderApi {

    @SuppressLint("StaticFieldLeak")
    lateinit var orderRepository: OrderRepository

    suspend fun createOrder(order: Order): Boolean {
        return try {
            Log.d("Create from WorkManager", "order title: ${order.name}")
            orderRepository.save(order)
            true
        } catch (e: Exception) {
            Log.i("Create failed from WorkManager", e.toString())
            false
        }
    }

    suspend fun updateOrder(order: Order): Boolean {
        return try {
            Log.d("Update from WorkManager", "order title: ${order.name}")
            orderRepository.update(order)
            true
        } catch (e: Exception) {
            Log.i("Update failed from WorkManager", e.toString())
            false
        }
    }
}
