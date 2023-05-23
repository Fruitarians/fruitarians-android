package com.althaaf.fruitarians.core.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.core.data.network.authentication.AuthRepository
import com.althaaf.fruitarians.core.di.Injection
import com.althaaf.fruitarians.ui.authentication.login.ForgotPasswordVerifyViewModel
import com.althaaf.fruitarians.ui.authentication.login.ForgotPasswordViewModel
import com.althaaf.fruitarians.ui.authentication.login.LoginViewModel
import com.althaaf.fruitarians.ui.authentication.register.RegisterViewModel

class AuthViewModelFactory private constructor(private val authRepository: AuthRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return  RegisterViewModel(authRepository) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return  LoginViewModel(authRepository) as T
        }

        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            return ForgotPasswordViewModel(authRepository) as T
        }

        if (modelClass.isAssignableFrom(ForgotPasswordVerifyViewModel::class.java)) {
            return ForgotPasswordVerifyViewModel(authRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null
        fun getInstance(context: Context): AuthViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: AuthViewModelFactory(Injection.provideAuthRepository(context))
            }.also { instance = it }
    }
}