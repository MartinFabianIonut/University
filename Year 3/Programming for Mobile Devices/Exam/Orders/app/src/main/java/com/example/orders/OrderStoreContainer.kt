package com.example.orders


import android.content.Context
import android.util.Log
import com.example.orders.core.TAG
import com.example.orders.core.data.remote.Api
import com.example.orders.core.utils.ConnectivityManagerNetworkMonitor
import com.example.orders.todo.data.OrderRepository
import com.example.orders.todo.data.remote.OrderService
import com.example.orders.todo.data.remote.OrderWsClient
import com.example.orders.todo.data.remote.OrderApi

class OrderStoreContainer(val context: Context) {
    init {
        Log.d(TAG, "init")
    }

    private val orderService: OrderService = Api.retrofit.create(OrderService::class.java)
    private val orderWsClient: OrderWsClient = OrderWsClient(Api.okHttpClient)

    private val database: OrderStoreAndroidDatabase by lazy { OrderStoreAndroidDatabase.getDatabase(context) }

    val orderRepository: OrderRepository by lazy {
        OrderRepository(orderService, orderWsClient, database.orderDao(), ConnectivityManagerNetworkMonitor(context))
    }

    init {
        OrderApi.orderRepository = orderRepository
    }
}