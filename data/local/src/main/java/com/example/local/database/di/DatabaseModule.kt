package com.example.local.database.di

import android.content.Context
import androidx.room.Room
import com.example.local.database.PoVDatabase
import com.example.local.util.Config.ROOM_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    /* provide database */
    @Provides
    @Singleton
    fun providesPoVDatabase(
        @ApplicationContext
        context: Context
    ): PoVDatabase = Room.databaseBuilder(
        context, PoVDatabase::class.java,
        name = ROOM_DATABASE
    ).build()
}