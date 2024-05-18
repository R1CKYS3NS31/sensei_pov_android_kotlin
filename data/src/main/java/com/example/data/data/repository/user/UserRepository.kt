package com.example.data.data.repository.user

import com.example.data.common.result.PoVResult
import com.example.data.data.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(userId: String): Flow<PoVResult<User>>
    fun getAllUsers(): Flow<PoVResult<List<User>>>
}