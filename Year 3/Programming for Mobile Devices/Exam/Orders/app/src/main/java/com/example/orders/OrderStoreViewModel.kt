package com.example.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.orders.core.TAG
import com.example.orders.todo.data.OrderRepository

class OrderStoreViewModel (
    private val orderRepository: OrderRepository
) :
    ViewModel() {

    init {
        Log.d(TAG, "init")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OrderStoreAndroid)
                OrderStoreViewModel(
                    app.container.orderRepository
                )
            }
        }
    }
}
