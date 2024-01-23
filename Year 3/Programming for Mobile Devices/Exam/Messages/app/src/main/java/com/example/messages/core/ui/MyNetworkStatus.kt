package com.example.messages.core.ui

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.messages.core.utils.ConnectivityManagerNetworkMonitor
import com.example.messages.core.utils.MyWorker
import kotlinx.coroutines.launch
import java.util.UUID

class MyNetworkStatusViewModel(application: Application) : AndroidViewModel(application) {
    private var uiState by mutableStateOf(false)
    private var workManager: WorkManager = WorkManager.getInstance(getApplication())
    private var workId: UUID? = null

    var circleColor by mutableStateOf(Color(0xEBC25858))

    private val networkMonitor = ConnectivityManagerNetworkMonitor(application)

    private fun handleNetworkStatusChange(isOnline: Boolean) {
        Log.d("MyNetworkStatusViewModel handleNetworkStatusChange", "isOnline: $isOnline, wasOnline: ${networkMonitor.getLastNetworkStatus()}")
        val wasOnline = networkMonitor.getLastNetworkStatus()
        if (!wasOnline && isOnline) {
            performOneTimeWork()
        }
    }

    private fun collectNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                uiState = isOnline
                circleColor = if (isOnline) Color(0xE184C284) else Color(0xEBC25858)
                Log.d("MyNetworkStatusViewModel", "Network status - is Online: $isOnline")
                handleNetworkStatusChange(isOnline)
            }
        }
    }

    init {
        collectNetworkStatus()
    }

    private fun performOneTimeWork() {
        viewModelScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
                .build()

            // Example: Trigger a one-time worker

            val myWork = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .setConstraints(constraints)
                .build()

            workId = myWork.id

            workManager.apply {
                // enqueue Work
                enqueue(myWork)

                // observe work status
                getWorkInfoByIdLiveData(workId!!).asFlow().collect {
                    Log.d("MyWorker is working", "$it")
                    if (it.state.isFinished) {
                        Log.d("MyWorker", "Worker is finished")
                    }
                    if (it.state == WorkInfo.State.FAILED) {
                        Log.d("MyWorker", "Worker failed")
                    }
                }
            }
        }
    }

    companion object {
        fun Factory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MyNetworkStatusViewModel(application)
            }
        }
    }
}

@Composable
fun MyNetworkStatus() {
    val myNetworkStatusViewModel = viewModel<MyNetworkStatusViewModel>(
        factory = MyNetworkStatusViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )

    Box(
        modifier = Modifier
            .size(16.dp)
            .background(myNetworkStatusViewModel.circleColor, CircleShape)
            .graphicsLayer(shape = CircleShape)
            .padding(15.dp)
    )
}
