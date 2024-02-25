package com.example.items.todo.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import com.example.items.todo.data.Item
interface ItemService {

    @GET("/space")
    suspend fun find(): List<Item>

    @GET("/item")
    suspend fun findWithAuth(@Header("Authorization") authorization: String): List<Item>

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
    @POST("/item")
    suspend fun createWithAuth(@Header("Authorization") authorization: String, @Body item: Item): Item

    @Headers("Content-Type: application/json")
    @PUT("/space/{id}")
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