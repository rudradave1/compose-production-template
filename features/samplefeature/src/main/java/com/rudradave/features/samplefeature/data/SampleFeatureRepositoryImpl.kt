package com.rudradave.features.samplefeature.data

import com.rudradave.core.common.result.AppResult
import com.rudradave.core.database.dao.PostDao
import com.rudradave.core.network.api.JsonPlaceholderApi
import com.rudradave.core.network.util.NetworkMonitor
import com.rudradave.core.network.util.safeApiCall
import com.rudradave.features.samplefeature.data.mapper.toDomain
import com.rudradave.features.samplefeature.data.mapper.toEntity
import com.rudradave.features.samplefeature.domain.model.Post
import com.rudradave.features.samplefeature.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository implementation with Room as single source of truth.
 */
class SampleFeatureRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val api: JsonPlaceholderApi,
    private val networkMonitor: NetworkMonitor
) : PostRepository {

    override fun observePosts(): Flow<List<Post>> {
        return postDao.observePosts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshPosts(): AppResult<Unit> {
        if (!networkMonitor.isOnline()) {
            return AppResult.Error("No network available. Showing cached data.")
        }

        return when (val result = safeApiCall { api.getPosts() }) {
            is AppResult.Success -> {
                val now = System.currentTimeMillis()
                val entities = result.data.map { it.toEntity(nowEpochMillis = now) }
                postDao.upsertAll(entities)
                AppResult.Success(Unit)
            }

            is AppResult.Error -> result
            AppResult.Loading -> AppResult.Loading
        }
    }
}
