package com.example.items.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.items.todo.data.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemDao {
    @Query("SELECT * FROM order_items")
    fun getAll(): Flow<List<OrderItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: OrderItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<OrderItem>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: OrderItem): Int

    @Delete
    suspend fun deleteItem(item: OrderItem): Int

    @Query("DELETE FROM order_items")
    suspend fun deleteAll()

    @Query("SELECT * FROM order_items WHERE dirty = 1")
    suspend fun getDirtyItems(): Array<OrderItem>
}