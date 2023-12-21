package com.example.bookstoreandroid

import android.app.Application
import android.util.Log
import com.example.bookstoreandroid.core.TAG

class BookStoreAndroid : Application() {
    lateinit var container: BookStoreContainer

    companion object {
        lateinit var instance: BookStoreAndroid
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "init")
        container = BookStoreContainer(this)
    }
}
