package com.example.remote.model.user

import kotlinx.serialization.SerialName

data class UserRemoteModel(
    @SerialName("_id")
    val id: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val createdAt: String,
    val updatedAt: String,
)

