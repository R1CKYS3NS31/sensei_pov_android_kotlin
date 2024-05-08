package com.example.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.local.dao.account.UserAccountDao
import com.example.local.dao.user.UserDao
import com.example.local.entity.account.UserAccountEntity
import com.example.local.entity.user.UserEntity

@Database(
    entities = [UserAccountEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PoVDatabase : RoomDatabase() {
    /* Dao */
    abstract fun userAccountDao(): UserAccountDao
    abstract fun userDao(): UserDao
}