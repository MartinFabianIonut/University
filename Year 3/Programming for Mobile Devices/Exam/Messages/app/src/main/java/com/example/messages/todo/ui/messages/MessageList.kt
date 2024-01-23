package com.example.messages.todo.ui.messages


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.messages.core.DateUtils
import com.example.messages.todo.data.Message
import com.example.messages.todo.ui.message.MessageViewModel
import kotlinx.coroutines.delay

typealias OnMessageFn = (id: Int?) -> Unit

@Composable
fun MessageList(messageList: List<Message>, onMessageClick: OnMessageFn, modifier: Modifier) {
    Log.d("MessageList", "recompose")

    var selectedUser by remember { mutableStateOf<String?>(null) }


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        LazyColumn {

            val groupedMessages = messageList.groupBy { it.sender }.entries.sortedByDescending {
                it.value.filter { !it.read }.maxByOrNull { it.created }?.created
            }
            // Iterate through sorted senders
            groupedMessages.forEach { (sender, messages) ->
                // Show sender with unread count
                val unreadCount = messages.count { !it.read }
                item {
                    SenderHeader(sender, unreadCount) {
                        // Callback when a user is clicked
                        // Toggle the selected user
                        selectedUser = if (selectedUser == sender) null else sender
                    }
                }

                // Show messages for the sender if selected
                if (selectedUser == sender) {
                    items(messages.sortedBy { it.created }) { message ->
                        Log.d("MessageList", message.toString())
                        MessageDetail(message.id, message, onMessageClick)
                    }
                }
            }
        }
    }
}


@Composable
fun SenderHeader(sender: String, unreadCount: Int, onUserClick: () -> Unit) {
    val textColor = MaterialTheme.colorScheme.onPrimary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onUserClick)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "User $sender [$unreadCount]",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MessageDetail(id: Int, message: Message, onMessageClick: OnMessageFn) {
    var isExpanded by remember { mutableStateOf(false) }
    var isBold by remember { mutableStateOf(!message.read) }
    Log.d("MessageDetail", "recompose for $message")
    val messageViewModel = viewModel<MessageViewModel>(factory = MessageViewModel.Factory(id))
    val gradientColors = listOf(
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary
    )
    val textColor = MaterialTheme.colorScheme.onPrimary

    Box(
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
                isExpanded = !isExpanded
            })
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Text: ${message.text}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                        color = textColor
                    ),
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clickable {
                            onMessageClick(message.id)
                        }
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Expand/Collapse",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Text(
                text = "Sender: ${message.sender}",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = "Created: ${message.created}",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = "Read: ${message.read}",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                modifier = Modifier.padding(bottom = 6.dp)
            )
            LaunchedEffect(Unit) {
                delay(1000)
                if (!message.read) {
                    messageViewModel.saveOrUpdateMessage(
                        message.id,
                        message.text,
                        true,
                        message.sender,
                        message.created
                    )
                    isBold = false
                }
            }
        }
    }
}
