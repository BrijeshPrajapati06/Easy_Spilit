package com.diyantech.easysplit.utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import com.diyantech.easysplit.MyApp
import com.diyantech.easysplit.modelclass.response.LoginResponse
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class PrefHelper() {
    /**
     * This method get integer value in preferences
     *
     * @param key          (String)       : key for unique string value
     * @param defaultValue (int) : defaultValue to be passed
     * @return(int) : it return  key value that user passed as a key
     * e.g. customerId - return value - 23
     */
    fun getInt(key: String?, defaultValue: Int): Int {
        return preferences!!.getInt(key, defaultValue)
    }

    /**
     * This method store integer value in preferences
     *
     * @param key   (String) : key for unique string value
     * @param value (int)  : value to be stored
     */
    fun setInt(key: String?, value: Int) {
        editor!!.putInt(key, value)
        apply(editor)
    }

    /**
     * This method get boolean value from preferences
     *
     * @param key          (String)           : key for unique string value
     * @param defaultValue (long) : defaultValue to be passed
     * @return (long) : it return  key value that user passed as a key
     */
    fun getLong(key: String?, defaultValue: Long): Long {
        return preferences!!.getLong(key, defaultValue)
    }

    /**
     * This method store the long dataType value in preferences
     *
     * @param key   (String) : key for unique string value
     * @param value (long) : defaultValue to be passed
     */
    fun setLong(key: String?, value: Long) {
        editor!!.putLong(key, value)
        apply(editor)
    }

    /**
     * This method get the double dataType value from preferences
     *
     * @param key   (String)   : key for unique string value
     * @param value (double) : defaultValue to be passed]
     * @return (int) : it return  key value that user passed as a key
     * e.g. customerId - return value - 2365786354358
     */
    fun getDouble(key: String?, value: Double): Long {
        return preferences!!.getLong(key, java.lang.Double.doubleToRawLongBits(value))
    }

    /**
     * This method store the double dataType value in preferences
     *
     * @param key(String)   : key for unique string value
     * @param value(double) : defaultValue to be passed
     */
    fun setDouble(key: String?, value: Double) {
        editor!!.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        apply(editor)
    }

    /**
     * This method return the string value from preferences
     *
     * @param key          (String)          : key for unique string value
     * @param defaultValue (String) : defaultValue to be passed
     * @return (String) : it return  key value that user passed as a key
     * e.g. lblAddress - return value - Address
     */
    fun getString(key: String?, defaultValue: String?): String? {
        return preferences!!.getString(key, defaultValue)
    }

    /**
     * This method store String value in preferences
     *
     * @param key   (String)   : key for unique string value
     * @param value (String) : value to be stored
     */
    fun setString(key: String?, value: String?) {
        editor!!.putString(key, value)
        apply(editor)
    }

    /**
     * This method get boolean value from preferences
     *
     * @param key          (String)           : key for unique string value
     * @param defaultValue (boolean) : defaultValue to be passed
     * @return (int) : it return  key value that user passed as a key
     * e.g. isActive - return value - true or false
     */
    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return preferences!!.getBoolean(key, defaultValue)
    }

    fun containsKey(key: String?): Boolean {
        return preferences!!.contains(key)
    }

    /**
     * This method store the boolean value in preferences
     *
     * @param key   (String)    : key for unique string value
     * @param value (boolean) : defaultValue to be passed
     */
    fun setBoolean(key: String?, value: Boolean) {
        editor!!.putBoolean(key, value)
        apply(editor)
    }

    /**
     * This method delete the value from preferences
     *
     * @param key (String) : key for unique string value
     */
    fun deletePreference(key: String?) {
        editor!!.remove(key)
        apply(editor)
    }

    /**
     * This method delete sp_v1 preference files from App
     */
    fun deleteAllPreferences() {
        editor!!.clear()
        apply(editor)
    }

    var id: String? = null

    //    val sharedPreference =  getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
    var hUserId = preferences?.getString("hostId", "")


    companion object {

        private var preferences: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null

        @get:SuppressLint("CommitPrefEdits")
        var instance: PrefHelper? = null
            get() {
                val PRIVATE_MODE = 0
                preferences = MyApp.instance.getSharedPreferences(APP_PREF, PRIVATE_MODE)
                editor = preferences!!.edit()
                if (field == null) {
                    field = PrefHelper()
                }
                return field
            }
        var loginResponse: LoginResponse.Data? = null
            private set

        const val KEY_IS_TOKEN = "token"
        const val KEY_MOBILE_NO = "number"
        const val USER_NAME = "userName"
        const val PROFILE = "profile"
        const val KEY_USER_ID = "hostId"
        private const val APP_PREF = "appPreference"
        private val sApplyMethod = findApplyMethod()
        private fun findApplyMethod(): Method? {
            try {
                val cls = SharedPreferences.Editor::class.java
                return cls.getMethod("apply")
            } catch (unused: NoSuchMethodException) {
                // fall through
            }
            return null
        }


        /**
         * This method apply change in preferences
         *
         * @param editor (SharedPreferences.Editor) : editor
         */
        private fun apply(editor: SharedPreferences.Editor?) {
            if (sApplyMethod != null) {
                try {
                    sApplyMethod.invoke(editor)
                    return
                } catch (unused: InvocationTargetException) {
                    // fall through
                } catch (unused: IllegalAccessException) {
                    // fall through
                }
            }
            editor!!.commit()
        }
    }
}