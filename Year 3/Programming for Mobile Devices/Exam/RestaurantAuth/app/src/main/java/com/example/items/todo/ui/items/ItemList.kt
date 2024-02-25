package com.example.items.todo.ui.items


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.items.todo.data.Item
import com.example.items.todo.data.OrderItem
import com.example.items.todo.ui.item.ItemViewModel
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

typealias OnItemFn = (id: Int?) -> Unit

@Composable
fun ItemList(
    itemList: List<Item>,
    orderItemList: List<OrderItem>,
    onItemClick: OnItemFn,
    modifier: Modifier
) {
    Log.d("ItemList", "recompose")
    var toShow by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var wasShown by remember { mutableStateOf(false) }
    LazyColumn(
        content = {
            items(itemList.size) { index ->
                val item = itemList[index]
                ItemDetail(item = item)
                if (item.specialOffer == true) {
                    toShow = true
                    text = "Special offer for: " + item.name
                }
            }
        },
        //add margin top 250.dp)
        modifier = modifier.padding(top = 70.dp)
    )
    LazyColumn(
        content = {
            items(orderItemList.size) { index ->
                val orderItem = orderItemList[index]
                OrderItemDetail(orderItem = orderItem)
            }
        },
        modifier = modifier.padding(top = 400.dp)
    )
    LaunchedEffect(toShow) {
        if (toShow && !wasShown){
            delay(1000)
            toShow = false
            text = ""
            wasShown = true
        }
    }
    ItemToShowToast(toShow = toShow, text = text)
}

@Composable
fun ItemToShowToast(toShow: Boolean, text: String) {
    Log.d("ItemToShowToast", "toShow = $toShow")
    AnimatedVisibility(
        visible = toShow && text.isNotEmpty(),
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
fun ItemDetail(
    item: Item
) {
    var isExpanded by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(0) }
    val itemsViewModel = viewModel<ItemViewModel>(factory = ItemViewModel.Factory(item.code))
    var ordered by remember { mutableStateOf(true) }
    var job by remember { mutableStateOf<Job?>(null) }
    Row {
        Column {
            ClickableText(text = AnnotatedString(item.name), onClick = {
                isExpanded = !isExpanded
            })
            if (isExpanded) {
                TextField(value = quantity.toString(), onValueChange = {
                    try {
                        quantity = it.toInt()
                    } catch (e: Exception) {
                        Log.d("ItemList", "Error: ${e.message}")
                        quantity = 0
                    }
                })
                Button(onClick = {
                    Log.d("ItemList", "Order item")
                    itemsViewModel.order(item, quantity) {
                        ordered = it
                    }
                    job?.cancel()
                    job = MainScope().launch {
                        delay(1000)
                        Log.d("Ordered: ", ordered.toString())
                        if (ordered) {
                            quantity = 0
                            isExpanded = false
                        }
                    }
                }) {
                    Text(text = "Order")
                }
            }
            if (!ordered) {
                ClickableText(text = AnnotatedString("Not sent"), onClick = {
                    itemsViewModel.order(item, quantity) {
                        ordered = it
                    }
                    Job().cancel()
                    job = MainScope().launch {
                        itemsViewModel.order(item, quantity) {
                            ordered = it
                        }
                        delay(1000)
                        Log.d("Ordered: ", ordered.toString())
                        if (ordered) {
                            quantity = 0
                            isExpanded = false
                        }
                    }
                })
            }
        }
    }
}

@Composable
fun OrderItemDetail(orderItem: OrderItem) {
    Row {
        Text(text = "Code: " + orderItem.code.toString() + " ")
        Text(text = "Table: " + orderItem.table.toString() + " ")
        Text(text = "Quantity: " + orderItem.quantity.toString() + " ")
    }
}
