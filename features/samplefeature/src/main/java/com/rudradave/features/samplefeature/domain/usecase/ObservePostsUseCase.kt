package com.rudradave.features.samplefeature.domain.usecase

import com.rudradave.features.samplefeature.domain.model.Post
import com.rudradave.features.samplefeature.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for observing cached posts.
 */
class ObservePostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    /**
     * Executes stream retrieval.
     */
    operator fun invoke(): Flow<List<Post>> = repository.observePosts()
}
