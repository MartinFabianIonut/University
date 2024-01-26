package com.example.questions.todo.data.remote

import android.annotation.SuppressLint
import android.util.Log
import com.example.questions.todo.data.Question
import com.example.questions.todo.data.QuestionRepository

object QuestionApi {

    @SuppressLint("StaticFieldLeak")
    lateinit var questionRepository: QuestionRepository

    suspend fun createQuestion(question: Question): Boolean {
        return try {
            Log.d("Create from WorkManager", "question title: ${question.text}")
            questionRepository.save(question)
            true
        } catch (e: Exception) {
            Log.i("Create failed from WorkManager", e.toString())
            false
        }
    }

    suspend fun updateQuestion(question: Question): Boolean {
        return try {
            Log.d("Update from WorkManager", "question title: ${question.text}")
            questionRepository.update(question)
            true
        } catch (e: Exception) {
            Log.i("Update failed from WorkManager", e.toString())
            false
        }
    }
}
