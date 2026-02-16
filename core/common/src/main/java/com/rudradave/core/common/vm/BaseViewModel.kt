package com.rudradave.core.common.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Base ViewModel with a helper to launch protected coroutines.
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * Launches [block] and redirects uncaught failures to [onError].
     */
    protected fun launchSafe(
        onError: (Throwable) -> Unit,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            onError(throwable)
        }
        viewModelScope.launch(handler, block = block)
    }
}
