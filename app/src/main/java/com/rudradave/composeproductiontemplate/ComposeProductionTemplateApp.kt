package com.rudradave.composeproductiontemplate

import android.app.Application
import com.rudradave.features.samplefeature.worker.SampleSyncWorker
import java.util.concurrent.TimeUnit

/**
 * Application entrypoint configuring Hilt and WorkManager sync.
 */
@dagger.hilt.android.HiltAndroidApp
class ComposeProductionTemplateApp : Application() {

    override fun onCreate() {
        super.onCreate()
        schedulePeriodicSync()
    }

    private fun schedulePeriodicSync() {
        val request = androidx.work.PeriodicWorkRequestBuilder<SampleSyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(
                androidx.work.Constraints.Builder()
                    .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
                    .build()
            )
            .addTag(SYNC_WORK_TAG)
            .build()

        androidx.work.WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            UNIQUE_SYNC_WORK_NAME,
            androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    companion object {
        private const val UNIQUE_SYNC_WORK_NAME = "sample_feature_sync_work"
        private const val SYNC_WORK_TAG = "sync_posts"
    }
}
