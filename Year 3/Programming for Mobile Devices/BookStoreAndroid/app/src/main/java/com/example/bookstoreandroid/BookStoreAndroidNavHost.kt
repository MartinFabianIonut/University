package com.example.bookstoreandroid

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookstoreandroid.auth.LoginScreen
import com.example.bookstoreandroid.core.data.UserPreferences
import com.example.bookstoreandroid.core.data.remote.Api
import com.example.bookstoreandroid.core.ui.UserPreferencesViewModel
import com.example.bookstoreandroid.todo.ui.book.BookScreen
import com.example.bookstoreandroid.todo.ui.books.BooksScreen

const val booksRoute = "books"
const val authRoute = "auth"

@Composable
fun BookStoreAndroidNavHost() {
    val navController = rememberNavController()
    val onCloseBook = {
        Log.d("MyAppNavHost", "navigate back to list")
        navController.popBackStack()
    }
    val userPreferencesViewModel =
        viewModel<UserPreferencesViewModel>(factory = UserPreferencesViewModel.Factory)
    val userPreferencesUiState by userPreferencesViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = UserPreferences()
    )
    val myAppViewModel = viewModel<BookStoreViewModel>(factory = BookStoreViewModel.Factory)
    NavHost(
        navController = navController,
        startDestination = authRoute
    ) {
        composable(booksRoute) {
            BooksScreen(
                onBookClick = { bookId ->
                    Log.d("MyAppNavHost", "navigate to book $bookId")
                    navController.navigate("$booksRoute/$bookId")
                },
                onAddBook = {
                    Log.d("MyAppNavHost", "navigate to new book")
                    navController.navigate("$booksRoute-new")
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
            route = "$booksRoute/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        )
        {
            BookScreen(
                bookId = it.arguments?.getString("id"),
                onClose = { onCloseBook() }
            )
        }
        composable(route = "$booksRoute-new")
        {
            BookScreen(
                bookId = null,
                onClose = { onCloseBook() }
            )
        }
        composable(route = authRoute)
        {
            LoginScreen(
                onClose = {
                    Log.d("MyAppNavHost", "navigate to list")
                    navController.navigate(booksRoute)
                }
            )
        }
    }
    LaunchedEffect(userPreferencesUiState.token) {
        if (userPreferencesUiState.token.isNotEmpty()) {
            Log.d("MyAppNavHost", "Lauched effect navigate to books")
            Api.tokenInterceptor.token = userPreferencesUiState.token
            myAppViewModel.setToken(userPreferencesUiState.token)
            navController.navigate(booksRoute) {
                popUpTo(0)
            }
        }
    }
}