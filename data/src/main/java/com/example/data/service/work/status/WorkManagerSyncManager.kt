package com.example.data.service.work.status

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.data.common.util.datasync.SyncManager
import com.example.data.service.work.initializer.SYNC_WORK_NAME
import com.example.data.service.work.works.SyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private fun List<WorkInfo>.anyRunning() = any { it.state == WorkInfo.State.RUNNING }

class WorkManagerSyncManager @Inject constructor(
    @ApplicationContext private val context: Context
) : SyncManager {
    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkFlow(SYNC_WORK_NAME)
            .map(List<WorkInfo>::anyRunning).conflate()

    override fun requestSync() {
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniqueWork(
            SYNC_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            SyncWorker.startUpSyncWork()
        )
    }
}