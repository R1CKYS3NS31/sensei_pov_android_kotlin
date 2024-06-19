package com.example.local.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_account")
data class UserAccountEntity(
    val id: String,
    @ColumnInfo("first_name")
    val firstName: String,
    @ColumnInfo("last_name")
    val lastName: String,
    @PrimaryKey
    val email: String,
    @ColumnInfo("photo_url")
    val photoUrl: String? = null,
    val createdAt: String,
    val updatedAt: String
)
