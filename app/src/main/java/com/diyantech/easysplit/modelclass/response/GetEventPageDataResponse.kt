package com.diyantech.easysplit.modelclass.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetEventPageDataResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data,
    @SerializedName("meta")
    @Expose
    val meta: Meta
) {
    data class Data(
        @SerializedName("nextPage")
        @Expose
        val nextPage: Int,
        @SerializedName("result")
        @Expose
        val result: List<Result>,
        @SerializedName("recordsTotal")
        @Expose
        val recordsTotal: Int
    ) {
        data class Result(
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