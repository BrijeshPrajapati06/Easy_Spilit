package com.diyantech.easysplit.modelclass.request


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CreatePassworsRequest(
    @SerializedName("pwd")
    @Expose
    val pwd: String,
    @SerializedName("userId")
    @Expose
    val userId: String
)