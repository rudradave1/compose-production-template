package com.rudradave.features.samplefeature.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rudradave.core.common.result.AppResult
import com.rudradave.features.samplefeature.sync.PostSyncOrchestrator
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Periodic worker that keeps local cache synchronized with remote source.
 */
class SampleSyncWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            SampleSyncWorkerEntryPoint::class.java
        )
        val syncOrchestrator = entryPoint.syncOrchestrator()

        return when (syncOrchestrator.sync()) {
            is AppResult.Success -> Result.success()
            is AppResult.Error -> Result.retry()
            AppResult.Loading -> Result.retry()
        }
    }
}

/**
 * Entry point used by WorkManager worker to access Hilt graph dependencies.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface SampleSyncWorkerEntryPoint {
    /**
     * Provides sync orchestrator instance.
     */
    fun syncOrchestrator(): PostSyncOrchestrator
}
