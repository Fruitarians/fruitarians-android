package com.althaaf.fruitarians.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.network.authentication.AuthRepository
import com.althaaf.fruitarians.core.data.network.cart.CartRepository
import com.althaaf.fruitarians.core.data.network.dashboard.DashboardRepository
import com.althaaf.fruitarians.core.data.network.fruitscan.FruitScanRepository
import com.althaaf.fruitarians.core.data.network.profile.MyStoreRepository
import com.althaaf.fruitarians.core.data.network.profile.MyVendorRepository
import com.althaaf.fruitarians.core.data.network.profile.ProfileRepository
import com.althaaf.fruitarians.core.data.network.retrofit.ApiConfig

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {

    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreference.getInstance(context.dataStore)
        return AuthRepository.getInstance(apiService, dataStore)
    }

    fun provideFruitScanRepository(): FruitScanRepository {
        val apiService = ApiConfig.getApiServiceScan()
        return FruitScanRepository.getInstance(apiService)
    }

    fun provideHomeRepository(context: Context): DashboardRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreference.getInstance(context.dataStore)
        return DashboardRepository.getInstance(apiService, dataStore)
    }

    fun provideProfileRepository(context: Context): ProfileRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreference.getInstance(context.dataStore)
        return ProfileRepository.getInstance(apiService, dataStore)
    }

    fun provideMyStoreRepository(context: Context): MyStoreRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreference.getInstance(context.dataStore)
        return MyStoreRepository.getInstance(apiService, dataStore)
    }

    fun provideMyVendorRepository(context: Context): MyVendorRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreference.getInstance(context.dataStore)
        return MyVendorRepository.getInstance(apiService, dataStore)
    }

    fun provideDataStore(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }

    fun provideCartRepository(context: Context): CartRepository {
        val apiService = ApiConfig.getApiService()
        val dataStore = UserPreference.getInstance(context.dataStore)
        return CartRepository.getInstance(apiService, dataStore)
    }
}