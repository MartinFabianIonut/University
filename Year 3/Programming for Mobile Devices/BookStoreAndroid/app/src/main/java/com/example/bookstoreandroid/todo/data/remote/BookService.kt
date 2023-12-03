package com.example.bookstoreandroid.todo.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import com.example.bookstoreandroid.todo.data.Book
interface BookService {
    @GET("/api/book")
    suspend fun find(@Header("Authorization") authorization: String): List<Book>

    @GET("/api/book/{id}")
    suspend fun read(
        @Header("Authorization") authorization: String,
        @Path("id") bookId: String?
    ): Book;

    @Headers("Content-Type: application/json")
    @POST("/api/book")
    suspend fun create(@Header("Authorization") authorization: String, @Body book: Book): Book

    @Headers("Content-Type: application/json")
    @PUT("/api/book/{id}")
    suspend fun update(
        @Header("Authorization") authorization: String,
        @Path("id") bookId: String?,
        @Body book: Book
    ): Book
}