package com.rudradave.core.common.result

/**
 * Standard result wrapper used for remote and local operations.
 */
sealed class AppResult<out T> {
    /**
     * Represents successful execution with [data].
     */
    data class Success<T>(val data: T) : AppResult<T>()

    /**
     * Represents failed execution with [message] and optional [cause].
     */
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : AppResult<Nothing>()

    /**
     * Represents an operation currently in progress.
     */
    data object Loading : AppResult<Nothing>()
}
