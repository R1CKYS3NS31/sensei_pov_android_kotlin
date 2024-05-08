package com.example.remote.retrofit.di

import com.example.datastore.UserAccountPreferencesRepository
import com.example.remote.retrofit.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {
    /* provide interceptors */
    @Provides
    @Singleton
    fun providesAuthInterceptor(
        userAccountPreferencesRepository: UserAccountPreferencesRepository
    ): AuthInterceptor = AuthInterceptor(userAccountPreferencesRepository)
}