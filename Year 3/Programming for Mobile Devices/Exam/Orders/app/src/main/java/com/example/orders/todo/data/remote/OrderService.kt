package com.example.orders.todo.data.remote

import com.example.orders.todo.data.Item
import com.example.orders.todo.data.Order
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderService {
    @GET("/order")
    suspend fun find(): List<Order>

    @Headers("Content-Type: application/json")
    @POST("/order")
    suspend fun create(@Body order: Order): Order

    @Headers("Content-Type: application/json")
    @PUT("/order/{id}")
    suspend fun update(
        @Path("id") orderId: Int?,
        @Body order: Order
    ): Order

    @Headers("Content-Type: application/json")
    @POST("/item")
    suspend fun postItem(
        @Body item: Item
    ): Item
}
