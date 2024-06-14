package com.example.remote.model.user

import com.example.remote.model.account.Name
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRemoteModel(
    @SerialName("_id")
    val id: String,
    val name: Name,
    val email: String,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val createdAt: String,
    val updatedAt: String,
)