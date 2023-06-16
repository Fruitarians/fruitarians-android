package com.althaaf.fruitarians.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.network.authentication.AuthRepository
import com.althaaf.fruitarians.core.data.network.authentication.request.login.SentTokenRequest
import com.althaaf.fruitarians.core.data.network.authentication.response.login.SentTokenResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class ForgotPasswordViewModel(private val authRepository: AuthRepository): ViewModel() {

    fun sentToken(sentTokenRequest: SentTokenRequest) : LiveData<ApiResult<SentTokenResponse>> {
        return authRepository.sentToken(sentTokenRequest)
    }
}