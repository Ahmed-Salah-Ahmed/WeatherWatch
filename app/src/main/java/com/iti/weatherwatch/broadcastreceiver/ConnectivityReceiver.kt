package com.iti.weatherwatch.broadcastreceiver

import android.content.*
import android.net.ConnectivityManager

/*
This class is a BroadcastReceiver that detects changes in network connectivity. It has the following components:

onReceive() method: This method is called when the BroadcastReceiver receives an Intent broadcast. It checks if a listener is registered and if so, it calls the onNetworkConnectionChanged() method of that listener with a boolean value indicating whether the network is connected or not.

isConnectedOrConnecting() method: This method checks if the device is currently connected to a network or is in the process of connecting.

ConnectivityReceiverListener interface: This interface defines the onNetworkConnectionChanged() method that is called when the network connectivity changes.

companion object: This object contains a connectivityReceiverListener variable, which is used to store an instance of a class that implements the ConnectivityReceiverListener interface. It allows other classes to register as listeners to receive network connectivity change events.
 */
class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(
                isConnectedOrConnecting(
                    context
                )
            )
        }
    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}
