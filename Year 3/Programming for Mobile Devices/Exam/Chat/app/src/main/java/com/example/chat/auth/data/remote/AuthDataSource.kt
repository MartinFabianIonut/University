package com.example.chat.auth.data.remote

import android.util.Log
import com.example.chat.core.TAG
import com.example.chat.core.data.remote.Api
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class AuthDataSource {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/login")
        suspend fun login(@Body user: User): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(user: User): Result<TokenHolder> {
        try {
            return Result.success(authService.login(user))
        } catch (e: Exception) {
            Log.w(TAG, "login failed", e)
            return Result.failure(e)
        }
    }
}

