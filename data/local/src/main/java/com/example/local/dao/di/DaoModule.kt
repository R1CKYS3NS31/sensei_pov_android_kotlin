package com.example.local.dao.di

import com.example.local.dao.account.UserAccountDao
import com.example.local.dao.pov.PoVDao
import com.example.local.dao.user.UserDao
import com.example.local.database.PoVDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    /* provide dao */
    @Provides
    fun providesUserAccountDao(
        database: PoVDatabase
    ): UserAccountDao = database.userAccountDao()

    @Provides
    fun providesUserDao(
        database: PoVDatabase
    ): UserDao = database.userDao()

    @Provides
    fun providePoVDao(
        database: PoVDatabase
    ): PoVDao = database.povDao()
}