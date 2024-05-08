package com.example.datastore

import kotlinx.coroutines.flow.Flow

interface UserAccountPreferencesRepository {
    suspend fun saveUserAccountCredential(id: String, password: String)
    fun getUserAccountCredentials(): Flow<Pair<String?, String?>>
    suspend fun saveToken(token: String?)
    fun getToken(): Flow<String?>
    suspend fun signout()
}