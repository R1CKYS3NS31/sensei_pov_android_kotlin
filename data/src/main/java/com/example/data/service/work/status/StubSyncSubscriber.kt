package com.example.data.service.work.status

import android.util.Log
import javax.inject.Inject

const val TAG = "StubSyncSubscriber"
class StubSyncSubscriber @Inject constructor():SyncSubscriber {
    override suspend fun subscribe() {
        Log.d(TAG, "subscribing to sync")
    }
}