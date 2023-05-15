package com.diyantech.easysplit.modelclass.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class  GetUserListResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data,
    @SerializedName("meta")
    @Expose
    val meta: Meta
) {
    data class Data(
        @SerializedName("result")
        @Expose
        val result: List<Result>
    ) {
        data class Result(
            @SerializedName("fullName")
            @Expose
            val fullName: String,
            @SerializedName("_id")
            @Expose
            val id: String,
            @SerializedName("isSelected")
            @Expose
            val isSelected: Boolean,
            @SerializedName("profileImage")
            @Expose
            val profileImage: String
        )
    }

    data class Meta(
        @SerializedName("code")
        @Expose
        val code: Int,
        @SerializedName("message")
        @Expose
        val message: String
    )
}