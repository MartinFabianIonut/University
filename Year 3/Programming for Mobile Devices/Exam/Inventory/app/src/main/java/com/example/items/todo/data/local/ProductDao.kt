package com.example.items.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.items.todo.data.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM Products")
    fun getAll(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(products: List<Product>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(product: Product): Int

    @Delete
    suspend fun deleteItem(product: Product): Int

    @Query("DELETE FROM Products")
    suspend fun deleteAll()

    @Query("SELECT * FROM Products WHERE dirty = 1")
    suspend fun getDirtyItems(): Array<Product>

    @Query("SELECT * FROM Products WHERE name LIKE :name")
    suspend fun findByName(name: String): List<Product>
}