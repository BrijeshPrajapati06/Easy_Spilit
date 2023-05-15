package com.diyantech.easysplit.modelclass.request


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ChangePasswordRequest(
    @SerializedName("_id")
    @Expose
    val id: String,
    @SerializedName("newpwd")
    @Expose
    val newpwd: String,
    @SerializedName("oldpwd")
    @Expose
    val oldpwd: String
)