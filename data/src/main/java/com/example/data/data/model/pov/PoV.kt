package com.example.data.data.model.pov

import com.example.local.entity.pov.PoVEntity
import com.example.remote.model.pov.NewPoVRemoteModel
import com.example.remote.model.pov.PoVRemoteModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class PoV(
    val id: String = "",
    val title: String = "",
    val points: String = "",
    val attachment: String = "",
    val author: String = "", // userId
    val createdAt: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
    val updatedAt: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
)

data class NewPoV(
    val title: String = "",
    val points: String = "",
    val attachment: String = "",
    val author: String = ""
)

fun PoV.asNewPoV(): NewPoV = NewPoV(
    title = title,
    attachment = attachment,
    author = author,
    points = points
)

fun NewPoV.asPoV(): PoV = PoV(
    title = title,
    points = points,
    attachment = attachment,
    author = author,
)

/* local */
fun PoV.asEntity(): PoVEntity = PoVEntity(
    id = id,
    title = title,
    points = points,
    attachment = attachment,
    author = author,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun PoVEntity.asPoV(): PoV = PoV(
    id = id,
    title = title,
    points = points,
    attachment = attachment,
    author = author,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

/* remote */
fun NewPoV.asRemote() = NewPoVRemoteModel(
    title = title,
    points = points,
    attachment = attachment,
    author = author
)

fun PoVEntity.asRemote(): PoVRemoteModel = PoVRemoteModel(
    id = id,
    title = title,
    points = points,
    attachment = attachment,
    author = author,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun PoVRemoteModel.asPoV(): PoV = PoV(
    id = id,
    title = title,
    points = points,
    attachment = attachment,
    author = author,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun PoV.asRemote(): PoVRemoteModel = PoVRemoteModel(
    id = id,
    title = title,
    points = points,
    attachment = attachment,
    author = author,
    createdAt = createdAt,
    updatedAt = updatedAt,
)