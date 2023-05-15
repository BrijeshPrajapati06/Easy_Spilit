package com.diyantech.easysplit

import android.app.Application
import com.diyantech.easysplit.utils.PrefHelper

//private const val URL = "http://122.170.0.3:3017"
class MyApp: Application() {
//    private var mSocket: Socket? = null



    override fun onCreate() {
        super.onCreate()
        instance = this


    }


    //return socket instance
//    fun getMSocket(): Socket? {
//        return mSocket
//    }
    companion object {
      lateinit var instance :MyApp
            private set
    }


}

