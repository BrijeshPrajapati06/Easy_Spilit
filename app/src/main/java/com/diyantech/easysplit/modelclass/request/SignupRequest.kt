package com.diyantech.easysplit.modelclass.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("fullName")
    @Expose
    val fullName: String,

    @SerializedName("profileImage")
    @Expose
    var profileImage : String,

    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("pwd")
    @Expose
    val pwd: String,

    @SerializedName("mobile")
    @Expose
    val mobile : String,

    @SerializedName("gender")
    @Expose
    val gender: String,


) {





}