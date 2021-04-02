package com.artemissoftware.parallelmultipleapirequests.api

import com.artemissoftware.parallelmultipleapirequests.api.models.Crypto
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CryptoCurrencyApi {

    companion object{
        const val BASE_URL= "https://api.cryptonator.com/api/full/";
    }

    @GET("{coin}-usd")
    suspend fun getCoinData(@Path("coin") coin: String?): Crypto

    @GET("error")
    suspend fun getError(): Crypto
}