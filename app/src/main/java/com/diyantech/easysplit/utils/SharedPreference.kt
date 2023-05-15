package com.diyantech.easysplit.utils

import android.content.Context
import android.content.SharedPreferences
import de.hdodenhof.circleimageview.CircleImageView

class SharedPreference(context : Context) : SharedPreferences {

    private val sharedPreference = context.getSharedPreferences("myPreference",0)

    //Save Boolean Values into the shared preference
    fun putBoolean(key:String,value:Boolean){
        sharedPreference.edit().putBoolean(key, value).apply()
    }

    // Retrieve boolean values from the shared preference
    fun getBoolean(key: String): Boolean {
        return sharedPreference.getBoolean(key,false)
    }

    // For String values
    fun putString(key:String,value:String){
        sharedPreference.edit().putString(key, value).apply()
    }

    fun getString(key:String) : String?{
        return sharedPreference.getString(key,null)
    }

    //For Integer values
    fun putInt(key: String, value: Int){
        sharedPreference.edit().putInt(key, value).apply()
    }

    fun getInt(key: String):Int {
        return sharedPreference.getInt(key,0)
    }

    //Clear Shared Preference
    fun clear(){
        sharedPreference.edit().clear().apply()
    }

    override fun getAll(): MutableMap<String, *> {
        TODO("Not yet implemented")
    }

    override fun getString(key: String?, defValue: String?): String? {
        TODO("Not yet implemented")
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? {
        TODO("Not yet implemented")
    }

    override fun getInt(key: String?, defValue: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getLong(key: String?, defValue: Long): Long {
        TODO("Not yet implemented")
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        TODO("Not yet implemented")
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(key: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun edit(): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

}