package com.artemissoftware.parallelmultipleapirequests.api

import com.artemissoftware.parallelmultipleapirequests.api.models.Post

import retrofit2.http.GET
import retrofit2.http.Path


interface JsonPlaceHolderApi {

    companion object{
        const val BASE_URL= "https://jsonplaceholder.typicode.com/"
    }

    @GET("posts")
    fun getPosts(): List<Post>

    @GET("posts?userId={id}")
    fun getPostsUserId(@Path("id") id: String): List<Post>
}