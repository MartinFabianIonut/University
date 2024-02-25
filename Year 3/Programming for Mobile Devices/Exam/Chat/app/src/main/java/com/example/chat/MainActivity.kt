package com.example.chat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.chat.core.TAG
import com.example.chat.core.utils.createNotificationChannel
import com.example.chat.ui.theme.ItemStoreAndroidTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            createNotificationChannel(channelId = "Items Channel",context = this@MainActivity)
            (application as ItemStoreAndroid).container.itemRepository.setContext(this@MainActivity)
            Log.d(TAG, "onCreate")

            ItemStoreAndroid {
                ItemStoreAndroidNavHost()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            (application as ItemStoreAndroid).container.itemRepository.openWsClient()
            (application as ItemStoreAndroid).container.itemRepository.setContext(this@MainActivity)
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            (application as ItemStoreAndroid).container.itemRepository.closeWsClient()
            (application as ItemStoreAndroid).container.itemRepository.setContext(this@MainActivity)
        }
    }
}

@Composable
fun ItemStoreAndroid(content: @Composable () -> Unit){
    Log.d("ItemStoreAndroid", "recompose")
    ItemStoreAndroidTheme {
        Surface {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewMyApp() {
    ItemStoreAndroid {
        ItemStoreAndroidNavHost()
    }
}
