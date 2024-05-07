package com.example.local.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class UserEntity(
    val id: String?,
    @ColumnInfo("first_name")
    val firstName: String? = null,
    @ColumnInfo("last_name")
    val lastName: String? = null,
    @PrimaryKey
    val email: String,
    @ColumnInfo("photo_url")
    val photoUrl: String? = null,
    val createdAt: String,
    val updatedAt: String
)
