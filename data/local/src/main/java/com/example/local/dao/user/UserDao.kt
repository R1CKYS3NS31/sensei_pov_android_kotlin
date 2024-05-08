package com.example.local.dao.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.local.entity.user.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(userEntity: UserEntity)

    @Query("select * from user where id =:id")
    fun getUser(id: String): Flow<UserEntity>

    @Query("select * from user")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)
}