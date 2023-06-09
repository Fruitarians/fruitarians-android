package com.althaaf.fruitarians.ui.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.network.profile.ProfileRepository
import com.althaaf.fruitarians.core.data.network.profile.changepassword.ChangePasswordRequest
import com.althaaf.fruitarians.core.data.network.profile.changepassword.ChangePasswordResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class ChangePasswordViewModel(private val profileRepository: ProfileRepository): ViewModel() {

    fun changePasswordUser(changePasswordRequest: ChangePasswordRequest): LiveData<ApiResult<ChangePasswordResponse>> {
        return profileRepository.changePasswordUser(changePasswordRequest)
    }

}