package com.example.questions

import android.app.Application
import android.util.Log
import com.example.questions.core.TAG

class QuestionStoreAndroid : Application() {
    lateinit var container: QuestionStoreContainer

    companion object {
        lateinit var instance: QuestionStoreAndroid
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "init")
        container = QuestionStoreContainer(this)
    }
}
