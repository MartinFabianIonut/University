package com.example.items.todo.ui.items

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.items.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(onItemClick: (id: Int?) -> Unit, onAddItem: () -> Unit, onLogout: () -> Unit) {
    Log.d("ItemsScreen", "recompose")
    val itemsViewModel = viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory)
    val itemsUiState by itemsViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    val orderItemsUiState by itemsViewModel.orderUiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    var textValue by remember { mutableStateOf(TextFieldValue("")) }
    // Debounce logic
    var job by remember { mutableStateOf<Job?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.items)) },
                actions = {
                    Button(onClick = onLogout) { Text("Logout") }
                }
            )
        }
    ) {
        TextField(
            value = textValue,
            onValueChange = {
                // Cancel the previous job if any
                job?.cancel()

                // Schedule a new job after 2 seconds
                job = MainScope().launch {
                    val timeNow = System.currentTimeMillis()
                    delay(2000)  // Debounce duration in milliseconds
                    Log.d("ItemsScreen", "Debounced value $it after: ${System.currentTimeMillis() - timeNow}")
                    itemsViewModel.refreshItems(it.text)
                }

                // Update the text value
                textValue = it
            },
            //add margin top 80.dp
            modifier = Modifier.padding(0.dp, 80.dp, 0.dp, 0.dp)
        )
        ItemList(itemList = itemsUiState,
            orderItemList = orderItemsUiState,
            onItemClick = onItemClick,
            modifier = Modifier.padding(it)
        )
    }

}

@Preview
@Composable
fun PreviewItemsScreen() {
    ItemsScreen(onItemClick = {}, onAddItem = {}, onLogout = {})
}
