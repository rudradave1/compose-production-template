package com.rudradave.features.samplefeature.data.mapper

import com.rudradave.core.database.entity.PostEntity
import com.rudradave.core.network.model.ApiPostDto
import com.rudradave.features.samplefeature.domain.model.Post

/**
 * Maps API DTOs into Room entities.
 */
fun ApiPostDto.toEntity(nowEpochMillis: Long): PostEntity {
    return PostEntity(
        id = id,
        userId = userId,
        title = title,
        body = body,
        syncedAtEpochMillis = nowEpochMillis
    )
}

/**
 * Maps Room entities into domain models.
 */
fun PostEntity.toDomain(): Post {
    return Post(
        id = id,
        userId = userId,
        title = title,
        body = body
    )
}
