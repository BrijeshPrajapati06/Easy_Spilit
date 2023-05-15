package com.diyantech.easysplit.modelclass.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class OtpResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data,
    @SerializedName("meta")
    @Expose
    val meta: Meta
) {
    data class Data(
        @SerializedName("_id")
        @Expose
        val id: String
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