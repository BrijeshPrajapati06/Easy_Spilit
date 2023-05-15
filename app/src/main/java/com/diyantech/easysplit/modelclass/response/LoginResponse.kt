package com.diyantech.easysplit.modelclass.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class LoginResponse(
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
        @SerializedName("_id")
        @Expose
        val id: String,
        @SerializedName("jti")
        @Expose
        val jti: String,
        @SerializedName("myToken")
        @Expose
        val myToken: String,
        @SerializedName("profileImage")
        @Expose
        val profileImage: String
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