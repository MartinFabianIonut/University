package com.example.questions.todo.ui.questions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.questions.QuestionStoreAndroid
import com.example.questions.core.TAG
import com.example.questions.todo.data.Question
import com.example.questions.todo.data.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuestionsViewModel(private val questionRepository: QuestionRepository) : ViewModel() {
    val uiState: Flow<List<Question>> = questionRepository.questionStream

    init {
        Log.d(TAG, "init")
        //loadQuestions()
    }

    private fun loadQuestions() {
        Log.d(TAG, "loadQuestions...")
        viewModelScope.launch {
            questionRepository.refresh()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuestionStoreAndroid)
                QuestionsViewModel(app.container.questionRepository)
            }
        }
    }
}
