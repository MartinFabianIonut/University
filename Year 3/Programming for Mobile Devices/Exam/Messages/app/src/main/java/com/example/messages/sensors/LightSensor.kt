package com.example.messages.sensors

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.messages.core.TAG
import kotlinx.coroutines.launch

class LightSensorViewModel(application: Application) : AndroidViewModel(application) {
    var uiState by mutableFloatStateOf(0f)
        private set

    init {
        viewModelScope.launch {
            LightSensorMonitor(getApplication()).lightLevel.collect {
                uiState = it
                updateBrightness(it)
            }
        }
    }

    private fun updateBrightness(lightLevel: Float) {
        Log.d(TAG, "updateBrightness $lightLevel")
        val maxBrightness = 255
        val minLightLevel = 0f
        val maxLightLevel = 40000f

        val normalizedLightLevel = lightLevel.coerceIn(minLightLevel, maxLightLevel)

        val screenBrightness: Float = when {
            normalizedLightLevel <= 0.1 * maxLightLevel -> 0.02f
            normalizedLightLevel <= 0.2 * maxLightLevel -> 0.05f
            normalizedLightLevel <= 0.3 * maxLightLevel -> 0.1f
            normalizedLightLevel <= 0.4 * maxLightLevel -> 0.15f
            normalizedLightLevel <= 0.5 * maxLightLevel -> 0.2f
            normalizedLightLevel <= 0.6 * maxLightLevel -> 0.3f
            normalizedLightLevel <= 0.7 * maxLightLevel -> 0.4f
            normalizedLightLevel <= 0.8 * maxLightLevel -> 0.5f
            normalizedLightLevel <= 0.9 * maxLightLevel -> 0.8f
            else -> 1.0f
        }

        val brightnessValue = (screenBrightness * maxBrightness).toInt()
        Log.d(TAG, "brightnessValue $brightnessValue")
        updateBrightnessSetting(brightnessValue)
    }

    private fun updateBrightnessSetting(brightness: Int) {
        try {
            val resolver: ContentResolver = getApplication<Application>().contentResolver
            Log.d(TAG, "try to change brightness to $brightness")
            while (!hasWriteSettingsPermission(getApplication())) {
                Thread.sleep(1000)
                Log.d(TAG, "waiting for permission")
            }
            Settings.System.putInt(
                resolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
            Log.d(TAG, "updateBrightnessSetting $brightness")
            Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
            Log.d(TAG, "SUCCESS")
        } catch (e: Exception) {
            Log.e(TAG, "updateBrightnessSetting $brightness")
            e.printStackTrace()
        }
    }

    private fun hasWriteSettingsPermission(context: Context): Boolean {
        return Settings.System.canWrite(context)
    }

    companion object {
        fun Factory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LightSensorViewModel(application)
            }
        }
    }
}

@Composable
fun LightSensor() {
    viewModel<LightSensorViewModel>(
        factory = LightSensorViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )
}