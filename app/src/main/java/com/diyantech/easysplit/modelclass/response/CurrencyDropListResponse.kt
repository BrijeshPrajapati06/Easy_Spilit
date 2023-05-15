package com.diyantech.easysplit.modelclass.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CurrencyDropListResponse(
    @SerializedName("data")
    @Expose
    val `data`: List<Data>,
    @SerializedName("meta")
    @Expose
    val meta: Meta
) {
    data class Data(
        @SerializedName("currencyName")
        @Expose
        val currencyName: String,
        @SerializedName("currencySign")
        @Expose
        val currencySign: String,
        @SerializedName("_id")
        @Expose
        val id: String,
        @SerializedName("isSelected")
        @Expose
        val isSelected: Boolean
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