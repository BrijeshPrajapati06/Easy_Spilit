package com.diyantech.easysplit.modelclass.request


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class OtpRequest(
    @SerializedName("OTP")
    @Expose
    val oTP: String,
    @SerializedName("userId")
    @Expose
    val userId: String
)