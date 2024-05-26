package com.example.data.common.util.datasync

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    val isSyncing:Flow<Boolean>
    fun requestSync()
}