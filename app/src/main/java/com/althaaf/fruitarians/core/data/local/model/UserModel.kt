package com.althaaf.fruitarians.core.data.local.model

data class UserModel(
    val accessToken: String,
    val tokenType: String,
    val email: String,
    val name: String
)
