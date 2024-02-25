package com.example.chat.todo.ui.items


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chat.todo.data.Item


typealias OnItemFn = (id: Int?) -> Unit

@Composable
fun ItemList(itemList: List<Item>, onItemClick: OnItemFn, modifier: Modifier) {
    Log.d("ItemList", "recompose")
    val roomList =
        remember(itemList) { itemList.sortedByDescending { it.created }.map { it.room }.distinct() }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(roomList) { room ->
            ItemDetail(roomId = room,itemList)
        }

    }

}

@Composable
fun MessageDetail(
    item: Item
) {
    val itemsViewModel = viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory)
    var isLoading by remember { mutableStateOf(false) }

    Row {
        Text(text = item.username + " : " + item.text, modifier = Modifier.padding(8.dp))
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(4.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        if (item.dirty) {
            ClickableText(text = AnnotatedString("NOT SENT"), onClick = {
                isLoading = true
                itemsViewModel.sendMessage(item.text, item.room)
                {
                    isLoading = false
                    if (it) {
                        itemsViewModel.markMessageClean(item.text, item.room)
                    }
                }
            })

        }

    }
}


@Composable
fun ItemDetail(
    roomId: String,
    itemList: List<Item>
) {
    val isChatShown = remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    val itemsViewModel = viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory)

    Row {
        ClickableText(
            text = AnnotatedString(roomId),
            onClick = {
                isChatShown.value = !isChatShown.value
            },
            modifier = Modifier.padding(8.dp)
        )
        if (isChatShown.value) {
            Column {
                itemList.filter {it.room==roomId}.sortedByDescending { it.created }.forEach { item ->
                    MessageDetail(item = item)
                }
                Row{
                    TextField(value = text, onValueChange = {
                        text = it
                    }, modifier = Modifier.padding(8.dp))
                    Button(onClick = {
                        Log.d("ItemDetail", "Send message")
                        itemsViewModel.sendMessage(text,roomId)
                        {
                            Log.d("ItemDetail", "Send message result it $it")
                            if (!it){
                                itemsViewModel.markMessageDirty(text,roomId)
                            }
                        }

                        text = ""

                    }, modifier = Modifier.padding(8.dp)) {
                        Text(text = "Send")
                    }
                }

            }
            
        }
    }
}
