package com.diyantech.easysplit.utils

import android.content.Context
import android.net.ConnectivityManager
import com.diyantech.easysplit.MyApp

class Utils {

    companion object {
        public fun  checkNetwork(context: Context) : Boolean {
            var connected = false
            val connectivityManager = MyApp.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            connected = networkInfo != null && networkInfo.isConnected
            return connected
        }
    }


}