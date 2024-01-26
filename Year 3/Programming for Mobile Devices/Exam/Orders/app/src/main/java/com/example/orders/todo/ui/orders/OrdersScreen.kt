package com.example.orders.todo.ui.orders

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
import com.example.orders.R

@Composable
fun OrdersScreen() {
    Log.d("OrdersScreen", "recompose")
    val ordersViewModel = viewModel<OrdersViewModel>(factory = OrdersViewModel.Factory)
    val ordersUiState by ordersViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    Scaffold(
        topBar = {
        },
        floatingActionButton = {

        }
    ) {
        OrderList(
            orderList = ordersUiState,
            modifier = Modifier.padding(it),
            ordersViewModel = ordersViewModel
        )
    }
}

@Preview
@Composable
fun PreviewOrdersScreen() {
    OrdersScreen()
}
