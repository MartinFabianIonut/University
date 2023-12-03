package com.example.bookstoreandroid.auth.data

import android.util.Log
import com.example.bookstoreandroid.auth.data.remote.AuthDataSource
import com.example.bookstoreandroid.auth.data.remote.TokenHolder
import com.example.bookstoreandroid.auth.data.remote.User
import com.example.bookstoreandroid.core.TAG
import com.example.bookstoreandroid.core.data.remote.Api

class AuthRepository(private val authDataSource: AuthDataSource) {
    init {
        Log.d(TAG, "init")
    }

    fun clearToken() {
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = authDataSource.login(user)
        if (result.isSuccess) {
            Api.tokenInterceptor.token = result.getOrNull()?.token
        }
        return result
    }
}
