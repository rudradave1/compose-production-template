package com.rudradave.features.samplefeature.domain.usecase

import com.rudradave.core.common.result.AppResult
import com.rudradave.features.samplefeature.domain.repository.PostRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RefreshPostsUseCaseTest {

    private val repository: PostRepository = mockk()

    @Test
    fun `invoke returns success result`() = runTest {
        coEvery { repository.refreshPosts() } returns AppResult.Success(Unit)

        val useCase = RefreshPostsUseCase(repository)
        val result = useCase()

        assertTrue(result is AppResult.Success)
    }

    @Test
    fun `invoke returns error result`() = runTest {
        coEvery { repository.refreshPosts() } returns AppResult.Error("No network")

        val useCase = RefreshPostsUseCase(repository)
        val result = useCase()

        assertTrue(result is AppResult.Error)
        assertEquals("No network", (result as AppResult.Error).message)
    }

    @Test
    fun `invoke returns loading result`() = runTest {
        coEvery { repository.refreshPosts() } returns AppResult.Loading

        val useCase = RefreshPostsUseCase(repository)
        val result = useCase()

        assertTrue(result is AppResult.Loading)
    }

    @Test
    fun `invoke propagates repository exception`() = runTest {
        coEvery { repository.refreshPosts() } throws IllegalStateException("refresh crashed")

        val useCase = RefreshPostsUseCase(repository)

        var thrown: Throwable? = null
        try {
            useCase()
        } catch (throwable: Throwable) {
            thrown = throwable
        }
        assertTrue(thrown is IllegalStateException)
        assertEquals("refresh crashed", thrown?.message)
    }

    @Test
    fun `invoke calls repository once`() = runTest {
        coEvery { repository.refreshPosts() } returns AppResult.Success(Unit)

        val useCase = RefreshPostsUseCase(repository)
        useCase()

        coVerify(exactly = 1) { repository.refreshPosts() }
    }
}
