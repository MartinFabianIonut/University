package com.example.items.todo.data.remote

import android.util.Log
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import com.example.items.todo.data.Item
import com.example.items.todo.data.NotesResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Query

interface ItemService {

    @Headers("Content-Type: application/json")
    @DELETE("/note/{id}")
    suspend fun delete(
        @Path("id") itemId: Int?
    ): Response<Unit>

    @GET("/note")
    suspend fun find(): NotesResponse

    @GET("/note")
    suspend fun findWithAuth(@Header("Authorization") authorization: String): List<Item>
    @GET("/note")
    suspend fun findWithCache(@Header("If-Modified-Since") ifModifiedSince: String?, @Query ("page") page: Int?): NotesResponse


    @GET("/note/{id}")
    suspend fun read(
        @Path("id") itemId: Int?
    ): Item;

    @GET("/note/{id}")
    suspend fun readWithAuth(
        @Header("Authorization") authorization: String,
        @Path("id") itemId: Int?
    ): Item;

    @Headers("Content-Type: application/json")
    @POST("/note")
    suspend fun create(@Body note: Item): Item

    @Headers("Content-Type: application/json")
    @POST("/note")
    suspend fun createWithAuth(@Header("Authorization") authorization: String, @Body note: Item): Item

    @Headers("Content-Type: application/json")
    @PUT("/note/{id}")
    suspend fun update(
        @Path("id") itemId: Int?,
        @Body note: Item
    ): Item

    @Headers("Content-Type: application/json")
    @PUT("/item/{id}")
    suspend fun updateWithAuth(
        @Header("Authorization") authorization: String,
        @Path("id") itemId: Int?,
        @Body note: Item
    ): Item

    // delete
}