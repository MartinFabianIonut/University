package com.example.orders.todo.data

import android.content.Context
import android.util.Log
import com.example.orders.core.TAG
import com.example.orders.core.data.remote.Api
import com.example.orders.core.utils.ConnectivityManagerNetworkMonitor
import com.example.orders.core.utils.showSimpleNotificationWithTapAction
import com.example.orders.todo.data.local.OrderDao
import com.example.orders.todo.data.remote.OrderEvent
import com.example.orders.todo.data.remote.OrderService
import com.example.orders.todo.data.remote.OrderWsClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class OrderRepository (
    private val orderService: OrderService,
    private val orderWsClient: OrderWsClient,
    private val orderDao: OrderDao,
    private val connectivityManagerNetworkMonitor: ConnectivityManagerNetworkMonitor
) {
    val orderStream by lazy {
        Log.d(TAG, "Perform a getAll query")
        val flow = orderDao.getAll()
        Log.d(TAG, "Get all orders from the database SUCCEEDED")
        flow
    }

    private lateinit var context: Context

    init {
        Log.d(TAG, "init")
    }

    suspend fun openWsClient() {
        Log.d(TAG, "openWsClient")
        withContext(Dispatchers.IO) {
            getOrderEvents().collect {
                Log.d(TAG, "Order event collected $it")
                if (it.isSuccess) {
                    val orderEvent = it.getOrNull();
                    when (orderEvent?.type) {
                        null -> {
                            for (order in orderEvent?.payload!!) {
                                Log.d(TAG, order.toString())
                                handleOrderCreated(order)
                            }
                        }
                    }
                    Log.d(TAG, "Order event handled $orderEvent, notify the user")
                    delay(1000)
                    showSimpleNotificationWithTapAction(
                        context,
                        "Orders Channel",
                        0,
                        "External change detected",
                        "Your list of orders has been updated. Tap to refresh."
                    )
                    Log.d(TAG, "Order event handled $orderEvent, notify the user SUCCEEDED")
                }
            }
        }
    }

    suspend fun closeWsClient() {
        Log.d(TAG, "closeWsClient")
        withContext(Dispatchers.IO) {
            orderWsClient.closeSocket()
        }
    }

    private suspend fun getOrderEvents(): Flow<Result<OrderEvent>> = callbackFlow {
        Log.d(TAG, "getOrderEvents started")
        orderWsClient.openSocket(
            onEvent = {
                Log.d(TAG, "onEvent $it")
                if (it != null) {
                    trySend(Result.success(it))
                }
            },
            onClosed = { close() },
            onFailure = { close() });
        awaitClose { orderWsClient.closeSocket() }
    }

    suspend fun update(order: Order): Order {
        Log.d(TAG, "update $order...")
        return if (isOnline()) {
            try {
                val updatedOrder = orderService.update(orderId = order.code, order = order)
                Log.d(TAG, "update on SERVER -> $order SUCCEEDED")
                handleOrderUpdated(updatedOrder)
                updatedOrder
            } catch (e: Exception) {
                Log.d(TAG, "update on SERVER -> $order FAILED")
                order
            }
        } else {
            Log.d(TAG, "update $order locally")
            val dirtyOrder = order.copy(dirty = true)
            handleOrderUpdated(dirtyOrder)
            dirtyOrder
        }
    }

    suspend fun save(order: Order): Order {
        Log.d(TAG, "save $order...")
        return if (isOnline()) {
            val createdOrder = orderService.create(order = order)
            Log.d(TAG, "save ON SERVER the order $createdOrder SUCCEEDED")
            handleOrderCreated(createdOrder)
            createdOrder
        } else {
            Log.d(TAG, "save $order locally")
            val dirtyOrder = order.copy(dirty = true)
            handleOrderCreated(dirtyOrder)
            dirtyOrder
        }
    }

    private suspend fun isOnline(): Boolean {
        Log.d(TAG, "verify online state...")
        return connectivityManagerNetworkMonitor.isOnline.first()
    }

    private suspend fun handleOrderUpdated(order: Order) {
        Log.d(TAG, "handleOrderUpdated... $order")
        val updated = orderDao.update(order)
        Log.d("handleOrderUpdated exited with code = ", updated.toString())
    }

    private suspend fun handleOrderCreated(order: Order) {
        Log.d(TAG, "handleOrderCreated... for order $order")
        if (order.code >= 0) {
            Log.d(TAG, "handleOrderCreated - insert/update an existing order")

            val orderFound= orderDao.find(order.code)
            Log.d(TAG, "FOUND ORDER: $orderFound")
            if (orderFound == null){
                orderDao.insert(order)
            }

            Log.d(TAG, "handleOrderCreated - insert/update an existing order SUCCEEDED")
        } else {
            val randomNumber = (-10000000..-1).random()
            val localOrder = order.copy(code = randomNumber)
            Log.d(TAG, "handleOrderCreated - create a new order locally $localOrder")
            orderDao.insert(localOrder)
            Log.d(TAG, "handleOrderCreated - create a new order locally SUCCEEDED")
        }
    }

    fun setContext(context: Context) {
        this.context = context
    }

    suspend fun updateOrderWithQuantity(order: Order, onResult: (Boolean) -> Unit) {
        Log.d(TAG, "updateOrderWithQuantity $order...")
        if (isOnline()) {
            try {
                val item = Item(code = order.code, quantity = order.quantity ?: 0)
                Log.d(TAG, "updateOrderWithQuantity on SERVER for item -> $item")
                val result = orderService.postItem(item)
                Log.d(TAG, "updateOrderWithQuantity on SERVER -> $order SUCCEEDED, result: $result")
                val newOrder = order.copy(dirty = false)
                handleOrderUpdated(newOrder)
                onResult(true)
            } catch (e: Exception) {
                Log.d(TAG, "updateOrderWithQuantity on SERVER -> $order FAILED $e")
                onResult(false)
            }
        } else {
            Log.d(TAG, "updateOrderWithQuantity $order locally")
            val dirtyOrder = order.copy(dirty = true)
            handleOrderUpdated(dirtyOrder)
            onResult(true)
        }
    }

    suspend fun updateOrderWithQuantityLocally(order: Order) {
        handleOrderUpdated(order)
    }
}