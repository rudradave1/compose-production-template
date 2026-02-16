package com.rudradave.core.network.model

import com.squareup.moshi.JsonClass

/**
 * DTO returned by jsonplaceholder post API.
 */
@JsonClass(generateAdapter = true)
data class ApiPostDto(
    val userId: Long,
    val id: Long,
    val title: String,
    val body: String
)
