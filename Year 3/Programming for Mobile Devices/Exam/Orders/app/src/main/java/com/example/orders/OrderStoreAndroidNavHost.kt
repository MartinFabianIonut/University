package com.example.orders

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.orders.todo.ui.orders.OrdersScreen

const val ordersRoute = "orders"
const val splashRoute = "splash"

@Composable
fun OrderStoreAndroidNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = splashRoute
    ) {
        // Splash screen
        composable(route = splashRoute) {
            SplashScreen {
                Handler().postDelayed({
                }, 1000)
            }
        }
        composable(ordersRoute) {
            OrdersScreen()
        }

    }

    LaunchedEffect(true){
        Log.d("MyAppNavHost", "Lauched effect navigate to orders")
        navController.navigate(ordersRoute) {
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
            // Add your welcome order with a nice font here
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