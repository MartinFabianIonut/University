package com.example.questions.todo.ui.questions


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.questions.todo.data.Question
import com.example.questions.todo.ui.question.QuestionViewModel
import kotlinx.coroutines.delay

typealias OnQuestionFn = (id: Int?) -> Unit

@Composable
fun QuestionList(questionList: List<Question>, onQuestionClick: OnQuestionFn, modifier: Modifier) {
    Log.d("QuestionList", "recompose")

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    var totalAnsweredQuestions by remember { mutableIntStateOf(0) }
    var awaitingSubmit by remember { mutableStateOf(true) }

    QuestionHeader(
        totalAnsweredQuestions = totalAnsweredQuestions,
        totalQuestions = questionList.size,
        correctAnswers = correctAnswers
    )

    if (questionList.isNotEmpty() && currentQuestionIndex == 0)
    {
        currentQuestionIndex = questionList.indexOfFirst { it.selectedIndex == null }
        Log.d("QuestionList", "currentQuestion is $questionList")
        var totalAns = 0
        var correctAns = 0
        questionList.forEach {
            if (it.selectedIndex != null) {
                totalAns++
                if (it.selectedIndex == it.indexCorrectOption) {
                    correctAns++
                }
            }
        }
        totalAnsweredQuestions = totalAns
        correctAnswers = correctAns
    }
    if (currentQuestionIndex < questionList.size) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "\n\n")
            QuestionDetail(questionList[currentQuestionIndex], onAnswerSubmit =
            {
                    answer ->
                run {
                    Log.d("QuestionList", "Answer submitted is correct: $answer")
                    totalAnsweredQuestions++
                    if (answer != false) {
                        correctAnswers++
                    }
                    awaitingSubmit = false
                    currentQuestionIndex++
                }
            })
        }

        LaunchedEffect(currentQuestionIndex, awaitingSubmit) {
            Log.d("QuestionList", "LaunchedEffect start waiting for 10 seconds")
            delay(10000)
            if (awaitingSubmit) {
                currentQuestionIndex++
                totalAnsweredQuestions++
            }
            else
            {
                awaitingSubmit = true
            }
            Log.d("QuestionList", "LaunchedEffect end waiting for 10 seconds")
        }
    } else {
        // All questions answered
        Text("\n\n\n\nAll questions answered")
    }
}


@Composable
fun QuestionHeader(
    totalAnsweredQuestions: Int,
    totalQuestions: Int,
    correctAnswers: Int
) {
    Text(
        text = "\n\n\n\t\tQuestions $totalAnsweredQuestions/$totalQuestions, Correct answers: $correctAnswers/$totalAnsweredQuestions",
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun QuestionDetail(
    question: Question,
    onAnswerSubmit: (answer : Boolean?) -> Unit
) {
    val gradientColors = listOf(
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary
    )
    val textColor = MaterialTheme.colorScheme.onPrimary
    var selectedIndex by remember { mutableStateOf(question.selectedIndex) }

    val questionViewModel = viewModel<QuestionViewModel>(factory = QuestionViewModel.Factory(question.id))


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(46.dp)
            .clip(
                shape = RoundedCornerShape(
                    topStart = CornerSize(5.dp),
                    topEnd = CornerSize(30.dp),
                    bottomStart = CornerSize(30.dp),
                    bottomEnd = CornerSize(5.dp)
                )
            )
            .background(brush = Brush.linearGradient(gradientColors))
            .padding(12.dp)
    ) {
        Column {
            Row {
                Text(
                    text = "Text: ${question.text}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = textColor
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            question.options.forEachIndexed { index, option ->
                Log.d("QuestionDetail", "option: $option")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                        .background(if (index == question.selectedIndex) Color.Magenta else Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .background(if (index == selectedIndex) Color.Magenta else Color.Transparent)
                            .clickable {
                                questionViewModel.updateLocally(
                                    question.id,
                                    question.text,
                                    question.options,
                                    question.indexCorrectOption,
                                    index)
                                selectedIndex = index
                            }
                    ) {
                        Text(
                            text = "Option: $option",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
            Row {
                Button(onClick = {
                    val isAnswerCorrect = question.selectedIndex == question.indexCorrectOption
                    onAnswerSubmit(isAnswerCorrect)
                }) {
                    Text("Next")
                }
            }
        }
    }
}
