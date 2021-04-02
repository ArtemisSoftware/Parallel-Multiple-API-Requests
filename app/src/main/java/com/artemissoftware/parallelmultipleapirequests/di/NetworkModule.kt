package com.artemissoftware.parallelmultipleapirequests.di

import com.artemissoftware.parallelmultipleapirequests.api.CryptoCurrencyApi
import com.artemissoftware.parallelmultipleapirequests.api.JsonPlaceHolderApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @Named("CryptoCurrencyApi")
    fun providCryptoCurrencyApi(): CryptoCurrencyApi = Retrofit.Builder()
        .baseUrl(CryptoCurrencyApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoCurrencyApi::class.java)


    @Singleton
    @Provides
    @Named("JsonPlaceHolderApi")
    fun provideJsonPlaceHolderApi(): JsonPlaceHolderApi = Retrofit.Builder()
        .baseUrl(JsonPlaceHolderApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(JsonPlaceHolderApi::class.java)
}