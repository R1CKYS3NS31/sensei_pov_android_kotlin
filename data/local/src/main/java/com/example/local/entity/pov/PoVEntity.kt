package com.example.local.entity.pov

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.local.entity.user.UserEntity

@Entity(tableName = "pov")
data class PoVEntity(
    val id: String,
    @PrimaryKey
    val title: String,
    val points: String,
    val attachment: List<String> = emptyList(),
    val author: String,
    val createdAt: String,
    val updatedAt: String
)
