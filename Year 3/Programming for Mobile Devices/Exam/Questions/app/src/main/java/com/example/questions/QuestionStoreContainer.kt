package com.example.questions


import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.example.questions.core.TAG
import com.example.questions.auth.data.AuthRepository
import com.example.questions.auth.data.remote.AuthDataSource
import com.example.questions.core.data.UserPreferencesRepository
import com.example.questions.core.data.remote.Api
import com.example.questions.core.utils.ConnectivityManagerNetworkMonitor
import com.example.questions.todo.data.QuestionRepository
import com.example.questions.todo.data.remote.QuestionService
import com.example.questions.todo.data.remote.QuestionWsClient
import com.example.questions.todo.data.remote.QuestionApi

val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class QuestionStoreContainer(val context: Context) {
    init {
        Log.d(TAG, "init")
    }

    private val questionService: QuestionService = Api.retrofit.create(QuestionService::class.java)
    private val questionWsClient: QuestionWsClient = QuestionWsClient(Api.okHttpClient)
    private val authDataSource: AuthDataSource = AuthDataSource()

    private val database: QuestionStoreAndroidDatabase by lazy { QuestionStoreAndroidDatabase.getDatabase(context) }

    val questionRepository: QuestionRepository by lazy {
        QuestionRepository(questionService, questionWsClient, database.questionDao(), ConnectivityManagerNetworkMonitor(context))
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(authDataSource)
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.userPreferencesDataStore)
    }
    // init QuestionApi QuestionService
    init {
        QuestionApi.questionRepository = questionRepository
    }
}