package com.rudradave.core.network.api

import com.rudradave.core.network.model.ApiPostDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit API definitions for jsonplaceholder.
 */
interface JsonPlaceholderApi {

    /**
     * Returns a list of posts.
     */
    @GET("posts")
    suspend fun getPosts(): Response<List<ApiPostDto>>
}
