package com.althaaf.fruitarians.ui.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.network.authentication.AuthRepository
import com.althaaf.fruitarians.core.data.network.authentication.request.register.RegisterRequest
import com.althaaf.fruitarians.core.data.network.authentication.response.register.RegisterResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class RegisterViewModel(private val authRepository: AuthRepository): ViewModel(){

    fun registerUser(registerRequest: RegisterRequest): LiveData<ApiResult<RegisterResponse>> {
        return authRepository.registerUser(registerRequest)
    }
}