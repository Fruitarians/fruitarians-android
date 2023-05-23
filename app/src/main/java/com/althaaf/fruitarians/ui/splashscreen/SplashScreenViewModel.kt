package com.althaaf.fruitarians.ui.splashscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.di.Injection
import com.althaaf.fruitarians.core.helper.AuthViewModelFactory

class SplashScreenViewModel(private val dataStore: UserPreference): ViewModel() {
    fun getUserSession() = dataStore.getUser().asLiveData()
}

class ViewModelFactory(private val dataStore: UserPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideDataStore(context))
            }.also { instance = it }
    }
}