package com.diyantech.easysplit.utils

import android.content.SharedPreferences
import android.util.Log
import com.diyantech.easysplit.modelclass.response.LoginResponse
import com.diyantech.easysplit.modelclass.response.SignupResponse
import com.diyantech.easysplit.modelclass.response.UserUpdateResponse
import com.google.gson.Gson
import com.google.gson.JsonParser

class Session {

    companion object{

        private val ourInstance: Session = Session()

        private val mPrefs: SharedPreferences? = null
        private val PREF_CHECK_VRESION = "pref_check_version"

        private val gson = Gson()

        fun getInstance(): Session? {
            return ourInstance
        }

        /**
         * Sets current user.
         *
         * @param Data the LoginResponse object
         */
        fun setCurrentUser(data: LoginResponse.Data) {
            PrefHelper.instance?.setString("user", Gson().toJson(data))
        }


        fun getCurrentUser(): LoginResponse.Data? {
            val strUser: String = PrefHelper.instance?.getString("user", "")!!
            Log.d("TAG", "getCurrentUser: ---->$strUser")
            return if (strUser.isEmpty()) {
                null
            } else {
                val parser = JsonParser()
                val mJson = parser.parse(strUser)
                val gson = Gson()
                gson.fromJson(
                    mJson,
                    LoginResponse.Data::class.java
                )
            }
        }

//        fun setSignUpUser(signUpData: SignupResponse.Data) {
//            PrefHelper.instance?.setString("signupuser", Gson().toJson(signUpData))
//        }
//
//        fun getSignUpUser(): SignupResponse.Data? {
//            val strSignUpUser: String = PrefHelper.instance?.getString("signupuser", "")!!
//            Log.d("TAG", "getCurrentUser: ---->$strSignUpUser")
//            return if (strSignUpUser.isEmpty()) {
//                null
//            }
//            else {
//                val parser = JsonParser()
//                val mJson = parser.parse(strSignUpUser)
//                val gson = Gson()
//                gson.fromJson(
//                    mJson,
//                    SignupResponse.Data::class.java
//                )
//            }
//        }

    }


}