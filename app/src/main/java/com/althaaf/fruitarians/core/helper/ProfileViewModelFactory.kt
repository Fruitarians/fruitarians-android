package com.althaaf.fruitarians.core.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.core.data.network.profile.ProfileRepository
import com.althaaf.fruitarians.core.di.Injection
import com.althaaf.fruitarians.ui.changepassword.ChangePasswordViewModel
import com.althaaf.fruitarians.ui.edit_profile.EditProfileViewModel
import com.althaaf.fruitarians.ui.profile.ProfileViewModel

class ProfileViewModelFactory private constructor(private val profileRepository: ProfileRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return  ProfileViewModel(profileRepository) as T
        }

        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return  EditProfileViewModel(profileRepository) as T
        }

        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            return ChangePasswordViewModel(profileRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ProfileViewModelFactory? = null
        fun getInstance(context: Context): ProfileViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ProfileViewModelFactory(Injection.provideProfileRepository(context))
            }.also { instance = it }
    }
}