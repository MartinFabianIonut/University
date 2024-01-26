package com.example.questions.todo.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import com.example.questions.todo.data.Question
interface QuestionService {
    @GET("/question")
    suspend fun find(): List<Question>

    @GET("/question")
    suspend fun findWithAuth(@Header("Authorization") authorization: String): List<Question>

    @GET("/question/{id}")
    suspend fun read(
        @Path("id") questionId: Int?
    ): Question;

    @GET("/question/{id}")
    suspend fun readWithAuth(
        @Header("Authorization") authorization: String,
        @Path("id") questionId: Int?
    ): Question;

    @Headers("Content-Type: application/json")
    @POST("/question")
    suspend fun create(@Body question: Question): Question

    @Headers("Content-Type: application/json")
    @POST("/question")
    suspend fun createWithAuth(@Header("Authorization") authorization: String, @Body question: Question): Question

    @Headers("Content-Type: application/json")
    @PUT("/question/{id}")
    suspend fun update(
        @Path("id") questionId: Int?,
        @Body question: Question
    ): Question

    @Headers("Content-Type: application/json")
    @PUT("/question/{id}")
    suspend fun updateWithAuth(
        @Header("Authorization") authorization: String,
        @Path("id") questionId: Int?,
        @Body question: Question
    ): Question

}