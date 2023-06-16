package com.althaaf.fruitarians.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.althaaf.fruitarians.core.data.network.dashboard.DataUserResponse
import com.althaaf.fruitarians.core.data.network.profile.ProfileRepository
import com.althaaf.fruitarians.core.data.network.profile.logout.LogoutResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository): ViewModel() {

    fun logout(): LiveData<ApiResult<LogoutResponse>> {
        return  profileRepository.logout()
    }

    fun getAllDataUser(): LiveData<ApiResult<DataUserResponse>> {
        return profileRepository.getAllDataUser()
    }

    fun clearUserSession() {
        viewModelScope.launch {
            profileRepository.clearUserSession()
        }
    }
}