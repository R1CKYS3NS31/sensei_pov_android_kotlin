package com.example.data.service.work.works

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.example.data.common.dispatcher.Dispatcher
import com.example.data.common.dispatcher.PoVDispatchers
import com.example.data.common.util.datasync.Synchronizer
import com.example.data.data.repository.pov.PoVRepository
import com.example.data.data.repository.user.UserRepository
import com.example.data.service.work.notification.syncConstraints
import com.example.data.service.work.notification.syncForegroundInfo
import com.example.data.service.work.status.SyncSubscriber
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    @Dispatcher(PoVDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val syncSubscriber: SyncSubscriber,
    private val userRepository: UserRepository,
    private val poVRepository: PoVRepository
) : CoroutineWorker(context, workerParameters), Synchronizer {
    override suspend fun getForegroundInfo(): ForegroundInfo = context.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        syncSubscriber.subscribe()

        val syncedSuccessfully = awaitAll(
            async { /* repos.sync*/
                userRepository.sync()
                poVRepository.sync()
            }).all { it }
        if (syncedSuccessfully) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        fun startUpSyncWork() =
            OneTimeWorkRequestBuilder<DelegatingWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(syncConstraints).setInputData(SyncWorker::class.delegateData())
                .build()
    }
}