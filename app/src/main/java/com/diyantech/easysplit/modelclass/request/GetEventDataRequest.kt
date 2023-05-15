package com.diyantech.easysplit.modelclass.request


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetEventDataRequest(
    @SerializedName("hostId")
    @Expose
    val hostId: String?
)