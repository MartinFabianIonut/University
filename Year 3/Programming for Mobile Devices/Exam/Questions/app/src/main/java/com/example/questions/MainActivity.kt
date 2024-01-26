package com.example.questions

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
import com.example.questions.core.TAG
import com.example.questions.core.ui.MyNetworkStatus
import com.example.questions.core.utils.createNotificationChannel
import com.example.questions.ui.theme.QuestionStoreAndroidTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            createNotificationChannel(channelId = "Questions Channel",context = this@MainActivity)
            (application as QuestionStoreAndroid).container.questionRepository.setContext(this@MainActivity)
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
//                QuestionStoreAndroid {
//                    QuestionStoreAndroidNavHost()
//                }
//                MyNetworkStatus()
//                LightSensor()
//            }


            QuestionStoreAndroid {
                QuestionStoreAndroidNavHost()
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
            (application as QuestionStoreAndroid).container.questionRepository.openWsClient()
            (application as QuestionStoreAndroid).container.questionRepository.setContext(this@MainActivity)
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            (application as QuestionStoreAndroid).container.questionRepository.closeWsClient()
            (application as QuestionStoreAndroid).container.questionRepository.setContext(this@MainActivity)
        }
    }
}

@Composable
fun QuestionStoreAndroid(content: @Composable () -> Unit){
    Log.d("QuestionStoreAndroid", "recompose")
    QuestionStoreAndroidTheme {
        Surface {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewMyApp() {
    QuestionStoreAndroid {
        QuestionStoreAndroidNavHost()
    }
}
