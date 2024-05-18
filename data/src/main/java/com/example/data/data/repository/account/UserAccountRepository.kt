package com.example.data.data.repository.account

import com.example.data.common.result.PoVResult
import com.example.data.data.model.account.UserAccount
import com.example.data.data.model.account.UserAccountSignIn
import com.example.data.data.model.account.UserAccountSignUp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserAccountRepository {
    val authToken: StateFlow<String?>
    //    val isOnline: StateFlow<Boolean>
    //    val userCredentials: StateFlow<Pair<String?, String?>>
    fun getAllUserAccounts(): Flow<PoVResult<List<UserAccount>>>
    suspend fun getUserCredentials(): Flow<Pair<String?, String?>>
    suspend fun signUp(userAccountSignUp: UserAccountSignUp): Flow<PoVResult<UserAccount>>
    suspend fun signIn(userAccountSignIn: UserAccountSignIn): Flow<PoVResult<UserAccount>>
    suspend fun signOut(userAccount: UserAccount): Flow<PoVResult<Unit>>
    suspend fun getUserAccount(userAccountId: String): Flow<PoVResult<UserAccount>>
    suspend fun updateUserAccount(userAccount: UserAccount): Flow<PoVResult<UserAccount>>
    suspend fun deleteUserAccount(userAccount: UserAccount): Flow<PoVResult<Unit>>
}