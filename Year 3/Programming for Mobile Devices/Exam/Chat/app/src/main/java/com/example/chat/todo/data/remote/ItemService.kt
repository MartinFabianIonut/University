package com.example.chat.todo.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import com.example.chat.todo.data.Item
import retrofit2.http.Query

interface ItemService {

    @GET("/item")
    suspend fun find(): List<Item>

    @Headers("Content-Type: application/json")
    @GET("/message")
    suspend fun findWithAuth(
        @Header("token") authorization: String,
        @Query("created") created: String
    ): List<Item>

    @GET("/item/{id}")
    suspend fun read(
        @Path("id") itemId: Int?
    ): Item;


    @GET("/item/{id}")
    suspend fun readWithAuth(
        @Header("Authorization") authorization: String,
        @Path("id") itemId: Int?
    ): Item;

    @Headers("Content-Type: application/json")
    @POST("/item")
    suspend fun create(@Body item: Item): Item

    @Headers("Content-Type: application/json")
    @POST("/message")
    suspend fun createWithAuth(
        @Header("token") authorization: String,
        @Body item: Item
    ): Item

    @Headers("Content-Type: application/json")
    @PUT("/item/{id}")
    suspend fun update(
        @Path("id") itemId: Int?,
        @Body item: Item
    ): Item

    @Headers("Content-Type: application/json")
    @PUT("/item/{id}")
    suspend fun updateWithAuth(
        @Header("Authorization") authorization: String,
        @Path("id") itemId: Int?,
        @Body item: Item
    ): Item

}