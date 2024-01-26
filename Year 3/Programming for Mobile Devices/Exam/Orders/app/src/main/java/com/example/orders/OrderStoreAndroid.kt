package com.example.orders

import android.app.Application
import android.util.Log
import com.example.orders.core.TAG

class OrderStoreAndroid : Application() {
    lateinit var container: OrderStoreContainer

    companion object {
        lateinit var instance: OrderStoreAndroid
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "init")
        container = OrderStoreContainer(this)
    }
}
