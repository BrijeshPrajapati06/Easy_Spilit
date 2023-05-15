package com.diyantech.easysplit.modelclass.request


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class LoginRequest(
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("pwd")
    @Expose
    val pwd: String
)