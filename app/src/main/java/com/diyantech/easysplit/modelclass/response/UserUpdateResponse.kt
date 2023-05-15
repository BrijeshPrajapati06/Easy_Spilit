package com.diyantech.easysplit.modelclass.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class UserUpdateResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data,
    @SerializedName("meta")
    @Expose
    val meta: Meta
) {
    data class Data(
        @SerializedName("email")
        @Expose
        val email: String,
        @SerializedName("fullName")
        @Expose
        val fullName: String,
        @SerializedName("gender")
        @Expose
        val gender: String,
        @SerializedName("mobile")
        @Expose
        val mobile: String
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