package com.artemissoftware.parallelmultipleapirequests.repository

import com.artemissoftware.parallelmultipleapirequests.api.CryptoCurrencyApi
import com.artemissoftware.parallelmultipleapirequests.api.models.Crypto
import javax.inject.Inject

class CryptoCurrencyRepository @Inject constructor(private val api: CryptoCurrencyApi) {

    suspend fun getCoinData(coin: String): Crypto {
        return api.getCoinData(coin)
    }

    suspend fun getError(): Crypto {
        return api.getError()
    }
}