package com.rudradave.features.samplefeature.domain.usecase

import app.cash.turbine.test
import com.rudradave.features.samplefeature.domain.model.Post
import com.rudradave.features.samplefeature.domain.repository.PostRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ObservePostsUseCaseTest {

    private val repository: PostRepository = mockk()

    @Test
    fun `invoke emits empty list`() = runTest {
        every { repository.observePosts() } returns MutableStateFlow(emptyList())

        val useCase = ObservePostsUseCase(repository)

        useCase().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke emits non-empty list`() = runTest {
        val posts = listOf(Post(1, 1, "A", "B"))
        every { repository.observePosts() } returns MutableStateFlow(posts)

        val useCase = ObservePostsUseCase(repository)

        useCase().test {
            assertEquals(posts, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke emits updates from repository flow`() = runTest {
        val stateFlow = MutableStateFlow(emptyList<Post>())
        every { repository.observePosts() } returns stateFlow

        val useCase = ObservePostsUseCase(repository)

        useCase().test {
            assertTrue(awaitItem().isEmpty())
            val updated = listOf(Post(2, 1, "Title", "Body"))
            stateFlow.value = updated
            assertEquals(updated, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke propagates upstream flow error`() = runTest {
        every { repository.observePosts() } returns flow { throw IllegalStateException("flow failure") }

        val useCase = ObservePostsUseCase(repository)

        useCase().test {
            val throwable = awaitError()
            assertTrue(throwable is IllegalStateException)
            assertEquals("flow failure", throwable.message)
        }
    }

    @Test
    fun `invoke requests stream from repository once`() = runTest {
        every { repository.observePosts() } returns MutableStateFlow(emptyList())

        val useCase = ObservePostsUseCase(repository)
        useCase().test { cancelAndIgnoreRemainingEvents() }

        verify(exactly = 1) { repository.observePosts() }
    }
}
