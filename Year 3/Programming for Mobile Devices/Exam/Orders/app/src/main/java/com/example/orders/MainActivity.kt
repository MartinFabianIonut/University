package com.example.orders

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.orders.core.TAG
import com.example.orders.core.utils.createNotificationChannel
import com.example.orders.ui.theme.OrderStoreAndroidTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            createNotificationChannel(channelId = "Orders Channel",context = this@MainActivity)
            (application as OrderStoreAndroid).container.orderRepository.setContext(this@MainActivity)
            Log.d(TAG, "onCreate")

            OrderStoreAndroid {
                OrderStoreAndroidNavHost()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            (application as OrderStoreAndroid).container.orderRepository.openWsClient()
            (application as OrderStoreAndroid).container.orderRepository.setContext(this@MainActivity)
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            (application as OrderStoreAndroid).container.orderRepository.closeWsClient()
            (application as OrderStoreAndroid).container.orderRepository.setContext(this@MainActivity)
        }
    }
}

@Composable
fun OrderStoreAndroid(content: @Composable () -> Unit){
    Log.d("OrderStoreAndroid", "recompose")
    OrderStoreAndroidTheme {
        Surface {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewMyApp() {
    OrderStoreAndroid {
        OrderStoreAndroidNavHost()
    }
}
