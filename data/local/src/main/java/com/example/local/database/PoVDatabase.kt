package com.example.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.local.dao.account.UserAccountDao
import com.example.local.dao.pov.PoVDao
import com.example.local.dao.pov.PoVFtsDao
import com.example.local.dao.user.UserDao
import com.example.local.entity.account.UserAccountEntity
import com.example.local.entity.pov.PoVEntity
import com.example.local.entity.pov.PoVFtsEntity
import com.example.local.entity.user.UserEntity
import com.example.local.util.AttachmentsConverter

@Database(
    entities = [
        UserAccountEntity::class,
        UserEntity::class,
        PoVEntity::class,
        PoVFtsEntity::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(AttachmentsConverter::class)
internal abstract class PoVDatabase : RoomDatabase() {
    /* Dao */
    abstract fun userAccountDao(): UserAccountDao
    abstract fun userDao(): UserDao
    abstract fun povDao(): PoVDao
    abstract fun povFtsDao(): PoVFtsDao
}