package com.rudradave.core.common.ui

/**
 * Generic UI state model for Compose screens.
 */
sealed interface UiState<out T> {
    /**
     * UI is actively loading content.
     */
    data object Loading : UiState<Nothing>

    /**
     * UI has a [data] payload to render.
     */
    data class Success<T>(val data: T) : UiState<T>

    /**
     * UI is in an error state with [message].
     */
    data class Error(val message: String) : UiState<Nothing>
}
