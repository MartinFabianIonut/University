package com.example.questions.todo.data

import android.content.Context
import android.util.Log
import com.example.questions.core.TAG
import com.example.questions.core.data.remote.Api
import com.example.questions.core.utils.ConnectivityManagerNetworkMonitor
import com.example.questions.core.utils.showSimpleNotificationWithTapAction
import com.example.questions.todo.data.local.QuestionDao
import com.example.questions.todo.data.remote.QuestionEvent
import com.example.questions.todo.data.remote.QuestionService
import com.example.questions.todo.data.remote.QuestionWsClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class QuestionRepository (
    private val questionService: QuestionService,
    private val questionWsClient: QuestionWsClient,
    private val questionDao: QuestionDao,
    private val connectivityManagerNetworkMonitor: ConnectivityManagerNetworkMonitor
) {
    val questionStream by lazy {
        Log.d(TAG, "Perform a getAll query")
        val flow = questionDao.getAll()
        Log.d(TAG, "Get all questions from the database SUCCEEDED")
        flow
    }

    private lateinit var context: Context

    init {
        Log.d(TAG, "init")
    }

    // private fun getBearerToken() = "Bearer ${Api.tokenInterceptor.token}"

    suspend fun clearDao() {
        questionDao.deleteAll()
    }

    suspend fun refresh() {
        Log.d(TAG, "refresh started")
        try {
            //val authorization = getBearerToken()
            val questions = questionService.find()
            questionDao.deleteAll()
            questions.forEach { questionDao.insert(it) }
            Log.d(TAG, "refresh succeeded")
            for (question in questions) {
                Log.d(TAG, question.toString())
            }
        } catch (e: Exception) {
            Log.w(TAG, "refresh failed", e)
        }
    }

    suspend fun openWsClient() {
        Log.d(TAG, "openWsClient")
        withContext(Dispatchers.IO) {
            getQuestionEvents().collect {
                Log.d(TAG, "Question event collected $it")
                if (it.isSuccess) {
                    val questionEvent = it.getOrNull();
                    when (questionEvent?.type) {
                        "created" -> handleQuestionCreated(questionEvent.payload)
                        "updated" -> handleQuestionUpdated(questionEvent.payload)
                        "deleted" -> handleQuestionDeleted(questionEvent.payload)
                        null -> handleQuestionCreated(questionEvent?.payload ?: Question())
                    }
                    Log.d(TAG, "Question event handled $questionEvent, notify the user")
                    showSimpleNotificationWithTapAction(
                        context,
                        "Questions Channel",
                        0,
                        "External change detected",
                        "Your list of questions has been updated. Tap to refresh."
                    )
                    Log.d(TAG, "Question event handled $questionEvent, notify the user SUCCEEDED")
                }
            }
        }
    }

    suspend fun closeWsClient() {
        Log.d(TAG, "closeWsClient")
        withContext(Dispatchers.IO) {
            questionWsClient.closeSocket()
        }
    }

    private suspend fun getQuestionEvents(): Flow<Result<QuestionEvent>> = callbackFlow {
        Log.d(TAG, "getQuestionEvents started")
        questionWsClient.openSocket(
            onEvent = {
                Log.d(TAG, "onEvent $it")
                if (it != null) {
                    trySend(kotlin.Result.success(it))
                }
            },
            onClosed = { close() },
            onFailure = { close() });
        awaitClose { questionWsClient.closeSocket() }
    }

    suspend fun selectedIndexSave(question: Question) {
        Log.d(TAG, "selectedIndexSave $question...")
        questionDao.update(question)
    }

    suspend fun update(question: Question): Question {
        Log.d(TAG, "update $question...")
        return if (isOnline()) {
            try {
                val updatedQuestion = questionService.update(questionId = question.id, question = question)
                Log.d(TAG, "update on SERVER -> $question SUCCEEDED")
                handleQuestionUpdated(updatedQuestion)
                updatedQuestion
            } catch (e: Exception) {
                Log.d(TAG, "update on SERVER -> $question FAILED")
                question
            }
        } else {
            Log.d(TAG, "update $question locally")
            val dirtyQuestion = question.copy(dirty = true)
            handleQuestionUpdated(dirtyQuestion)
            dirtyQuestion
        }
    }

    suspend fun save(question: Question): Question {
        Log.d(TAG, "save $question...")
        return if (isOnline()) {
            val createdQuestion = questionService.create(question = question)
            Log.d(TAG, "save ON SERVER the question $createdQuestion SUCCEEDED")
            handleQuestionCreated(createdQuestion)
            createdQuestion
        } else {
            Log.d(TAG, "save $question locally")
            val dirtyQuestion = question.copy(dirty = true)
            handleQuestionCreated(dirtyQuestion)
            dirtyQuestion
        }
    }

    suspend fun get (questionId: Int) {
        Log.d(TAG, "get $questionId...")
        try {
            val question = questionService.read(questionId = questionId)
            Log.d(TAG, "get $questionId on SERVER -> $question SUCCEEDED")
            questionDao.insert(question)
            Log.d(TAG, "question $questionId saved in the local database")
        } catch (e: Exception) {
            Log.d(TAG, "get $questionId on SERVER -> FAILED with exception $e")
            throw e
        }
    }

    private suspend fun isOnline(): Boolean {
        Log.d(TAG, "verify online state...")
        return connectivityManagerNetworkMonitor.isOnline.first()
    }

    private suspend fun handleQuestionDeleted(question: Question) {
        Log.d(TAG, "handleQuestionDeleted - todo $question")
    }

    suspend fun handleQuestionUpdated(question: Question) {
        Log.d(TAG, "handleQuestionUpdated... $question")
        val updated = questionDao.update(question)
        Log.d("handleQuestionUpdated exited with code = ", updated.toString())
    }

    private suspend fun handleQuestionCreated(question: Question) {
        Log.d(TAG, "handleQuestionCreated... for question $question")
        if (question.id >= 0) {
            Log.d(TAG, "handleQuestionCreated - insert/update an existing question")
            questionDao.insert(question)
            Log.d(TAG, "handleQuestionCreated - insert/update an existing question SUCCEEDED")
        } else {
            val randomNumber = (-10000000..-1).random()
            val localQuestion = question.copy(id = randomNumber)
            Log.d(TAG, "handleQuestionCreated - create a new question locally $localQuestion")
            questionDao.insert(localQuestion)
            Log.d(TAG, "handleQuestionCreated - create a new question locally SUCCEEDED")
        }
    }

    suspend fun deleteAll() {
        questionDao.deleteAll()
    }

    fun setToken(token: String) {
        questionWsClient.authorize(token)
    }

    fun setContext(context: Context) {
        this.context = context
    }
}