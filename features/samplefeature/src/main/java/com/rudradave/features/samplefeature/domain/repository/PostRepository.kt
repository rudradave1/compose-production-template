package com.rudradave.features.samplefeature.domain.repository

import com.rudradave.core.common.result.AppResult
import com.rudradave.features.samplefeature.domain.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for post flows and refresh operations.
 */
interface PostRepository {

    /**
     * Streams cached posts as the source of truth.
     */
    fun observePosts(): Flow<List<Post>>

    /**
     * Refreshes posts from remote and persists them locally.
     */
    suspend fun refreshPosts(): AppResult<Unit>
}
