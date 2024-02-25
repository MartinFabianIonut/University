package com.example.items

import android.app.Application
import android.util.Log
import com.example.items.core.TAG

class ItemStoreAndroid : Application() {
    lateinit var container: ItemStoreContainer

    companion object {
        lateinit var instance: ItemStoreAndroid
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "init")
        container = ItemStoreContainer(this)
    }
}
