package com.rudradave.features.samplefeature.presentation

import com.rudradave.core.common.result.AppResult
import com.rudradave.core.common.ui.UiState
import com.rudradave.features.samplefeature.domain.model.Post
import com.rudradave.features.samplefeature.domain.usecase.ObservePostsUseCase
import com.rudradave.features.samplefeature.domain.usecase.RefreshPostsUseCase
import com.rudradave.features.samplefeature.testutil.FakePostRepository
import com.rudradave.features.samplefeature.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SampleFeatureViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun init_emitsPostsAndTriggersInitialRefresh() = runTest {
        val fakeRepository = FakePostRepository(
            initialPosts = listOf(
                Post(id = 1, userId = 1, title = "Alpha", body = "Body A")
            )
        )

        val viewModel = SampleFeatureViewModel(
            observePostsUseCase = ObservePostsUseCase(fakeRepository),
            refreshPostsUseCase = RefreshPostsUseCase(fakeRepository)
        )

        advanceUntilIdle()

        val postsState = viewModel.uiState.value.postsState
        assertTrue(postsState is UiState.Success)
        assertEquals(1, (postsState as UiState.Success).data.size)
        assertEquals(1, fakeRepository.refreshCalls)
    }

    @Test
    fun refreshError_setsErrorMessageAndStopsRefreshing() = runTest {
        val fakeRepository = FakePostRepository()
        fakeRepository.refreshResult = AppResult.Error("No network")

        val viewModel = SampleFeatureViewModel(
            observePostsUseCase = ObservePostsUseCase(fakeRepository),
            refreshPostsUseCase = RefreshPostsUseCase(fakeRepository)
        )

        advanceUntilIdle()
        viewModel.refresh()
        advanceUntilIdle()

        assertEquals("No network", viewModel.uiState.value.errorMessage)
        assertEquals(false, viewModel.uiState.value.isRefreshing)
    }
}
