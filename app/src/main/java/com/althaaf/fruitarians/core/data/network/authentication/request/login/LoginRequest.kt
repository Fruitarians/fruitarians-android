package com.althaaf.fruitarians.core.data.network.authentication.request.login

data class LoginRequest(
    val email: String,
    val password: String,
)