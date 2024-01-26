package com.example.questions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.questions.core.TAG
import com.example.questions.core.data.UserPreferences
import com.example.questions.core.data.UserPreferencesRepository
import com.example.questions.todo.data.QuestionRepository
import kotlinx.coroutines.launch

class QuestionStoreViewModel (
    private val userPreferencesRepository: UserPreferencesRepository,
    private val questionRepository: QuestionRepository
) :
    ViewModel() {

    init {
        Log.d(TAG, "init")
    }

    fun logout() {
        viewModelScope.launch {
            questionRepository.deleteAll()
            userPreferencesRepository.save(UserPreferences())
        }
    }

    fun setToken(token: String) {
        questionRepository.setToken(token)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuestionStoreAndroid)
                QuestionStoreViewModel(
                    app.container.userPreferencesRepository,
                    app.container.questionRepository
                )
            }
        }
    }
}
