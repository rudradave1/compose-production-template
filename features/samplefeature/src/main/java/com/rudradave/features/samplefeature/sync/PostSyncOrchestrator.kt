package com.rudradave.features.samplefeature.sync

import com.rudradave.core.common.result.AppResult
import com.rudradave.features.samplefeature.domain.usecase.RefreshPostsUseCase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Coordinates sync strategy and conflict handling for posts.
 */
@Singleton
class PostSyncOrchestrator @Inject constructor(
    private val refreshPostsUseCase: RefreshPostsUseCase
) {

    /**
     * Executes a sync pass.
     * Conflict strategy: remote wins by replacing matching IDs in Room.
     */
    suspend fun sync(): AppResult<Unit> = refreshPostsUseCase()
}
