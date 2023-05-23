package com.althaaf.fruitarians.core.data.network.retrofit

import com.althaaf.fruitarians.core.data.network.authentication.request.login.LoginRequest
import com.althaaf.fruitarians.core.data.network.authentication.request.login.SentTokenRequest
import com.althaaf.fruitarians.core.data.network.authentication.request.login.VerifyPasswordRequest
import com.althaaf.fruitarians.core.data.network.authentication.request.register.RegisterRequest
import com.althaaf.fruitarians.core.data.network.authentication.response.login.LoginResponse
import com.althaaf.fruitarians.core.data.network.authentication.response.login.SentTokenResponse
import com.althaaf.fruitarians.core.data.network.authentication.response.login.VerifyPasswordResponse
import com.althaaf.fruitarians.core.data.network.authentication.response.register.RegisterResponse
import com.althaaf.fruitarians.core.data.network.profile.logout.LogoutResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {

    @POST("auth/signup")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ) : RegisterResponse

    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ) : LoginResponse

    @POST("user/forget_password")
    suspend fun sentToken(
        @Body loginRequest: SentTokenRequest
    ) : SentTokenResponse

    @PATCH("user/forget_password")
    suspend fun verifyPassword(
        @Body loginRequest: VerifyPasswordRequest
    ) : VerifyPasswordResponse

    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String,
    ) : LogoutResponse

}