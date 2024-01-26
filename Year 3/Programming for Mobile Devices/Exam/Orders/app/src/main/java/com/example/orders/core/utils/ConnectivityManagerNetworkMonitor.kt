package com.example.orders.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest.Builder
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

class ConnectivityManagerNetworkMonitor(val context: Context) {
    private var lastNetworkStatus: Boolean = false
    fun getLastNetworkStatus(): Boolean {
        return lastNetworkStatus
    }
    private fun updateLastNetworkStatus(isOnline: Boolean) {
        lastNetworkStatus = isOnline
    }

    val isOnline: Flow<Boolean> = callbackFlow<Boolean> {
        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                updateLastNetworkStatus(false)
                channel.trySend(true)

            }

            override fun onLost(network: Network) {
                updateLastNetworkStatus(true)
                channel.trySend(false)

            }
        }

        val connectivityManager = context.getSystemService<ConnectivityManager>()

        connectivityManager?.registerNetworkCallback(
            Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            callback
        )

        channel.trySend(connectivityManager.isCurrentlyConnected())

        awaitClose {
            connectivityManager?.unregisterNetworkCallback(callback)
        }
    }
        .conflate()

    private fun ConnectivityManager?.isCurrentlyConnected() = when (this) {
        null -> false
        else -> activeNetwork
            ?.let(::getNetworkCapabilities)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }
}
