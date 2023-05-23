package com.althaaf.fruitarians.core.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.core.data.network.authentication.AuthRepository
import com.althaaf.fruitarians.core.data.network.home.HomeRepository
import com.althaaf.fruitarians.core.di.Injection
import com.althaaf.fruitarians.ui.authentication.login.ForgotPasswordVerifyViewModel
import com.althaaf.fruitarians.ui.authentication.login.ForgotPasswordViewModel
import com.althaaf.fruitarians.ui.authentication.login.LoginViewModel
import com.althaaf.fruitarians.ui.authentication.register.RegisterViewModel
import com.althaaf.fruitarians.ui.home.HomeViewModel

class HomeViewModelFactory private constructor(private val homeRepository: HomeRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return  HomeViewModel(homeRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null
        fun getInstance(context: Context): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeViewModelFactory(Injection.provideHomeRepository(context))
            }.also { instance = it }
    }

}