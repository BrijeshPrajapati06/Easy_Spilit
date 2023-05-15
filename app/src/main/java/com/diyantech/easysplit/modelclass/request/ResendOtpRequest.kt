package com.diyantech.easysplit.modelclass.request


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ResendOtpRequest(
    @SerializedName("activity")
    @Expose
    val activity: Int,
    @SerializedName("userId")
    @Expose
    val userId: String
)