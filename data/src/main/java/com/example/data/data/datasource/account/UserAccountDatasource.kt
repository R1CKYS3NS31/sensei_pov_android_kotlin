package com.example.data.data.datasource.account

import com.example.local.dao.account.UserAccountDao
import com.example.remote.api.account.UserAccountApiService
import javax.inject.Inject

class UserAccountDatasource @Inject constructor(
    private val userAccountDao: UserAccountDao, // local datasource
    private val userAccountApiService: UserAccountApiService, // remote datasource
    @Dispatcher
) {
}