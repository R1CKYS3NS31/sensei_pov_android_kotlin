package com.example.local.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_account")
data class UserAccountEntity(
    val id: String,
    @ColumnInfo("first_name")
    val firstName: String? = null,
    @ColumnInfo("last_name")
    val lastName: String? = null,
    @PrimaryKey
    val email: String,
    val password: String,
    @ColumnInfo("photo_url")
    val photoUrl: String? = null,
    val createdAt: String,
    val updatedAt: String
)
