package com.althaaf.fruitarians.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.local.model.UserModel
import com.althaaf.fruitarians.core.data.network.home.HomeRepository
import com.althaaf.fruitarians.core.di.Injection
import com.althaaf.fruitarians.ui.splashscreen.SplashScreenViewModel
import com.althaaf.fruitarians.ui.splashscreen.ViewModelFactory

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    fun getUserSession(): LiveData<UserModel> {
        return homeRepository.getUserSession()
    }
}

