package com.example.messages.core.ui

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.messages.core.utils.LocationMonitor
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

class MyLocationViewModel(application: Application) : AndroidViewModel(application) {
    var uiState by mutableStateOf<Location?>(null)
        private set

    init {
        collectLocation()
    }

    private fun collectLocation() {
        viewModelScope.launch {
            LocationMonitor(getApplication()).currentLocation.collect {
                Log.d("MyLocationViewModel", "collect $it")
                uiState = it;
            }
        }
    }

    companion object {
        fun Factory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MyLocationViewModel(application)
            }
        }
    }
}

@Composable
fun MyLocation(lat: Double? = null, lng: Double? = null,  onLocationUpdated: (LatLng) -> Unit) {
    val myLocationViewModel = viewModel<MyLocationViewModel>(
        factory = MyLocationViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )
    val location = myLocationViewModel.uiState

    if (lat == null || lng == null) {
        if (location != null) {
            MyMap(location.latitude, location.longitude){ newLatLng ->
                onLocationUpdated(newLatLng)
            }
        } else {
            LinearProgressIndicator()
        }
    } else {
        MyMap(lat, lng){ newLatLng ->
            onLocationUpdated(newLatLng)
        }
    }
}

const val TAG = "MyMap"
@Composable
fun MyMap(lat: Double, long: Double, onMapLongClick: (LatLng) -> Unit) {
    val markerState = rememberMarkerState(position = LatLng(lat, long))
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerState.position, 10f)
    }
    val mapId = "mapId"
    MapFragment.newInstance(
        GoogleMapOptions()
            .mapId(mapId)
    )
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = {
            Log.d(TAG, "onMapClick $it")
        },
        onMapLongClick = {latLng ->
            markerState.position = latLng
            Log.d(TAG, "onMapLongClick $latLng")
            onMapLongClick(latLng)
        }
    ) {
        Marker(
            state = MarkerState(position = markerState.position),
            title = "User location title",
            snippet = "User location",
        )
    }
}
