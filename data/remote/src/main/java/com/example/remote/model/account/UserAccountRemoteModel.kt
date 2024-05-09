package com.example.remote.model.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAccountRemoteModel(
    @SerialName("_id")
    val id: String,
    val name: Name,
    val email: String,
    val password: String,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class Name(
    val first: String? = null,
    val last: String? = null
)

@Serializable
data class AuthResponseModel(
    val token: String,
    val userAccountRemoteModel: UserAccountRemoteModel
)

@Serializable
data class UserAccountSignUpRemoteModel(
    val name: Name,
    val email: String,
    val password: String
)

@Serializable
data class UserAccountSignInRemoteModel(
    val email: String,
    val password: String
)