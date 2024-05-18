package com.example.data.data.di

import com.example.data.data.datasource.account.UserAccountDatasource
import com.example.data.data.datasource.user.UserDatasource
import com.example.data.data.repository.account.UserAccountRepository
import com.example.data.data.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatasourceModule {
    /* bind data sources */
    @Binds
    @Singleton
    fun bindsUserAccountRepository(
        userAccountRepository: UserAccountDatasource
    ): UserAccountRepository

    @Binds
    @Singleton
    fun bindsUserRepository(
        userRepository: UserDatasource
    ): UserRepository
}