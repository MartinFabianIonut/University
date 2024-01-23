package com.example.messages.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.messages.todo.data.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM Messages")
    fun getAll(): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(messages: List<Message>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(message: Message): Int

    @Delete
    suspend fun deleteMessage(message: Message): Int

    @Query("DELETE FROM Messages")
    suspend fun deleteAll()

    @Query("SELECT * FROM Messages WHERE dirty = 1")
    suspend fun getDirtyMessages(): Array<Message>
}