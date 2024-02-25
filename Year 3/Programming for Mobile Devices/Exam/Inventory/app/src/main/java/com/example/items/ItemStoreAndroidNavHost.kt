package com.example.items

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.items.core.data.UserPreferences
import com.example.items.core.data.remote.Api
import com.example.items.core.ui.UserPreferencesViewModel
import com.example.items.todo.ui.items.ItemsScreen
import kotlinx.coroutines.delay

const val itemsRoute = "items"
const val authRoute = "auth"
const val splashRoute = "splash"

@Composable
fun ItemStoreAndroidNavHost() {
    val navController = rememberNavController()
    val onCloseItem = {
        Log.d("MyAppNavHost", "navigate back to list")
        navController.popBackStack()
    }
    val userPreferencesViewModel =
        viewModel<UserPreferencesViewModel>(factory = UserPreferencesViewModel.Factory)
    val userPreferencesUiState by userPreferencesViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = UserPreferences()
    )
    val myAppViewModel = viewModel<ItemStoreViewModel>(factory = ItemStoreViewModel.Factory)
    NavHost(
        navController = navController,
        startDestination = splashRoute
    ) {
        // Splash screen
        composable(route = splashRoute) {
            SplashScreen {
                Handler().postDelayed({
                    //if no auth comment
//                    if (userPreferencesUiState.token.isEmpty()) {
//                        navController.navigate(authRoute) {
//                            popUpTo(splashRoute) { inclusive = true }
//                        }
//                    }
                }, 1000)
            }
        }
        composable(itemsRoute) {
            ItemsScreen(
                onItemClick = { itemId ->
                    Log.d("MyAppNavHost", "navigate to item $itemId")
                    navController.navigate("$itemsRoute/$itemId")
                },
                onAddItem = {
                    Log.d("MyAppNavHost", "navigate to new item")
                    navController.navigate("$itemsRoute-new")
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
//        composable(
//            route = "$itemsRoute/{id}",
//            arguments = listOf(navArgument("id") { type = NavType.StringType })
//        )
//        {
//            ItemScreen(
//                itemId = it.arguments?.getInt("id"),
//                onClose = { onCloseItem() }
//            )
//        }

//        composable(route = "$itemsRoute-new")
//        {
//            ItemScreen(
//                itemId = null,
//                onClose = { onCloseItem() }
//            )
//        }
//        composable(route = authRoute)
//        {
//            LoginScreen(
//                onClose = {
//                    Log.d("MyAppNavHost", "navigate to list")
//                    navController.navigate(itemsRoute)
//                }
//            )
//        }
    }
//    LaunchedEffect(userPreferencesUiState.token) {
//        if (userPreferencesUiState.token.isNotEmpty()) {
//            delay(1500)
//            Log.d("MyAppNavHost", "Lauched effect navigate to items")
//            Api.tokenInterceptor.token = userPreferencesUiState.token
//            myAppViewModel.setToken(userPreferencesUiState.token)
//            navController.navigate(itemsRoute) {
//                popUpTo(0)
//            }
//        }
//
//    }
    LaunchedEffect(true){
        delay(1500)
        Log.d("MyAppNavHost", "Lauched effect navigate to items")
        Api.tokenInterceptor.token = userPreferencesUiState.token
        myAppViewModel.setToken(userPreferencesUiState.token)
        navController.navigate(itemsRoute) {
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
            // Add your welcome item with a nice font here
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