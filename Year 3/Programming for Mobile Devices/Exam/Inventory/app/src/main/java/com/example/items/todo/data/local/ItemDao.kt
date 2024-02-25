package com.example.items.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.items.todo.data.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM Items")
    fun getAll(): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(products: List<Item>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(product: Item): Int

    @Delete
    suspend fun deleteItem(product: Item): Int

    @Query("DELETE FROM Items")
    suspend fun deleteAll()

    @Query("SELECT * FROM Items WHERE dirty = 1")
    suspend fun getDirtyItems(): Array<Item>
}