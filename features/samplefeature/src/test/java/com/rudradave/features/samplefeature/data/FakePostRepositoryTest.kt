package com.rudradave.features.samplefeature.data

import com.rudradave.core.common.result.AppResult
import com.rudradave.features.samplefeature.testutil.FakePostRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FakePostRepositoryTest {

    @Test
    fun refreshPosts_returnsConfiguredErrorResult() = runTest {
        val repository = FakePostRepository()
        repository.refreshResult = AppResult.Error("Forced error")

        val result = repository.refreshPosts()

        assertTrue(result is AppResult.Error)
        assertEquals("Forced error", (result as AppResult.Error).message)
        assertEquals(1, repository.refreshCalls)
    }

    @Test
    fun observePosts_emitsCurrentInMemoryValue() = runTest {
        val repository = FakePostRepository()
        repository.emitPosts(emptyList())

        val posts = repository.observePosts().first()

        assertEquals(0, posts.size)
    }
}
