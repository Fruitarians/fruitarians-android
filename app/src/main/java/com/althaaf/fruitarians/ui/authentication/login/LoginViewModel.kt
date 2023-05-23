package com.althaaf.fruitarians.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.network.authentication.AuthRepository
import com.althaaf.fruitarians.core.data.network.authentication.request.login.LoginRequest
import com.althaaf.fruitarians.core.data.network.authentication.response.login.LoginResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {

    fun loginUser(loginRequest: LoginRequest): LiveData<ApiResult<LoginResponse>> {
        return authRepository.loginUser(loginRequest)
    }

}