package com.artemissoftware.parallelmultipleapirequests.repository

import com.artemissoftware.parallelmultipleapirequests.api.JsonPlaceHolderApi
import com.artemissoftware.parallelmultipleapirequests.api.models.Post
import javax.inject.Inject

class JsonPlaceHolderRepository @Inject constructor(private val api: JsonPlaceHolderApi) {

    suspend fun  getPosts(): List<Post> {
        return api.getPosts()
    }

    suspend fun  getPostsUserId(id: String): List<Post> {
        return api.getPostsUserId(id)
    }
}