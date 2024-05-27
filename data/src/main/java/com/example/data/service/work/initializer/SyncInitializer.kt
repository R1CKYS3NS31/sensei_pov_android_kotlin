package com.example.data.service.work.initializer

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.data.service.work.works.SyncWorker

internal const val SYNC_WORK_NAME = "SyncWorkName"

object Sync {
    fun initializer(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(SYNC_WORK_NAME, ExistingWorkPolicy.KEEP, SyncWorker.startUpSyncWork())
        }
    }
}