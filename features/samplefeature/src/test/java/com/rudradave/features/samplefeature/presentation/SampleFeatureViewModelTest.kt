package com.rudradave.features.samplefeature.presentation

import app.cash.turbine.test
import com.rudradave.core.common.result.AppResult
import com.rudradave.core.common.ui.UiState
import com.rudradave.features.samplefeature.domain.model.Post
import com.rudradave.features.samplefeature.domain.usecase.ObservePostsUseCase
import com.rudradave.features.samplefeature.domain.usecase.RefreshPostsUseCase
import com.rudradave.features.samplefeature.testutil.MainDispatcherExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class SampleFeatureViewModelTest {

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    private val observePostsUseCase: ObservePostsUseCase = mockk()
    private val refreshPostsUseCase: RefreshPostsUseCase = mockk()

    @Test
    fun `init with success refresh emits success posts state`() = runTest {
        val postsFlow = MutableStateFlow(listOf(Post(1, 1, "Alpha", "Body")))
        every { observePostsUseCase.invoke() } returns postsFlow
        coEvery { refreshPostsUseCase.invoke() } returns AppResult.Success(Unit)

        val viewModel = SampleFeatureViewModel(observePostsUseCase, refreshPostsUseCase)
        advanceUntilIdle()
        viewModel.uiState.test {
            val latest = awaitItem()
            assertTrue(latest.postsState is UiState.Success)
            assertEquals(1, (latest.postsState as UiState.Success).data.size)
            assertEquals(false, latest.isRefreshing)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init with empty posts emits empty success state`() = runTest {
        every { observePostsUseCase.invoke() } returns MutableStateFlow(emptyList())
        coEvery { refreshPostsUseCase.invoke() } returns AppResult.Success(Unit)

        val viewModel = SampleFeatureViewModel(observePostsUseCase, refreshPostsUseCase)
        advanceUntilIdle()
        viewModel.uiState.test {
            val latest = awaitItem()
            val posts = (latest.postsState as UiState.Success).data
            assertTrue(posts.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init refresh error sets error message`() = runTest {
        every { observePostsUseCase.invoke() } returns MutableStateFlow(emptyList())
        coEvery { refreshPostsUseCase.invoke() } returns AppResult.Error("No network")

        val viewModel = SampleFeatureViewModel(observePostsUseCase, refreshPostsUseCase)
        advanceUntilIdle()
        viewModel.uiState.test {
            val latest = awaitItem()
            assertEquals("No network", latest.errorMessage)
            assertEquals(false, latest.isRefreshing)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init refresh loading keeps refreshing true`() = runTest {
        every { observePostsUseCase.invoke() } returns MutableStateFlow(emptyList())
        coEvery { refreshPostsUseCase.invoke() } returns AppResult.Loading

        val viewModel = SampleFeatureViewModel(observePostsUseCase, refreshPostsUseCase)
        advanceUntilIdle()
        viewModel.uiState.test {
            val latest = awaitItem()
            assertEquals(true, latest.isRefreshing)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh clears old error before successful refresh`() = runTest {
        every { observePostsUseCase.invoke() } returns MutableStateFlow(emptyList())
        coEvery { refreshPostsUseCase.invoke() } returnsMany listOf(
            AppResult.Error("Old error"),
            AppResult.Success(Unit)
        )

        val viewModel = SampleFeatureViewModel(observePostsUseCase, refreshPostsUseCase)
        advanceUntilIdle()

        viewModel.refresh()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals(false, viewModel.uiState.value.isRefreshing)
    }

    @Test
    fun `onSearchQueryChange filters by title and body`() = runTest {
        val posts = listOf(
            Post(1, 1, "Android", "Kotlin"),
            Post(2, 1, "Server", "Compose")
        )
        every { observePostsUseCase.invoke() } returns MutableStateFlow(posts)
        coEvery { refreshPostsUseCase.invoke() } returns AppResult.Success(Unit)

        val viewModel = SampleFeatureViewModel(observePostsUseCase, refreshPostsUseCase)
        advanceUntilIdle()

        viewModel.onSearchQueryChange("compose")
        advanceUntilIdle()

        val filtered = (viewModel.uiState.value.postsState as UiState.Success).data
        assertEquals(1, filtered.size)
        assertEquals(2L, filtered.first().id)
    }

    @Test
    fun `onSearchQueryChange blank shows all posts`() = runTest {
        val posts = listOf(
            Post(1, 1, "One", "A"),
            Post(2, 1, "Two", "B")
        )
        every { observePostsUseCase.invoke() } returns MutableStateFlow(posts)
        coEvery { refreshPostsUseCase.invoke() } returns AppResult.Success(Unit)

        val viewModel = SampleFeatureViewModel(observePostsUseCase, refreshPostsUseCase)
        advanceUntilIdle()

        viewModel.onSearchQueryChange("o")
        advanceUntilIdle()
        viewModel.onSearchQueryChange("")
        advanceUntilIdle()

        val visible = (viewModel.uiState.value.postsState as UiState.Success).data
        assertEquals(2, visible.size)
    }

    @Test
    fun `onNotesChange and clearError update transient ui fields`() = runTest {
        every { observePostsUseCase.invoke() } returns MutableStateFlow(emptyList())
        coEvery { refreshPostsUseCase.invoke() } returns AppResult.Error("error")

        val viewModel = SampleFeatureViewModel(observePostsUseCase, refreshPostsUseCase)
        advanceUntilIdle()

        viewModel.onNotesChange("test note")
        viewModel.clearError()
        advanceUntilIdle()

        assertEquals("test note", viewModel.uiState.value.notesInput)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `refresh called manually invokes refresh use case again`() = runTest {
        every { observePostsUseCase.invoke() } returns MutableStateFlow(emptyList())
        coEvery { refreshPostsUseCase.invoke() } returns AppResult.Success(Unit)

        val viewModel = SampleFeatureViewModel(observePostsUseCase, refreshPostsUseCase)
        advanceUntilIdle()
        viewModel.refresh()
        advanceUntilIdle()

        coVerify(exactly = 2) { refreshPostsUseCase.invoke() }
    }
}
