package com.example.codechallengeex.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData

/**
 * Created by Satya Seshu on 10/04/22.
 */
class ConnectionLiveData(private val context: Context) : LiveData<ConnectionState>() {

    override fun onActive() {
        super.onActive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

            val isConnected: Boolean = activeNetwork != null && activeNetwork.isConnected

            if (isConnected) {
                postValue(
                    ConnectionState(
                        activeNetwork?.type ?: 0,
                        true
                    )
                )
            } else {
                postValue(
                    ConnectionState(
                        0,
                        false
                    )
                )
            }
        }
    }
}

data class ConnectionState(var type: Int, var isConnected: Boolean)