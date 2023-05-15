package com.diyantech.easysplit.ApiKtService

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.diyantech.easysplit.modelclass.response.LoginResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.preference.PreferenceManager
import com.diyantech.easysplit.MyApp
import com.diyantech.easysplit.utils.Session


object ApiUtility {

    var loginData : LoginResponse.Data? = null

    fun getUserToken(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        if (Session.getCurrentUser() != null){
            loginData = Session.getCurrentUser()
        }


        var tokenclient: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer " + loginData?.myToken).build()


            Log.d("TAG", "token: --->"+ loginData?.myToken)
            return@Interceptor chain.proceed(newRequest)
        }).addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl("http://122.170.0.3:3018/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(tokenclient)
            .build()
    }

    fun getUser(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .build()
            return@Interceptor chain.proceed(newRequest)
        }).addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl("http://122.170.0.3:3018/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

}