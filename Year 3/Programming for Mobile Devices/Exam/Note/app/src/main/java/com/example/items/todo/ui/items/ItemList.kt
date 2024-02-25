package com.example.items.todo.ui.items


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.items.todo.data.Item
import com.example.items.todo.ui.item.ItemViewModel
import kotlinx.coroutines.delay

typealias OnItemFn = (id: Int?) -> Unit

@Composable
fun ItemList(itemList: List<Item>, onItemClick: OnItemFn, modifier: Modifier) {
    Log.d("ItemList", "recompose")
    var lastModified by remember { mutableStateOf("") }
    val itemsViewModel = viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory)

    // get the biggest date from the list of items
    if (itemList.isNotEmpty()) {
        lastModified = itemList.maxBy { it.date }.date
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(itemList) { note ->
            ItemDetail(item = note, lastModified, itemsViewModel)
        }
    }
}

fun fetchNotes(
    ifModifiedSince: String,
    itemsViewModel: ItemsViewModel,
    page: Int,
    onResult: (Boolean) -> Unit
) {
    try {
        Log.d("fetchNotes", "fetchNotes AT current time milis: ${System.currentTimeMillis()}")
        itemsViewModel.getNotesCached(ifModifiedSince, page) { isSuccess ->
            if (isSuccess) {
                Log.d("fetchNotes", "fetchNotes SUCCEEDED")
                onResult(true)
            } else {
                Log.d("fetchNotes", "fetchNotes FAILED")
                onResult(false)
            }
        }
    } catch (e: Exception) {
        Log.e("fetchNotes", "fetchNotes error: ${e.message}")
    }
}

@Composable
fun ItemDetail(
    item: Item,
    lastModified: String,
    itemsViewModel: ItemsViewModel
) {
    val gradientColors = listOf(
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary
    )
    val textColor = MaterialTheme.colorScheme.onPrimary
    var isDeleted by remember { mutableStateOf(true) }
    var really by remember { mutableStateOf(false) }

    val itemViewModel = viewModel<ItemViewModel>(factory = ItemViewModel.Factory(item.id))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(26.dp)
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
                    text = "Text: ${item.text}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (!isDeleted) MaterialTheme.colorScheme.error else textColor
                    )
                )
                Button(onClick = {
                    really = !really
                }
                ) {
                    Text("Delete", color = textColor)
                }
                if (really) {
                    Button(onClick = {
                        itemViewModel.deleteItem(item.id) {
                            isDeleted = it
                        }
                        really = !really
                    }
                    ) {
                        Text("Really", color = textColor)
                    }
                }
            }
        }
        LaunchedEffect(Unit){
            Log.d("ItemList", "LaunchedEffect")
            // if is the last item in the list, scroll to it
            if (!itemsViewModel.isInList(item.id + 1)) {
                Log.d("ItemList", "LaunchedEffect fetchNotes for item: ${item.id}")
                val currentPage = item.id / 10 + 1
                val modified = item.date
                fetchNotes(modified, itemsViewModel, currentPage) { isSuccess: Boolean ->
                    if (isSuccess) {
                        Log.d("ItemList", "fetchNotes SUCCEEDED")
                    } else {
                        Log.d("ItemList", "fetchNotes FAILED")
                    }
                }
            }
        }
        LaunchedEffect(isDeleted){
//            while(!isDeleted){
//                delay(5000)
//                itemsViewModel.reopenWsConnection()
//                itemViewModel.deleteItem(item.id) {
//                    isDeleted = it
//                }
//            }
        }
    }
}
