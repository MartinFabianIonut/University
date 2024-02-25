package com.example.items.todo.ui.items


import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.items.todo.data.Item
import kotlinx.coroutines.delay

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*

import androidx.compose.ui.draw.rotate

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi

typealias OnItemFn = (id: Int?) -> Unit

@Composable
fun ItemList(itemList: List<Item>, modifier: Modifier, itemsViewModel: ItemsViewModel, username : String) {
    Log.d("ItemList", "recompose")
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        items(itemList.size) { index ->
            var item = itemList[index]
            ItemDetail(item = item, itemsViewModel = itemsViewModel, username = username)
        }
    }

}

@Composable
fun ItemDetail(
    item: Item,
    itemsViewModel: ItemsViewModel,
    username: String
) {
    var expanded by remember { mutableStateOf(false) }

    var isSuccess by remember { mutableStateOf(true) }

    Row {
        Column {
            if (item.status == "taken") {
                ClickableText(
                    text = AnnotatedString(item.number + " " + item.status + " by " + item.takenBy),
                    onClick = {
                        expanded = !expanded
                    })
            } else {
                ClickableText(text = AnnotatedString(item.number + " " + item.status), onClick = {
                    expanded = !expanded
                })
            }
            if (expanded) {
                if (item.status == "free") {
                    if (isSuccess) {
                        Button(onClick = {
                            val newItem=Item(item.id, item.number, "taken", username)
                            itemsViewModel.updateItem(newItem){
                                isSuccess = it
                            }

                        }, modifier = Modifier.padding(start = 20.dp)) {
                            Text(text = "Take")
                        }
                    } else {
                        Button(onClick = {
                            val newItem=Item(item.id, item.number, "taken", username)
                            itemsViewModel.updateItem(newItem){
                                isSuccess = it
                            }
                        }) {
                            Text(text = "Retry")
                        }
                    }

                } else if (item.status == "taken" && item.takenBy == username) {
                    if (isSuccess) {
                        Button(onClick = {
                            val newItem=Item(item.id, item.number, "free", "")
                            itemsViewModel.updateItem(newItem){
                                isSuccess = it
                            }
                        }, modifier = Modifier.padding(start = 20.dp)) {
                            Text(text = "Release")
                        }
                    } else {
                        Button(onClick = {
                            val newItem=Item(item.id, item.number, "free", "")
                            itemsViewModel.updateItem(newItem){
                                isSuccess = it
                            }
                        }) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
        }
    }
}

