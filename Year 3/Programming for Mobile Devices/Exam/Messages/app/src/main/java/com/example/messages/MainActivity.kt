package com.example.messages

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.messages.core.TAG
import com.example.messages.core.ui.MyNetworkStatus
import com.example.messages.core.utils.createNotificationChannel
import com.example.messages.ui.theme.MessageStoreAndroidTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            createNotificationChannel(channelId = "Messages Channel",context = this@MainActivity)
            (application as MessageStoreAndroid).container.messageRepository.setContext(this@MainActivity)
            Log.d(TAG, "onCreate")
            MessageStoreAndroid {
                MessageStoreAndroidNavHost()
            }
            MyNetworkStatus()
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            (application as MessageStoreAndroid).container.messageRepository.openWsClient()
            (application as MessageStoreAndroid).container.messageRepository.setContext(this@MainActivity)
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            (application as MessageStoreAndroid).container.messageRepository.closeWsClient()
            (application as MessageStoreAndroid).container.messageRepository.setContext(this@MainActivity)
        }
    }
}

@Composable
fun MessageStoreAndroid(content: @Composable () -> Unit){
    Log.d("MessageStoreAndroid", "recompose")
    MessageStoreAndroidTheme {
        Surface {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewMyApp() {
    MessageStoreAndroid {
        MessageStoreAndroidNavHost()
    }
}
