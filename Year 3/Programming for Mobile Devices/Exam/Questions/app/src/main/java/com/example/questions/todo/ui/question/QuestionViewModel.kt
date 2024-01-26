package com.example.questions.todo.ui.question

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.questions.QuestionStoreAndroid
import com.example.questions.core.Result
import com.example.questions.core.TAG
import com.example.questions.todo.data.Question
import com.example.questions.todo.data.QuestionRepository
import kotlinx.coroutines.launch


data class QuestionUiState(
    val questionId: Int? = null,
    val question: Question = Question(),
    var loadResult: Result<Question>? = null,
    var submitResult: Result<Question>? = null,
)

class QuestionViewModel(private val questionId: Int?, private val questionRepository: QuestionRepository) :
    ViewModel() {

    var uiState: QuestionUiState by mutableStateOf(QuestionUiState(loadResult = Result.Loading))
        private set

    init {
        Log.d(TAG, "init")
        if (questionId != null) {
            loadQuestion()
        } else {
            uiState = uiState.copy(loadResult = Result.Success(Question()))
        }
    }

    private fun loadQuestion() {
        viewModelScope.launch {
            questionRepository.questionStream.collect { questions ->
                Log.d(TAG, "Collecting questions")
                if (uiState.loadResult !is Result.Loading) {
                    return@collect
                }
                Log.d(TAG, "Collecting questions - loadResult is loading, attempting to find question with id: $questionId")
                val question = questions.find { it.id == questionId } ?: Question()
                Log.d(TAG, "Collecting questions - question: $question")
                uiState = uiState.copy(question = question, loadResult = Result.Success(question))
            }
        }
    }

    fun updateLocally(
        id: Int,
        text: String,
        options: List<Int>,
        indexCorrectOption: Int,
        selectedIndex: Int
    ) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(submitResult = Result.Loading)

                val question = uiState.question.copy(
                    id = id,
                    text = text,
                    options = options,
                    indexCorrectOption = indexCorrectOption,
                    selectedIndex = selectedIndex
                )

                questionRepository.handleQuestionUpdated(question)

                uiState = uiState.copy(submitResult = Result.Success(question))
            } catch (e: Exception) {
                uiState = uiState.copy(submitResult = Result.Error(e))
            }
        }
    }


    companion object {
        fun Factory(questionId: Int?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuestionStoreAndroid)
                QuestionViewModel(questionId, app.container.questionRepository)
            }
        }
    }
}
