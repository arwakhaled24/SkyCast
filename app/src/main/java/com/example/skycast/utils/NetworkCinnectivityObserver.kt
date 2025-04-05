
package com.example.skycast.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NetworkConnectivityObserver(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _networkStatus = MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val networkStatus = _networkStatus.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                scope.launch { _networkStatus.emit(true) }
            }

            override fun onLost(network: Network) {
                scope.launch { _networkStatus.emit(false) }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
    }
}
