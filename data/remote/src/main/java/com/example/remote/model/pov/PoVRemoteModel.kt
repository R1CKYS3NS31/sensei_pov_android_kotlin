package com.example.remote.model.pov

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PoVRemoteModel(
    @SerialName("_id")
    val id: String,
    val title: String,
    val subtitle: String,
    val points: String,
    val attachment: String? = null,
    val author: String,
    val createdAt: String,
    val updatedAt: String
)


@Serializable
data class NewPoVRemoteModel(
    val title: String,
    val subtitle: String = "",
    val points: String,
    val attachment: String? = null,
    val author: String
)
