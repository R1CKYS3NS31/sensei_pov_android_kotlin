package com.example.data.data.di

import com.example.data.data.datasource.account.UserAccountDatasource
import com.example.data.data.datasource.pov.PoVDatasource
import com.example.data.data.datasource.search.SearchDatasource
import com.example.data.data.datasource.user.UserDatasource
import com.example.data.data.repository.account.UserAccountRepository
import com.example.data.data.repository.pov.PoVRepository
import com.example.data.data.repository.search.SearchRepository
import com.example.data.data.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DatasourceModule {
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

    @Binds
    @Singleton
    fun bindsPoVRepository(
        poVRepository: PoVDatasource
    ): PoVRepository

    @Binds
    @Singleton
    fun bindsSearchRepository(
        searchRepository: SearchDatasource
    ): SearchRepository
}