package com.example.data.data.model.pov

import com.example.data.data.model.user.User
import com.example.data.data.model.user.asRemote
import com.example.data.data.model.user.asUser
import com.example.data.data.model.user.asUserEntity
import com.example.local.entity.pov.PoVEntity
import com.example.remote.model.pov.NewPoVRemoteModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class PoV(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val points: String = "",
    val attachment: String? = null,
    val author: User? = null,
    val createdAt: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
    val updatedAt: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
)

data class NewPoV(
    val title: String = "",
    val subtitle: String = "",
    val points: String = "",
    val attachment: String? = null,
    val author: User? = null
)

fun PoV.asPoVEntity(): PoVEntity = PoVEntity(
    id = id,
    title = title,
    subtitle = subtitle,
    points = points,
    attachment = attachment,
    author = author?.asUserEntity(),
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun PoVEntity.asPoV(): PoV = PoV(
    id = id,
    title = title,
    subtitle = subtitle,
    points = points,
    attachment = attachment,
    author = author?.asUser(),
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun PoV.asNewPoV(): NewPoV = NewPoV(
    title = title,
    subtitle = subtitle,
    attachment = attachment,
    author = author,
    points = points
)

fun NewPoV.asPoV(): PoV = PoV(
    title = title,
    subtitle = subtitle,
    points = points,
    attachment = attachment,
    author = author,
)

// remote
fun NewPoV.asNewPoVRemote() = NewPoVRemoteModel(
    title = title,
    subtitle = subtitle,
    points = points,
    attachment = attachment,
    author = author?.asRemote()
)