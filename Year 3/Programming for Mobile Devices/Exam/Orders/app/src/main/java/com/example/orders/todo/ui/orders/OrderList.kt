package com.example.orders.todo.ui.orders


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.orders.todo.data.Order
import com.example.orders.todo.ui.order.OrderViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class FilterOption {
    SHOW_ALL,
    SHOW_WITH_QUANTITIES
}

@Composable
fun OrderList(
    orderList: List<Order>,
    modifier: Modifier,
    ordersViewModel: OrdersViewModel
) {
    Log.d("OrderList", "recompose")
    var filterOption by remember { mutableStateOf(FilterOption.SHOW_ALL) }
    val filteredOrders = when (filterOption) {
        FilterOption.SHOW_ALL -> orderList
        FilterOption.SHOW_WITH_QUANTITIES -> orderList.filter { it.quantity != null }
    }

    var failedOrders by remember { mutableStateOf<List<Order>>(emptyList()) }
    var isError by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var isLoadingFromServer by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        if (isLoading || isLoadingFromServer) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(4.dp)
                    .align(Alignment.TopCenter)
            )
        }
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RadioButton(
                    selected = filterOption == FilterOption.SHOW_ALL,
                    onClick = { filterOption = FilterOption.SHOW_ALL },
                    modifier = Modifier
                        .background(Color.White)
                        .padding(4.dp)
                )
                Text(
                    "Show all orders",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RadioButton(
                    selected = filterOption == FilterOption.SHOW_WITH_QUANTITIES,
                    onClick = { filterOption = FilterOption.SHOW_WITH_QUANTITIES },
                    modifier = Modifier
                        .background(Color.White)
                        .padding(4.dp)
                )
                Text(
                    "Show orders with quantities",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(0.9f)
            ) {
                items(filteredOrders) {
                    isLoadingFromServer = false
                    val failed = failedOrders.find { f -> f == it }
                    val isFailed = failed != null
                    OrderDetail(
                        id = it.code,
                        order = it,
                        isHighlighted = isFailed
                    )
                }
            }

            val coroutineScope = rememberCoroutineScope()
            Row {
                Button(
                    onClick = {
                        failedOrders = emptyList()
                        coroutineScope.launch {
                            isLoading = true
                            for (order in orderList) {
                                if (order.quantity != null) {
                                    ordersViewModel.updateOrderWithQuantity(order) { isSuccess ->
                                        if (!isSuccess) {
                                            failedOrders = failedOrders + order
                                            isError = true
                                            Log.e("OrderList", "Failed updating order: $order")
                                        } else {
                                            Log.d("OrderList", "Success updating order: $order")
                                        }
                                    }
                                }
                            }
                            isLoading = false
                        }
                    },
                    modifier = Modifier.align(Alignment.Bottom)
                ) {
                    Text(text = "Submit")
                }
            }
        }

        LaunchedEffect(isError) {
            if (!isError) {
                delay(3500L)
                text = ""
            } else {
                delay(5000L)
                isError = false
            }
        }
        ErrorInOrder(error = isError, text = text)
    }
}

@Composable
fun ErrorInOrder(error: Boolean, text: String) {
    Log.d("CanAddAOrder", "canSave = $error")
    AnimatedVisibility(
        visible = error && text.isNotEmpty(),
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 1500, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 1500, easing = FastOutLinearInEasing)
        )
    ) {
        Surface(
            tonalElevation = 100.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 60.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.Red,
                            MaterialTheme.colorScheme.tertiary,
                            MaterialTheme.colorScheme.primary,
                            Color.Red
                        )
                    )
                )

        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun OrderDetail(id: Int, order: Order, isHighlighted: Boolean = false) {
    Log.d("OrderDetail", "recompose for $order")
    val orderViewModel = viewModel<OrderViewModel>(factory = OrderViewModel.Factory(id))
    val gradientColors = listOf(
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary
    )
    val textColor = MaterialTheme.colorScheme.onPrimary
    var isEditingQuantity by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var showButton by remember { mutableStateOf(false) }
    var quantityByRemember by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(
                shape = RoundedCornerShape(
                    topStart = CornerSize(5.dp),
                    topEnd = CornerSize(30.dp),
                    bottomStart = CornerSize(30.dp),
                    bottomEnd = CornerSize(5.dp)
                )
            )
            .background(brush = Brush.linearGradient(gradientColors))
            .clickable(onClick = {
                isEditingQuantity = !isEditingQuantity
            })
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Log.d("OrderDetail", "quantity = $quantityByRemember")
        Text(
            text = order.name,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = if (isHighlighted) Color.Red else textColor
            ),
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = quantityByRemember,
            onValueChange = {
                showButton = true
                quantityByRemember = it
            },
            readOnly = !isEditingQuantity,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = if (isHighlighted) Color.Red else textColor
            ),
            modifier = Modifier.width(90.dp)
        )
        if (showButton) {
            Button(onClick = {
                try {
                    val q = quantityByRemember.toInt()
                    val newOrder = order.copy(quantity = q)
                    orderViewModel.updateOrderWithQuantity(newOrder)
                } catch (e: Exception) {
                    Log.d("OrderDetail", "Error updating order with quantity: ${e.message}")
                    isError = true
                    text = e.message ?: "Server indisponibil"
                }
                isEditingQuantity = false
                showButton = false
            }) {
                Text(text = "Confirm")
            }
        }
    }

    ErrorInOrder(error = isError, text = text)
    LaunchedEffect(isError) {
        if (!isError) {
            delay(3500L)
            text = ""
        } else {
            delay(3000L)
            isError = false
        }
    }
    LaunchedEffect(order.quantity){
        if (quantityByRemember  != order.quantity.toString())
        {
            quantityByRemember = order.quantity.toString()
        }
    }
}
