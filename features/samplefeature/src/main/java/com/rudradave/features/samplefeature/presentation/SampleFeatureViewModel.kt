package com.rudradave.features.samplefeature.presentation

import androidx.lifecycle.viewModelScope
import com.rudradave.core.common.result.AppResult
import com.rudradave.core.common.ui.UiState
import com.rudradave.core.common.vm.BaseViewModel
import com.rudradave.features.samplefeature.domain.model.Post
import com.rudradave.features.samplefeature.domain.usecase.ObservePostsUseCase
import com.rudradave.features.samplefeature.domain.usecase.RefreshPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for form/list sample feature screen.
 */
@HiltViewModel
class SampleFeatureViewModel @Inject constructor(
    private val observePostsUseCase: ObservePostsUseCase,
    private val refreshPostsUseCase: RefreshPostsUseCase
) : BaseViewModel() {

    private var allPosts = emptyList<Post>()

    private val _uiState = MutableStateFlow(SampleFeatureUiState())
    val uiState: StateFlow<SampleFeatureUiState> = _uiState.asStateFlow()

    init {
        observePosts()
        refresh()
    }

    /**
     * Updates sample text field query.
     */
    fun onSearchQueryChange(value: String) {
        _uiState.update { it.copy(searchQuery = value) }
        applyFilter()
    }

    /**
     * Updates notes input field.
     */
    fun onNotesChange(value: String) {
        _uiState.update { it.copy(notesInput = value) }
    }

    /**
     * Refreshes data manually.
     */
    fun refresh() {
        launchSafe(
            onError = { throwable ->
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        errorMessage = throwable.message ?: "Unexpected refresh failure"
                    )
                }
            }
        ) {
            _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            when (val result = refreshPostsUseCase()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isRefreshing = false) }
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            errorMessage = result.message
                        )
                    }
                }

                AppResult.Loading -> {
                    _uiState.update { it.copy(isRefreshing = true) }
                }
            }
        }
    }

    /**
     * Clears transient error message after it is displayed.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun observePosts() {
        viewModelScope.launch {
            observePostsUseCase().collect { posts ->
                allPosts = posts
                applyFilter()
            }
        }
    }

    private fun applyFilter() {
        val query = _uiState.value.searchQuery
        val filtered = allPosts.filter { post ->
            query.isBlank() ||
                post.title.contains(query, ignoreCase = true) ||
                post.body.contains(query, ignoreCase = true)
        }
        _uiState.update { it.copy(postsState = UiState.Success(filtered)) }
    }
}
