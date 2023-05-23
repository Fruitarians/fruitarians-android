package com.althaaf.fruitarians.core.data.network.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.local.model.UserModel
import com.althaaf.fruitarians.core.data.network.authentication.AuthRepository
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService

class HomeRepository(private val apiService: ApiService, private val dataStore: UserPreference) {

    fun getUserSession(): LiveData<UserModel> {
        return dataStore.getUser().asLiveData()
    }

    companion object {
        private const val TAG = "HomeRepository"

        @Volatile
        private var instance: HomeRepository? = null
        fun getInstance(apiService: ApiService, dataStore: UserPreference): HomeRepository =
            instance ?: synchronized(this) {
                instance ?: HomeRepository(apiService, dataStore)
            }.also { instance = it }

    }
}