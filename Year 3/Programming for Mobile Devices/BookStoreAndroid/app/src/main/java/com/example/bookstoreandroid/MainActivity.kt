package com.example.bookstoreandroid

import android.Manifest
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
import com.example.bookstoreandroid.core.TAG
import com.example.bookstoreandroid.core.ui.MyNetworkStatus
import com.example.bookstoreandroid.core.utils.Permissions
import com.example.bookstoreandroid.core.utils.createNotificationChannel
import com.example.bookstoreandroid.sensors.LightSensor
import com.example.bookstoreandroid.ui.theme.BookStoreAndroidTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import okio.AsyncTimeout.Companion.lock

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            createNotificationChannel(channelId = "Books Channel",context = this@MainActivity)
            (application as BookStoreAndroid).container.bookRepository.setContext(this@MainActivity)
            Log.d(TAG, "onCreate")
            lock.lock()
            askPermissions(this)
            lock.unlock()
            Permissions(
                permissions = listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                rationaleText = "Please allow app to use location (coarse or fine)",
                dismissedText = "O noes! No location provider allowed!"
            ) {
                BookStoreAndroid {
                    BookStoreAndroidNavHost()
                }
                MyNetworkStatus()
                LightSensor()
            }
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
            (application as BookStoreAndroid).container.bookRepository.openWsClient()
            (application as BookStoreAndroid).container.bookRepository.setContext(this@MainActivity)
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            (application as BookStoreAndroid).container.bookRepository.closeWsClient()
            (application as BookStoreAndroid).container.bookRepository.setContext(this@MainActivity)
        }
    }
}

@Composable
fun BookStoreAndroid(content: @Composable () -> Unit){
    Log.d("BookStoreAndroid", "recompose")
    BookStoreAndroidTheme {
        Surface {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewMyApp() {
    BookStoreAndroid {
        BookStoreAndroidNavHost()
    }
}
