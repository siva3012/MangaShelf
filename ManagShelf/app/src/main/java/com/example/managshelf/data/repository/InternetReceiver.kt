package com.example.managshelf.data.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkReceiver(private val networkDataSource:  NetworkDataSource) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent?.action) {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
           networkDataSource.internetState(isConnected)
        }
    }
}
