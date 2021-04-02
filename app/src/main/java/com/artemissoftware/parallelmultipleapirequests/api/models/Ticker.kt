package com.artemissoftware.parallelmultipleapirequests.api.models


import com.google.gson.annotations.SerializedName

data class Ticker(
    @SerializedName("base")
    val base: String,
    @SerializedName("change")
    val change: String,
    @SerializedName("markets")
    val markets: List<Market>,
    @SerializedName("price")
    val price: String,
    @SerializedName("target")
    val target: String,
    @SerializedName("volume")
    val volume: String
)