package com.example.questions

import android.os.Handler
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.questions.auth.LoginScreen
import com.example.questions.core.data.UserPreferences
import com.example.questions.core.data.remote.Api
import com.example.questions.core.ui.UserPreferencesViewModel
//import com.example.questions.todo.ui.question.QuestionScreen
import com.example.questions.todo.ui.questions.QuestionsScreen
import kotlinx.coroutines.delay

const val questionsRoute = "questions"
const val authRoute = "auth"
const val splashRoute = "splash"

@Composable
fun QuestionStoreAndroidNavHost() {
    val navController = rememberNavController()

    val userPreferencesViewModel =
        viewModel<UserPreferencesViewModel>(factory = UserPreferencesViewModel.Factory)
    val userPreferencesUiState by userPreferencesViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = UserPreferences()
    )
    val myAppViewModel = viewModel<QuestionStoreViewModel>(factory = QuestionStoreViewModel.Factory)
    NavHost(
        navController = navController,
        startDestination = splashRoute
    ) {
        // Splash screen
        composable(route = splashRoute) {
            SplashScreen {
                Handler().postDelayed({
                    if (userPreferencesUiState.token.isEmpty()) {
                        navController.navigate(authRoute) {
                            popUpTo(splashRoute) { inclusive = true }
                        }
                    }
                }, 1000)
            }
        }
        composable(questionsRoute) {
            QuestionsScreen(
                onQuestionClick = { questionId ->
                    Log.d("MyAppNavHost", "navigate to question $questionId")
                    navController.navigate("$questionsRoute/$questionId")
                },
                onAddQuestion = {
                    Log.d("MyAppNavHost", "navigate to new question")
                    navController.navigate("$questionsRoute-new")
                },
                onLogout = {
                    Log.d("MyAppNavHost", "logout")
                    myAppViewModel.logout()
                    Api.tokenInterceptor.token = null
                    navController.navigate(authRoute) {
                        popUpTo(0)
                    }
                })
        }
        composable(route = authRoute)
        {
            LoginScreen(
                onClose = {
                    Log.d("MyAppNavHost", "navigate to list")
                    navController.navigate(questionsRoute)
                }
            )
        }
    }
    LaunchedEffect(userPreferencesUiState.token) {
        if (userPreferencesUiState.token.isNotEmpty()) {
            delay(1500)
            Log.d("MyAppNavHost", "Lauched effect navigate to questions")
            Api.tokenInterceptor.token = userPreferencesUiState.token
            myAppViewModel.setToken(userPreferencesUiState.token)
            navController.navigate(questionsRoute) {
                popUpTo(0)
            }
        }

    }
//    LaunchedEffect(true){
//        delay(1500)
//        Log.d("MyAppNavHost", "Lauched effect navigate to questions")
//        Api.tokenInterceptor.token = userPreferencesUiState.token
//        myAppViewModel.setToken(userPreferencesUiState.token)
//        navController.navigate(questionsRoute) {
//            popUpTo(0)
//        }
//    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Add your welcome question with a nice font here
            Text(
                text = "Welcome!",
                style = TextStyle(
                    fontFamily = FontFamily.Cursive, // Replace with your desired font
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add any additional content or loading indicator as needed
            CircularProgressIndicator()

            // Call onTimeout when the splash screen timeout occurs
            LaunchedEffect(true) {
                onTimeout()
            }
        }
    }
}