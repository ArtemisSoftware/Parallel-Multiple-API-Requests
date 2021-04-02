package com.artemissoftware.parallelmultipleapirequests.api.models


import com.google.gson.annotations.SerializedName

data class Crypto(
    @SerializedName("error")
    val error: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("ticker")
    val ticker: Ticker,
    @SerializedName("timestamp")
    val timestamp: Int
)