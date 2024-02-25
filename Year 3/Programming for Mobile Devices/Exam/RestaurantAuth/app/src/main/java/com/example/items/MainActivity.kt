package com.example.items

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.items.core.TAG
import com.example.items.core.ui.MyNetworkStatus
import com.example.items.core.utils.createNotificationChannel
import com.example.items.ui.theme.ItemStoreAndroidTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            createNotificationChannel(channelId = "Items Channel",context = this@MainActivity)
            (application as ItemStoreAndroid).container.itemRepository.setContext(this@MainActivity)
            Log.d(TAG, "onCreate")
//            lock.lock()
//            askPermissions(this)
//            lock.unlock()
//            Permissions(
//                permissions = listOf(
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ),
//                rationaleText = "Please allow app to use location (coarse or fine)",
//                dismissedText = "O noes! No location provider allowed!"
//            ) {
//                ItemStoreAndroid {
//                    ItemStoreAndroidNavHost()
//                }
//                MyNetworkStatus()
//                LightSensor()
//            }


            ItemStoreAndroid {
                ItemStoreAndroidNavHost()
            }
            MyNetworkStatus()
        }
    }

    private fun askPermissions(context: Context) {
        if (!Settings.System.canWrite(context)) {
            val i = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            context.startActivity(i);
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
