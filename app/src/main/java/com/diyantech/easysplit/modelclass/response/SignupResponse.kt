package com.diyantech.easysplit.modelclass.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SignupResponse(
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
        @SerializedName("email")
        @Expose
        val email: String,
        @SerializedName("fullName")
        @Expose
        val fullName: String,
        @SerializedName("gender")
        @Expose
        val gender: String,
        @SerializedName("_id")
        @Expose
        val id: String,
        @SerializedName("mobile")
        @Expose
        val mobile: String,
            @SerializedName("`profileImage`")
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