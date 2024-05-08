package com.example.datastore.di

import android.content.Context
import com.example.datastore.UserAccountPreferences
import com.example.datastore.UserAccountPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {
    @Provides
    @Singleton
    fun providesUserAccountPreferences(
        @ApplicationContext
        context: Context
    ): UserAccountPreferencesRepository = UserAccountPreferences(context)
}