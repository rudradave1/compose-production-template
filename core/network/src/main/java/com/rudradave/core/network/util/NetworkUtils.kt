package com.rudradave.core.network.util

import com.rudradave.core.common.result.AppResult
import retrofit2.Response

/**
 * Executes a Retrofit [call] and maps API response into [AppResult].
 */
suspend fun <T> safeApiCall(call: suspend () -> Response<T>): AppResult<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                AppResult.Success(body)
            } else {
                AppResult.Error("Received empty response body")
            }
        } else {
            AppResult.Error("Request failed with code ${response.code()}")
        }
    } catch (throwable: Throwable) {
        AppResult.Error(
            message = throwable.message ?: "Unknown network error",
            cause = throwable
        )
    }
}
