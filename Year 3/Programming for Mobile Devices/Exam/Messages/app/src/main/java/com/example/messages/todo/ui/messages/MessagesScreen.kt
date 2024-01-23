package com.example.messages.todo.ui.messages

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
import com.example.messages.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(onMessageClick: (id: Int?) -> Unit, onAddMessage: () -> Unit, onLogout: () -> Unit) {
    Log.d("MessagesScreen", "recompose")
    val messagesViewModel = viewModel<MessagesViewModel>(factory = MessagesViewModel.Factory)
    val messagesUiState by messagesViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.messages)) },
                actions = {
                    Button(onClick = onLogout) { Text("Logout") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d("MessagesScreen", "Add a new message")
                    onAddMessage()
                }
            ) { Icon(Icons.Rounded.Add, "Add") }
        }
    ) {
        MessageList(
            messageList = messagesUiState,
            onMessageClick = onMessageClick,
            modifier = Modifier.padding(it)
        )
    }
}

@Preview
@Composable
fun PreviewMessagesScreen() {
    MessagesScreen(onMessageClick = {}, onAddMessage = {}, onLogout = {})
}
