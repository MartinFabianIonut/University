package com.example.chat.auth.data

import android.util.Log
import com.example.chat.auth.data.remote.AuthDataSource
import com.example.chat.auth.data.remote.TokenHolder
import com.example.chat.auth.data.remote.User
import com.example.chat.core.TAG
import com.example.chat.core.data.remote.Api

class AuthRepository(private val authDataSource: AuthDataSource) {
    init {
        Log.d(TAG, "init")
    }

    fun clearToken() {
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String): Result<TokenHolder> {
        val user = User(username)
        val result = authDataSource.login(user)
        if (result.isSuccess) {
            Api.tokenInterceptor.token = result.getOrNull()?.token
        }
        return result
    }
}
