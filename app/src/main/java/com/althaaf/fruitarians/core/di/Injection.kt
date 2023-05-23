package com.althaaf.fruitarians.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.network.authentication.AuthRepository
import com.althaaf.fruitarians.core.data.network.home.HomeRepository
import com.althaaf.fruitarians.core.data.network.profile.ProfileRepository
import com.althaaf.fruitarians.core.data.network.retrofit.ApiConfig

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {

    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreference.getInstance(context.dataStore)
        return AuthRepository.getInstance(apiService, dataStore)
    }

    fun provideHomeRepository(context: Context): HomeRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreference.getInstance(context.dataStore)
        return HomeRepository.getInstance(apiService, dataStore)
    }

    fun provideProfileRepository(context: Context): ProfileRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreference.getInstance(context.dataStore)
        return ProfileRepository.getInstance(apiService, dataStore)
    }

    fun provideDataStore(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }
}