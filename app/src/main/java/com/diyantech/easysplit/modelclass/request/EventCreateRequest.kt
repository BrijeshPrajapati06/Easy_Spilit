package com.diyantech.easysplit.modelclass.request


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class EventCreateRequest(
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("eventName")
    @Expose
    val eventName: String
)