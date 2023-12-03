package com.example.bookstoreandroid.todo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookstoreandroid.todo.data.Book
import kotlinx.coroutines.flow.Flow
@Dao
interface BookDao {
    @Query("SELECT * FROM Books")
    fun getAll(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(books: List<Book>)

    @Update
    suspend fun update(book: Book): Int

    @Query("DELETE FROM Books WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM Books")
    suspend fun deleteAll()
}