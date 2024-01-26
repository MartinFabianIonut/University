package com.example.questions.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.questions.todo.data.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM Questions")
    fun getAll(): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questions: List<Question>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(question: Question): Int

    @Delete
    suspend fun deleteQuestion(question: Question): Int

    @Query("DELETE FROM Questions")
    suspend fun deleteAll()

    @Query("SELECT * FROM Questions WHERE dirty = 1")
    suspend fun getDirtyQuestions(): Array<Question>
}