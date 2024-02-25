package com.example.items.todo.ui.items

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.items.R
import com.example.items.todo.data.Product
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(onItemClick: (id: Int?) -> Unit, onAddItem: () -> Unit, onLogout: () -> Unit) {
    Log.d("ItemsScreen", "recompose")
    val itemsViewModel = viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory)
    val itemsUiState by itemsViewModel.itemState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )

    val productState = itemsViewModel.productState
    var toSearch by remember { mutableStateOf("") }
    var productsToShow by remember { mutableStateOf(emptyList<Product>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.items)) },
                actions = {
                    if (itemsViewModel.repository.downloadState.show) {
                        Button(onClick = { itemsViewModel.startDownload() }) { Text("Download") }
                    }

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d("ItemsScreen", "Add a new item")

                }
            ) { Icon(Icons.Rounded.Add, "Add") }
        }
    ) {
        if (productState.isDownloading) {
            Text(
                "Downloading... ${productState.currentPage}/${productState.numberOfPages}",
                modifier = Modifier.padding(it)
            )
        } else if (productState.isDownloaded) {
            TextField(value = toSearch, onValueChange = {
                toSearch = it
            }, modifier = Modifier.padding(it).fillMaxWidth())
            ProductList(
                productList = productsToShow,
                itemList = itemsUiState,
                itemsViewModel = itemsViewModel,
                onItemClick = onItemClick,
                modifier = Modifier.padding(top = 150.dp)
            )
            LaunchedEffect(toSearch) {
                delay(2000)
                productsToShow = if (toSearch == "") {
                    emptyList()
                } else
                    itemsViewModel.search(toSearch)
            }

        }
    }
}

@Preview
@Composable
fun PreviewItemsScreen() {
    ItemsScreen(onItemClick = {}, onAddItem = {}, onLogout = {})
}
