package com.rudradave.features.samplefeature.presentation

import com.rudradave.core.common.ui.UiState
import com.rudradave.features.samplefeature.domain.model.Post

/**
 * UI state holder for sample feature.
 */
data class SampleFeatureUiState(
    val postsState: UiState<List<Post>> = UiState.Loading,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val notesInput: String = ""
)
