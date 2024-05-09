package com.example.data.common.util.network.di

import com.example.data.common.util.network.ConnectivityManagerNetworkMonitor
import com.example.data.common.util.network.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkMonitorModule {
    /* bind network monitor */
    @Binds
    @Singleton
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor
}