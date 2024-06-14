package com.example.local.entity.pov

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.local.entity.user.UserEntity

@Entity(tableName = "pov")
data class PoVEntity(
    val id: String,
    @PrimaryKey
    val title: String,
    val subtitle: String,
    val points: String,
    val attachment: String,
    val author: UserEntity? = null,
    val createdAt: String,
    val updatedAt: String
)
