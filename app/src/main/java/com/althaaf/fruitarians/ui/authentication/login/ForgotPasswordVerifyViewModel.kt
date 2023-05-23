package com.althaaf.fruitarians.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.network.authentication.AuthRepository
import com.althaaf.fruitarians.core.data.network.authentication.request.login.VerifyPasswordRequest
import com.althaaf.fruitarians.core.data.network.authentication.response.login.VerifyPasswordResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class ForgotPasswordVerifyViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun verifyPassword(verifyPasswordRequest: VerifyPasswordRequest) : LiveData<ApiResult<VerifyPasswordResponse>> {
        return authRepository.verifyPassword(verifyPasswordRequest)
    }
}