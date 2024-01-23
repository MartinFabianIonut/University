package com.example.messages.todo.ui.message

import android.app.Application
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.messages.R
import com.example.messages.core.DateUtils
import com.example.messages.core.Result
import com.example.messages.core.ui.MyLocation
import com.example.messages.core.ui.MyLocationViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(messageId: Int?, onClose: () -> Unit) {
    Log.d("MessageScreen", "recompose")
    Log.d("MessageScreen", "messageId = $messageId")
    val messageViewModel = viewModel<MessageViewModel>(factory = MessageViewModel.Factory(messageId))
    val messageUiState = messageViewModel.uiState
    var text by rememberSaveable { mutableStateOf(messageUiState.message.text) }
    var read by rememberSaveable { mutableStateOf(messageUiState.message.read) }
    var sender by rememberSaveable { mutableStateOf(messageUiState.message.sender) }
    var created by rememberSaveable { mutableStateOf(messageUiState.message.created) }
    Log.d("MessageScreen", messageUiState.message.toString())

    LaunchedEffect(messageUiState.submitResult) {
        Log.d("MessageScreen", "Submit = ${messageUiState.submitResult}")
        if (messageUiState.submitResult is Result.Success) {
            Log.d("MessageScreen", "Closing screen")
            onClose()
        }
    }

    var textInitialized by remember { mutableStateOf(messageId == null) }
    LaunchedEffect(messageId, messageUiState.loadResult) {
        if (textInitialized) {
            return@LaunchedEffect
        }
        if(messageUiState.loadResult !is Result.Loading) {
            text = messageUiState.message.text
            read = messageUiState.message.read
            sender = messageUiState.message.sender
            created = messageUiState.message.created
            textInitialized = true
        }
    }

    var canSave by rememberSaveable { mutableStateOf(true) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.message)) },
                actions = {
                    Button(
                        enabled = canSave,
                        onClick = {
//                        if (text.length < 3) {
//                            errorMessage += "\n\tTitle must be at least 3 characters long!"
//                            canSave = false
//                        }

                        if (canSave) {
                            errorMessage = ""  // Reset error message if conditions are met
                            messageViewModel.saveOrUpdateMessage(
                                messageId ?: 0,
                                text,
                                read,
                                sender,
                                created
                            )
                        }
                    })
                    {
                        Row(
                            modifier = Modifier.padding(horizontal = 1.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                            AnimatedVisibility(visible = canSave) {
                                Text(
                                    text = "Save",
                                    modifier = Modifier
                                        .padding(start = 8.dp, top = 3.dp)
                                )
                            }
                        }
                    }

                }
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // UI for loading state
            if (messageUiState.loadResult is Result.Loading) {
                CircularProgressIndicator()
                return@Scaffold
            }
            if (messageUiState.submitResult is Result.Loading) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { LinearProgressIndicator() }
            }
            if (messageUiState.loadResult is Result.Error) {
                Text(text = "Failed to load message - ${(messageUiState.loadResult as Result.Error).exception?.message}")
            }

            // UI for loaded state
            Row {
                StyledTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = "text"
                )
            }
            Row {
                StyledTextField(
                    value = sender,
                    onValueChange = { sender = it },
                    label = "sender")
            }
            Row {
                StyledTextField(
                    value = created.toString(),
                    onValueChange = { created = it.toLong() },
                    label = "created")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        shape = RoundedCornerShape(50), brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.tertiary,
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    ) ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = read,
                    onCheckedChange = { isChecked -> read = isChecked },
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                )
                Text(text = if (read) "Read" else "Not read", color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.fillMaxWidth())
            }
            Row {
//                val myLocationViewModel = viewModel<MyLocationViewModel>(
//                    factory = MyLocationViewModel.Factory(
//                        LocalContext.current.applicationContext as Application
//                    )
//                )
//                val location = myLocationViewModel.uiState
//                if (lat != 0.0 && lng != 0.0) {
//                    MyLocation(lat, lng) { newLatLng ->
//                        lat = newLatLng.latitude
//                        lng = newLatLng.longitude
//                    }
//                } else if (location != null) {
//                    MyLocation(location.latitude, location.longitude) { newLatLng ->
//                        lat = newLatLng.latitude
//                        lng = newLatLng.longitude
//                    }
//                    lat = location.latitude
//                    lng = location.longitude
//                } else {
//                    LinearProgressIndicator()
//                }
            }
            if (messageUiState.submitResult is Result.Error) {
                Text(
                    text = "Failed to submit message - ${(messageUiState.submitResult as Result.Error).exception?.message}",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        LaunchedEffect(canSave) {
            if (!canSave) {
                delay(5000L)
                canSave = true
            }
            else{
                delay(1500L)
                errorMessage = ""
            }
        }
        CanAddAMessage(canSave, "You cannot save this because:$errorMessage")
    }
}

@Composable
fun CanAddAMessage(canSave: Boolean, message: String) {
    Log.d("CanAddAMessage", "canSave = $canSave")
    AnimatedVisibility(
        visible = !canSave,
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
                            MaterialTheme.colorScheme.tertiary,
                            MaterialTheme.colorScheme.primary,
                        )
                    )
                )

        ) {
            Text(
                text = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

}


@Preview
@Composable
fun PreviewMessageScreen() {
    MessageScreen(messageId = 0, onClose = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.tertiary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            ),
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        ),
        label = { Text(label, color = MaterialTheme.colorScheme.onSecondary) },
        placeholder = { Text(label, color = MaterialTheme.colorScheme.onSecondary) },
        colors = TextFieldDefaults.textFieldColors(
            disabledPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            errorPlaceholderColor = MaterialTheme.colorScheme.error,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent,
        )
    )
}