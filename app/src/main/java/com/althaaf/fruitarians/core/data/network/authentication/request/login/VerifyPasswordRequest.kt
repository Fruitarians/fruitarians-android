package com.althaaf.fruitarians.core.data.network.authentication.request.login

data class VerifyPasswordRequest (
    val change_password_token: String,
    val password: String
)