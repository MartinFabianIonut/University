package com.example.items.todo.ui.item

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.items.R
import com.example.items.core.Result
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(itemId: Int?, onClose: () -> Unit) {
//    Log.d("ItemScreen", "recompose")
//    Log.d("ItemScreen", "itemId = $itemId")
//    val itemViewModel = viewModel<ItemViewModel>(factory = ItemViewModel.Factory(itemId))
//    val itemUiState = itemViewModel.uiState
//    var text by rememberSaveable { mutableStateOf(itemUiState.item.text) }
////    var read by rememberSaveable { mutableStateOf(itemUiState.item.read) }
////    var sender by rememberSaveable { mutableStateOf(itemUiState.item.sender) }
////    var created by rememberSaveable { mutableStateOf(itemUiState.item.created) }
//    Log.d("ItemScreen", itemUiState.item.toString())
//
//    LaunchedEffect(itemUiState.submitResult) {
//        Log.d("ItemScreen", "Submit = ${itemUiState.submitResult}")
//        if (itemUiState.submitResult is Result.Success) {
//            Log.d("ItemScreen", "Closing screen")
//            onClose()
//        }
//    }
//
//    var textInitialized by remember { mutableStateOf(itemId == null) }
//    LaunchedEffect(itemId, itemUiState.loadResult) {
//        if (textInitialized) {
//            return@LaunchedEffect
//        }
//        if(itemUiState.loadResult !is Result.Loading) {
//            text = itemUiState.item.text
////            read = itemUiState.item.read
////            sender = itemUiState.item.sender
////            created = itemUiState.item.created
//            textInitialized = true
//        }
//    }
//
//    var canSave by rememberSaveable { mutableStateOf(true) }
//    var errorItem by rememberSaveable { mutableStateOf("") }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = stringResource(id = R.string.item)) },
//                actions = {
//                    Button(
//                        enabled = canSave,
//                        onClick = {
////                        if (text.length < 3) {
////                            errorItem += "\n\tTitle must be at least 3 characters long!"
////                            canSave = false
////                        }
//
////                        if (canSave) {
////                            errorItem = ""  // Reset error item if conditions are met
////                            itemViewModel.saveOrUpdateItem(
////                                itemId ?: 0,
////                                text,
////                                read,
////                                sender,
////                                created
////                            )
////                        }
//                    })
//                    {
//                        Row(
//                            modifier = Modifier.padding(horizontal = 1.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Edit,
//                                contentDescription = null
//                            )
//                            AnimatedVisibility(visible = canSave) {
//                                Text(
//                                    text = "Save",
//                                    modifier = Modifier
//                                        .padding(start = 8.dp, top = 3.dp)
//                                )
//                            }
//                        }
//                    }
//
//                }
//            )
//        }
//    ) { it ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(it)
//        ) {
//            // UI for loading state
//            if (itemUiState.loadResult is Result.Loading) {
//                CircularProgressIndicator()
//                return@Scaffold
//            }
//            if (itemUiState.submitResult is Result.Loading) {
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) { LinearProgressIndicator() }
//            }
//            if (itemUiState.loadResult is Result.Error) {
//                Text(text = "Failed to load item - ${(itemUiState.loadResult as Result.Error).exception?.message}")
//            }
//
//            // UI for loaded state
//            Row {
//                StyledTextField(
//                    value = text,
//                    onValueChange = { text = it },
//                    label = "text"
//                )
//            }
////            Row {
////                StyledTextField(
////                    value = sender,
////                    onValueChange = { sender = it },
////                    label = "sender")
////            }
////            Row {
////                StyledTextField(
////                    value = created.toString(),
////                    onValueChange = { created = it.toLong() },
////                    label = "created")
////            }
////            Row(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(8.dp)
////                    .background(
////                        shape = RoundedCornerShape(50), brush = Brush.linearGradient(
////                            listOf(
////                                MaterialTheme.colorScheme.tertiary,
////                                MaterialTheme.colorScheme.primary,
////                                MaterialTheme.colorScheme.secondary
////                            )
////                        )
////                    ) ,
////                verticalAlignment = Alignment.CenterVertically
////            ) {
////                Checkbox(
////                    checked = read,
////                    onCheckedChange = { isChecked -> read = isChecked },
////                    modifier = Modifier
////                        .padding(8.dp)
////                        .background(
////                            shape = RoundedCornerShape(50),
////                            color = MaterialTheme.colorScheme.onPrimary
////                        )
////
////                )
////                Text(text = if (read) "Read" else "Not read", color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.fillMaxWidth())
////            }
//            Row {
////                val myLocationViewModel = viewModel<MyLocationViewModel>(
////                    factory = MyLocationViewModel.Factory(
////                        LocalContext.current.applicationContext as Application
////                    )
////                )
////                val location = myLocationViewModel.uiState
////                if (lat != 0.0 && lng != 0.0) {
////                    MyLocation(lat, lng) { newLatLng ->
////                        lat = newLatLng.latitude
////                        lng = newLatLng.longitude
////                    }
////                } else if (location != null) {
////                    MyLocation(location.latitude, location.longitude) { newLatLng ->
////                        lat = newLatLng.latitude
////                        lng = newLatLng.longitude
////                    }
////                    lat = location.latitude
////                    lng = location.longitude
////                } else {
////                    LinearProgressIndicator()
////                }
//            }
//            if (itemUiState.submitResult is Result.Error) {
//                Text(
//                    text = "Failed to submit item - ${(itemUiState.submitResult as Result.Error).exception?.message}",
//                    modifier = Modifier.fillMaxWidth(),
//                )
//            }
//        }
//        LaunchedEffect(canSave) {
//            if (!canSave) {
//                delay(5000L)
//                canSave = true
//            }
//            else{
//                delay(1500L)
//                errorItem = ""
//            }
//        }
//        CanAddAItem(canSave, "You cannot save this because:$errorItem")
//    }
}

@Composable
fun CanAddAItem(canSave: Boolean, item: String) {
    Log.d("CanAddAItem", "canSave = $canSave")
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
                text = item,
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
fun PreviewItemScreen() {
    ItemScreen(itemId = 0, onClose = {})
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