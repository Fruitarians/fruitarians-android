package com.althaaf.fruitarians.core.data.network.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.local.model.UserModel
import com.althaaf.fruitarians.core.data.network.authentication.request.login.LoginRequest
import com.althaaf.fruitarians.core.data.network.authentication.request.login.SentTokenRequest
import com.althaaf.fruitarians.core.data.network.authentication.request.login.VerifyPasswordRequest
import com.althaaf.fruitarians.core.data.network.authentication.request.register.RegisterRequest
import com.althaaf.fruitarians.core.data.network.authentication.response.login.LoginResponse
import com.althaaf.fruitarians.core.data.network.authentication.response.login.SentTokenResponse
import com.althaaf.fruitarians.core.data.network.authentication.response.login.VerifyPasswordResponse
import com.althaaf.fruitarians.core.data.network.authentication.response.register.RegisterResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class AuthRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreference
) {

    fun registerUser(registerRequest: RegisterRequest): LiveData<ApiResult<RegisterResponse>> =
        liveData {
            emit(ApiResult.Loading)
            try {
                val response = apiService.registerUser(registerRequest)

                when (response.errors) {
                    false -> {
                        Log.d(TAG, "registerUser: User Created")
                        emit(ApiResult.Success(response))
                    }

                    true -> {
                        Log.d(TAG, "registerUser: failed ${response.message}")
                        emit(ApiResult.Error(response.message))
                    }
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                if (errorBody != null) {
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, RegisterResponse::class.java)
                    val errorMessage = errorResponse.message
                    emit(ApiResult.Error(errorMessage))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }

    fun loginUser(loginRequest: LoginRequest): LiveData<ApiResult<LoginResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val response = apiService.loginUser(loginRequest)
            Log.d(TAG, response.toString())

            when(response.errors) {
                false -> {
                    if (response.data != null) {
                        dataStore.saveUser(
                            user = UserModel(
                                accessToken = response.data.accessToken,
                                tokenType = response.data.tokenType,
                                email = response.data.email,
                                name = response.data.name,
                                role = response.data.role
                            )
                        )
                    }
                    Log.d(TAG,"loginUser: Login Success")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "loginUser: failed")
                    emit(ApiResult.Error(response.message))
                }
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, LoginResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun sentToken(sentTokenRequest: SentTokenRequest) : LiveData<ApiResult<SentTokenResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val response = apiService.sentToken(sentTokenRequest)

            when(response.errors){
                false -> {
                    Log.d(TAG,"sentToken: Sent Token Success")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "sentToken: failed")
                    emit(ApiResult.Error(response.message))
                }
            }
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, SentTokenResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun verifyPassword(verifyPasswordRequest: VerifyPasswordRequest): LiveData<ApiResult<VerifyPasswordResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val response = apiService.verifyPassword(verifyPasswordRequest)

            when(response.errors){
                false -> {
                    Log.d(TAG,"verifyPassword: Verify Password Success")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "verifyPassword: failed")
                    emit(ApiResult.Error(response.message))
                }
            }
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, VerifyPasswordResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    companion object {
        private const val TAG = "AuthRepository"

        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(apiService: ApiService, dataStore: UserPreference): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, dataStore)
            }.also { instance = it }

    }
}