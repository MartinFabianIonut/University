package com.example.questions.core.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.questions.QuestionStoreAndroidDatabase
import com.example.questions.todo.data.remote.QuestionApi

class MyWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val questionDao = QuestionStoreAndroidDatabase.getDatabase(applicationContext).questionDao()
        val dirtyQuestions = questionDao.getDirtyQuestions()
        dirtyQuestions.forEach {dirtyQuestion ->
            Log.d("MyWorker do work", dirtyQuestion.toString())
            val newQuestion = dirtyQuestion.copy(dirty = false)
            if (dirtyQuestion.id.toInt() > 0) {
                QuestionApi.updateQuestion(newQuestion)
            } else {
                QuestionApi.createQuestion(newQuestion)
                Log.d(
                    "Update from WorkManager",
                    newQuestion.toString()
                )
                Thread.sleep(1000)
                questionDao.deleteQuestion(dirtyQuestion)
                Log.d("Delete from WorkManager",
                    "SUCCESS : $dirtyQuestion deleted"
                )
            }
        }
        if (dirtyQuestions.isNotEmpty()){
            showSimpleNotificationWithTapAction(
                context,
                "Questions Channel",
                0,
                "Synchronization complete",
                "Your list of questions has been synchronized with the server."
            )
        }
        return Result.success()
    }
}