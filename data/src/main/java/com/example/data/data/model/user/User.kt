package com.example.data.data.model.user

import com.example.data.data.model.account.Name
import com.example.data.data.model.account.asName
import com.example.data.data.model.account.asRemoteName
import com.example.local.entity.user.UserEntity
import com.example.remote.model.user.UserRemoteModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class User(
    val id: String = "",
    val name: Name = Name(),
    val email: String = "",
    val photoUrl: String? = null,
    val createdAt: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
    val updatedAt: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
) {
    val fullName: String
        get() = "${name.first?.replaceFirstChar { it.uppercase() }} ${name.last?.replaceFirstChar { it.uppercase() }}"
}

fun User.asUserEntity(): UserEntity =
    UserEntity(
        id = id,
        firstName = name.first,
        lastName = name.last,
        email = email,
        photoUrl = photoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun UserEntity.asUser(): User =
    User(
        id = id,
        name = Name(firstName, lastName),
        email = email,
        photoUrl = photoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )


fun UserRemoteModel.asUserEntity(): UserEntity =
    UserEntity(
        id = id,
        firstName = name.first,
        lastName = name.last,
        email = email,
        photoUrl = photoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun UserRemoteModel.asUser(): User =
    User(
        id = id,
        name = name.asName(),
        email = email,
        photoUrl = photoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun User.asRemote(): UserRemoteModel =
    UserRemoteModel(
        id = id,
        name = name.asRemoteName(),
        email = email,
        photoUrl = photoUrl,
        createdAt = createdAt, // remove
        updatedAt = updatedAt
    )

