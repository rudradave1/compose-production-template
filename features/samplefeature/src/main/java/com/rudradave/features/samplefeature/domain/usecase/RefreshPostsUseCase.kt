package com.rudradave.features.samplefeature.domain.usecase

import com.rudradave.core.common.result.AppResult
import com.rudradave.features.samplefeature.domain.repository.PostRepository
import javax.inject.Inject

/**
 * Use case for explicit post refresh from remote source.
 */
class RefreshPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    /**
     * Executes refresh and persistence.
     */
    suspend operator fun invoke(): AppResult<Unit> = repository.refreshPosts()
}
