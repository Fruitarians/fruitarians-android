package com.althaaf.fruitarians.core.data.network.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.local.model.UserModel
import com.althaaf.fruitarians.core.data.network.dashboard.DashboardRepository
import com.althaaf.fruitarians.core.data.network.dashboard.DataUserResponse
import com.althaaf.fruitarians.core.data.network.profile.changepassword.ChangePasswordRequest
import com.althaaf.fruitarians.core.data.network.profile.changepassword.ChangePasswordResponse
import com.althaaf.fruitarians.core.data.network.profile.editprofile.EditProfileRequest
import com.althaaf.fruitarians.core.data.network.profile.editprofile.EditProfileResponse
import com.althaaf.fruitarians.core.data.network.profile.logout.LogoutResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    fun editProfileUser(
        name: RequestBody,
        negara: RequestBody,
        kota: RequestBody,
        deskripsi_alamat: RequestBody,
        telepon: RequestBody,
        deskripsi: RequestBody,
        jam_buka: RequestBody,
        jam_tutup: RequestBody,
        hari_buka_awal: RequestBody,
        hari_buka_akhir: RequestBody,
        file: MultipartBody.Part?
    ): LiveData<ApiResult<EditProfileResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val response = apiService.editDataUser(
                token = "${userSession.tokenType} ${userSession.accessToken}",
                name = name,
                negara = negara,
                kota = kota,
                deskripsi_alamat = deskripsi_alamat,
                telepon = telepon ,
                deskripsi = deskripsi,
                jam_buka = jam_buka,
                jam_tutup = jam_tutup,
                hari_buka_awal = hari_buka_awal,
                hari_buka_akhir = hari_buka_akhir,
                file = file
            )

            when (response.errors) {
                false -> {
                    Log.d(TAG, "EditDataUser: Success Edit Data User")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "EditDataUser: failed ${response.message}")
                    emit(ApiResult.Error(response.message))
                }
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, EditProfileResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun getAllDataUser(): LiveData<ApiResult<DataUserResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val response = apiService.getAllDataUser(token = "${userSession.tokenType} ${userSession.accessToken}")

            when (response.errors) {
                false -> {
                    Log.d(TAG, "GetAllDataUser: Success Get All Data User")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "GetAllDataUser: failed ${response.message}")
                    emit(ApiResult.Error(response.message))
                }
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, DataUserResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun changePasswordUser(changePasswordRequest: ChangePasswordRequest) : LiveData<ApiResult<ChangePasswordResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val response = apiService.changePasswordUser(token = "${userSession.tokenType} ${userSession.accessToken}", changePasswordRequest)

            when (response.errors) {
                false -> {
                    Log.d(TAG, "ChangePasswordUser: Success Change Password User")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "ChangePasswordUser: failed ${response.message}")
                    emit(ApiResult.Error(response.message))
                }
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ChangePasswordResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
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