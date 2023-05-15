package com.diyantech.easysplit.modelclass.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class EventCreateResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data,
    @SerializedName("meta")
    @Expose
    val meta: Meta
) {
    data class Data(
        @SerializedName("createdAt")
        @Expose
        val createdAt: String,
        @SerializedName("description")
        @Expose
        val description: String,
        @SerializedName("eventName")
        @Expose
        val eventName: String,
        @SerializedName("hostId")
        @Expose
        val hostId: String,
        @SerializedName("_id")
        @Expose
        val id: String,
        @SerializedName("totalSpendAmount")
        @Expose
        val totalSpendAmount: Int
    )

    data class Meta(
        @SerializedName("code")
        @Expose
        val code: Int,
        @SerializedName("message")
        @Expose
        val message: String
    )
}