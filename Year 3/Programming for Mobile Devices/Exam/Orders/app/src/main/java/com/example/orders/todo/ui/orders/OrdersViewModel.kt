package com.example.orders.todo.ui.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.orders.OrderStoreAndroid
import com.example.orders.core.TAG
import com.example.orders.todo.data.Order
import com.example.orders.todo.data.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OrdersViewModel(private val orderRepository: OrderRepository) : ViewModel() {
    val uiState: Flow<List<Order>> = orderRepository.orderStream

    init {
        Log.d(TAG, "init")
    }

    suspend fun updateOrderWithQuantity(order: Order, onResult: (Boolean) -> Unit) {
        Log.d(TAG, "updateOrdersWithQuantity...")
        suspendCoroutine { continuation ->
            viewModelScope.launch {
                orderRepository.updateOrderWithQuantity(order) { isSuccess ->
                    onResult(isSuccess)
                    continuation.resume(if (isSuccess) "Success" else "Failure")
                }
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OrderStoreAndroid)
                OrdersViewModel(app.container.orderRepository)
            }
        }
    }
}
