package com.example.orders.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.orders.todo.data.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders")
    fun getAll(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE code = :code")
    suspend fun find(code: Int) : Order?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orders: List<Order>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(order: Order): Int

    @Delete
    suspend fun deleteOrder(order: Order): Int

    @Query("DELETE FROM orders")
    suspend fun deleteAll()

    @Query("SELECT * FROM orders WHERE dirty = 1")
    suspend fun getDirtyOrders(): Array<Order>
}