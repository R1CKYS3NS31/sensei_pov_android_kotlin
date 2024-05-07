package com.example.local.dao.account

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.local.entity.account.UserAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAccountDao {

    /* sign up && sign in*/
    @Insert
    suspend fun insertUserAccount(userAccountEntity: UserAccountEntity)

    @Upsert
    suspend fun upsertUserAccount(userAccountEntity: UserAccountEntity)

    @Query("select  * from user_account where id =:id")
    fun getUserAccount(id: String): Flow<UserAccountEntity>

    @Query("select * from user_account")
    fun getAllUserAccounts(): Flow<List<UserAccountEntity>>

    @Update
    suspend fun updateUserAccount(userAccountEntity: UserAccountEntity)

    @Delete
    suspend fun deleteUserAccount(userAccountEntity: UserAccountEntity)
}