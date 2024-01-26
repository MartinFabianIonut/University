package com.example.questions.todo.ui.questions

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.questions.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsScreen(onQuestionClick: (id: Int?) -> Unit, onAddQuestion: () -> Unit, onLogout: () -> Unit) {
    Log.d("QuestionsScreen", "recompose")
    val questionsViewModel = viewModel<QuestionsViewModel>(factory = QuestionsViewModel.Factory)
    val questionsUiState by questionsViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.questions)) },
                actions = {
                    Button(onClick = onLogout) { Text("Logout") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d("QuestionsScreen", "Add a new question")
                    onAddQuestion()
                }
            ) { Icon(Icons.Rounded.Add, "Add") }
        }
    ) {
        QuestionList(
            questionList = questionsUiState,
            onQuestionClick = onQuestionClick,
            modifier = Modifier.padding(it)
        )
    }
}

@Preview
@Composable
fun PreviewQuestionsScreen() {
    QuestionsScreen(onQuestionClick = {}, onAddQuestion = {}, onLogout = {})
}
