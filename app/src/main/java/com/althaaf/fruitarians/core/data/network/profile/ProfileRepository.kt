package com.althaaf.fruitarians.core.data.network.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.local.model.UserModel
import com.althaaf.fruitarians.core.data.network.authentication.AuthRepository
import com.althaaf.fruitarians.core.data.network.authentication.response.login.SentTokenResponse
import com.althaaf.fruitarians.core.data.network.home.HomeRepository
import com.althaaf.fruitarians.core.data.network.profile.logout.LogoutResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProfileRepository(private val apiService: ApiService, private val dataStore: UserPreference) {

    fun logout() : LiveData<ApiResult<LogoutResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val response = apiService.logout("${userSession.tokenType} ${userSession.accessToken}" )

            when(response.errors){
                false -> {
                    Log.d(TAG,"logoutUser: Logout Success")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "logoutUser: failed")
                    emit(ApiResult.Error(response.message))
                }
            }
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, LogoutResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun getUserSession(): LiveData<UserModel> {
        return dataStore.getUser().asLiveData()
    }

    suspend fun clearUserSession() {
        dataStore.clearUser()
    }

    companion object {
        private const val TAG = "ProfileRepository"

        @Volatile
        private var instance: ProfileRepository? = null
        fun getInstance(apiService: ApiService, dataStore: UserPreference): ProfileRepository =
            instance ?: synchronized(this) {
                instance ?: ProfileRepository(apiService, dataStore)
            }.also { instance = it }

    }
}