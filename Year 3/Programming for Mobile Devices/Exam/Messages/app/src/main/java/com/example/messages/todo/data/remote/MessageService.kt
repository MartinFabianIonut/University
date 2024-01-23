package com.example.messages.todo.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import com.example.messages.todo.data.Message
interface MessageService {
    @GET("/message")
    suspend fun find(): List<Message>

    @GET("/message")
    suspend fun findWithAuth(@Header("Authorization") authorization: String): List<Message>

    @GET("/message/{id}")
    suspend fun read(
        @Header("Authorization") authorization: String,
        @Path("id") messageId: Int?
    ): Message;

    @Headers("Content-Type: application/json")
    @POST("/message")
    suspend fun create(@Body message: Message): Message

    @Headers("Content-Type: application/json")
    @POST("/message")
    suspend fun createWithAuth(@Header("Authorization") authorization: String, @Body message: Message): Message

    @Headers("Content-Type: application/json")
    @PUT("/message/{id}")
    suspend fun update(
        @Path("id") messageId: Int?,
        @Body message: Message
    ): Message

    @Headers("Content-Type: application/json")
    @PUT("/message/{id}")
    suspend fun updateWithAuth(
        @Header("Authorization") authorization: String,
        @Path("id") messageId: Int?,
        @Body message: Message
    ): Message

}