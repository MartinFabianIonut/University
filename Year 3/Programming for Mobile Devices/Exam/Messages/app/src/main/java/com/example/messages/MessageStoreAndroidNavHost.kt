package com.example.messages

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
import com.example.messages.auth.LoginScreen
import com.example.messages.core.data.UserPreferences
import com.example.messages.core.data.remote.Api
import com.example.messages.core.ui.UserPreferencesViewModel
import com.example.messages.todo.ui.message.MessageScreen
import com.example.messages.todo.ui.messages.MessagesScreen
import kotlinx.coroutines.delay

const val messagesRoute = "messages"
const val authRoute = "auth"
const val splashRoute = "splash"

@Composable
fun MessageStoreAndroidNavHost() {
    val navController = rememberNavController()
    val onCloseMessage = {
        Log.d("MyAppNavHost", "navigate back to list")
        navController.popBackStack()
    }
    val userPreferencesViewModel =
        viewModel<UserPreferencesViewModel>(factory = UserPreferencesViewModel.Factory)
    val userPreferencesUiState by userPreferencesViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = UserPreferences()
    )
    val myAppViewModel = viewModel<MessageStoreViewModel>(factory = MessageStoreViewModel.Factory)
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
        composable(messagesRoute) {
            MessagesScreen(
                onMessageClick = { messageId ->
                    Log.d("MyAppNavHost", "navigate to message $messageId")
                    navController.navigate("$messagesRoute/$messageId")
                },
                onAddMessage = {
                    Log.d("MyAppNavHost", "navigate to new message")
                    navController.navigate("$messagesRoute-new")
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
        composable(
            route = "$messagesRoute/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        )
        {
            MessageScreen(
                messageId = it.arguments?.getInt("id"),
                onClose = { onCloseMessage() }
            )
        }
//        composable(route = "$messagesRoute-new")
//        {
//            MessageScreen(
//                messageId = null,
//                onClose = { onCloseMessage() }
//            )
//        }
        composable(route = authRoute)
        {
            LoginScreen(
                onClose = {
                    Log.d("MyAppNavHost", "navigate to list")
                    navController.navigate(messagesRoute)
                }
            )
        }
    }
    LaunchedEffect(userPreferencesUiState.token) {
//        if (userPreferencesUiState.token.isNotEmpty()) {
//            delay(1500)
//            Log.d("MyAppNavHost", "Lauched effect navigate to messages")
//            Api.tokenInterceptor.token = userPreferencesUiState.token
//            myAppViewModel.setToken(userPreferencesUiState.token)
//            navController.navigate(messagesRoute) {
//                popUpTo(0)
//            }
//        }

    }
    LaunchedEffect(true){
        delay(1500)
        Log.d("MyAppNavHost", "Lauched effect navigate to messages")
        Api.tokenInterceptor.token = userPreferencesUiState.token
        myAppViewModel.setToken(userPreferencesUiState.token)
        navController.navigate(messagesRoute) {
            popUpTo(0)
        }
    }
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
            // Add your welcome message with a nice font here
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