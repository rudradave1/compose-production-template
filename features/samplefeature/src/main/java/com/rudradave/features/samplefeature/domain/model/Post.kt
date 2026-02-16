package com.rudradave.features.samplefeature.domain.model

/**
 * Domain model consumed by presentation.
 */
data class Post(
    val id: Long,
    val userId: Long,
    val title: String,
    val body: String
)
