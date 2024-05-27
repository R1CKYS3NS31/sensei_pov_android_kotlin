package com.example.data.common.util.datasync

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

const val SYNC_UTILITY_TAG = "SyncUtility"

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}

interface Synchronizer {
    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}

private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (throwable: Throwable) {
    Log.e(
        SYNC_UTILITY_TAG,
        "failed to evaluate a suspendRunCatchingBlock, Returning failure Result",
        throwable
    )
    Result.failure(throwable)
}

suspend fun <T> Synchronizer.dataSynchronizer(
    remoteData: Set<T>,
    localData: Set<T>,
    localModelUpdater: suspend (T) -> Unit,
    remoteModelUpdater: suspend (T) -> Unit = {},
    remoteModelDeleter: suspend (T) -> Unit = {}
) = suspendRunCatching {
    val addToLocal = remoteData - localData
    val updateToLocal = localData.intersect(remoteData)
    val deleteToRemote = localData - remoteData

    addToLocal.forEach {
        localModelUpdater(it)
    }
    updateToLocal.forEach { localModelUpdater(it) }
    deleteToRemote.forEach { remoteModelDeleter(it) }
    localData.forEach { remoteModelUpdater(it) }
}.isSuccess


fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    transform: suspend (T1, T2, T3, T4, T5, T6) -> R,
): Flow<R> = combine(
    combine(flow, flow2, flow3, ::Triple),
    combine(flow4, flow5, flow6, ::Triple),
) { t1, t2 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third
    )
}