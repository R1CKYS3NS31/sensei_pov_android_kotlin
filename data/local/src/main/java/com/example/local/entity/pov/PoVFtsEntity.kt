package com.example.local.entity.pov

import androidx.room.Entity
import androidx.room.Fts4

@Entity(tableName = "povfts")
@Fts4
data class PoVFtsEntity(
    val id: String,
    val title: String,
    val points: String
)

fun PoVEntity.asFtsEntity() = PoVFtsEntity(
    id = id,
    points = points,
    title = title,
)