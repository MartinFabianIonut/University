package com.example.bookstoreandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.bookstoreandroid.core.TAG
import com.example.bookstoreandroid.ui.theme.BookStoreAndroidTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.d(TAG, "onCreate")
            BookStoreAndroid {
                BookStoreAndroidNavHost()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            (application as BookStoreAndroid).container.bookRepository.openWsClient()
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            (application as BookStoreAndroid).container.bookRepository.closeWsClient()
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
