package com.example.data.data.model.pov

import com.example.data.data.model.user.User

data class PoV(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val points: String = "",
    val attachment: String = "",
    val author: User? = null,
    val createdAt: String,
    val updatedAt: String
)

data class NewPoV(
    val title: String = "",
    val subtitle: String = "",
    val points: String = "",
    val attachment: String = "",
    val author: User? = null
)
