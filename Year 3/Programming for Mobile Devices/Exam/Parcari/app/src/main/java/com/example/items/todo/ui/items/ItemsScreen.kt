package com.example.items.todo.ui.items

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.items.R
import com.example.items.core.TAG
import com.example.items.core.data.UserPreferences
import com.example.items.core.utils.ConnectivityManagerNetworkMonitor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(onItemClick: (id: Int?) -> Unit, onLogout: () -> Unit, userPreferencesUiState: UserPreferences) {
    Log.d("ItemsScreen", "recompose")
    val itemsViewModel = viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory)
    val itemsUiState by itemsViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )

    val context = LocalContext.current
    val connectivityManager = remember {
        ConnectivityManagerNetworkMonitor(context)
    }


    var isError by rememberSaveable { mutableStateOf(false) }



    itemsViewModel.loadItems{isSuccess ->
        isError = !isSuccess
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.items)) },
                actions = {
                    Button(onClick = onLogout) { Text("Logout") }
                }
            )

        },

    ) {

        if (isError){
            Row{
                Column {
                    Log.d(TAG, "isError: $isError")
                    if (isError) {
                        Button(onClick = { itemsViewModel.loadItems{
                                isSuccess -> isError = !isSuccess
                        } }, modifier = Modifier.padding(70.dp)) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier.padding(100.dp)) {
            Column {
                ItemList(
                    itemList = itemsUiState,
                    modifier = Modifier.padding(it),
                    itemsViewModel = itemsViewModel,
                    username = userPreferencesUiState.username
                )
            }
        }
    }
}

