package com.artemissoftware.parallelmultipleapirequests.api.models


import com.google.gson.annotations.SerializedName

data class Market(
    @SerializedName("market")
    val market: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("volume")
    val volume: String
)