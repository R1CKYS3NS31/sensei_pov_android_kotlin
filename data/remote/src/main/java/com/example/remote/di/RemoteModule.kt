package com.example.remote.di

import com.example.remote.api.account.UserAccountApiService
import com.example.remote.api.pov.PoVApiService
import com.example.remote.api.user.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    /* provide api services */
    @Provides
    @Singleton
    fun providesUserAccountApiService(
        retrofit: Retrofit?
    ): UserAccountApiService = retrofit?.create(UserAccountApiService::class.java)!!

    @Provides
    @Singleton
    fun providesUserApiService(
        retrofit: Retrofit?
    ): UserApiService = retrofit?.create(UserApiService::class.java)!!

    @Provides
    @Singleton
    fun providesPoVApiService(
        retrofit: Retrofit?
    ): PoVApiService = retrofit?.create(PoVApiService::class.java)!!
}