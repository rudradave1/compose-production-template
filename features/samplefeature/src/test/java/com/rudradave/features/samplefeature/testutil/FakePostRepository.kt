package com.rudradave.features.samplefeature.testutil

import com.rudradave.core.common.result.AppResult
import com.rudradave.features.samplefeature.domain.model.Post
import com.rudradave.features.samplefeature.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * In-memory fake repository for deterministic unit tests.
 */
class FakePostRepository(
    initialPosts: List<Post> = emptyList()
) : PostRepository {

    private val posts = MutableStateFlow(initialPosts)

    var refreshResult: AppResult<Unit> = AppResult.Success(Unit)
    var refreshCalls: Int = 0

    override fun observePosts(): Flow<List<Post>> = posts

    override suspend fun refreshPosts(): AppResult<Unit> {
        refreshCalls += 1
        return refreshResult
    }

    fun emitPosts(value: List<Post>) {
        posts.value = value
    }
}
