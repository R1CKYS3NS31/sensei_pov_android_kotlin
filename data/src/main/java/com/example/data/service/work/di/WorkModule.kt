package com.example.data.service.work.di

import com.example.data.common.util.datasync.SyncManager
import com.example.data.service.work.status.StubSyncSubscriber
import com.example.data.service.work.status.SyncSubscriber
import com.example.data.service.work.status.WorkManagerSyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WorkModule {

    @Singleton
    @Binds
    fun bindsSyncSubscriber(
        syncSubscriber: StubSyncSubscriber
    ): SyncSubscriber

    @Singleton
    @Binds
    fun bindsSyncManager(
        syncManager: WorkManagerSyncManager
    ): SyncManager
}