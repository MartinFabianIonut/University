package com.example.messages

import android.app.Application
import android.util.Log
import com.example.messages.core.TAG

class MessageStoreAndroid : Application() {
    lateinit var container: MessageStoreContainer

    companion object {
        lateinit var instance: MessageStoreAndroid
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "init")
        container = MessageStoreContainer(this)
    }
}
