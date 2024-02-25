package com.example.items.todo.ui.items

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.items.todo.data.Item
import com.example.items.todo.data.Product
import com.example.items.todo.ui.item.ItemViewModel

typealias OnItemFn = (id: Int?) -> Unit

@Composable
fun ProductList(productList: List<Product>, itemList: List<Item>, itemsViewModel: ItemsViewModel,onItemClick: OnItemFn, modifier: Modifier) {
    Log.d("ItemList", "recompose")

    LazyColumn(content = {
        items(productList) { product ->
            ProductDetail(product = product)
        }
    }, modifier = modifier)
    LazyColumn(content = {
        items(itemList) { item ->
            Log.d("ItemList", "Item ${item.id} -> ${item.quantity}")
            ItemDetail(item = item)
        }
    }, modifier = Modifier.padding(top = 350.dp))
    if (itemList.isNotEmpty()) {
        Row(modifier = Modifier.padding(top = 700.dp)){
            Button(onClick = {
                Log.d("ItemList", "Update all items")
                itemsViewModel.uploadAllItems()
            }) {
                Text(text = "Upload")
            }
            if (itemsViewModel.uploadState.isUploading) {
                Text(text = "Uploading ${itemsViewModel.uploadState.currentItemIndex} / ${itemsViewModel.uploadState.totalItems} items")
            }

        }

    }
}

@Composable
fun ProductDetail(
    product: Product,
) {
    var isExpended by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(0) }
    val itemViewModel = viewModel<ItemViewModel>(factory = ItemViewModel.Factory(product.code))

    Row {
        ClickableText(text = AnnotatedString(product.name), onClick = {
            isExpended = !isExpended
        }, style = MaterialTheme.typography.bodyLarge)

        if (isExpended) {
            TextField(value = quantity.toString(), onValueChange = {
                quantity = if (it.isEmpty()) {
                    0
                } else {
                    try {
                        it.toInt()
                    } catch (e: Exception) {
                        0
                    }
                }

            })
            if (quantity > 0) {
                Button(onClick = {
                    Log.d("ItemList", "Add $quantity ${product.name} to cart")
                    itemViewModel.saveOrUpdateItem(product.code, quantity)
                    isExpended = false
                    quantity = 0
                }) {
                    Text(text = "Add")
                }
            }
        }
    }
}

@Composable
fun ItemDetail(
    item: Item,
) {
    Row {
        Text(text = "${item.id}: ${item.code} -> ${item.quantity}")
    }
}