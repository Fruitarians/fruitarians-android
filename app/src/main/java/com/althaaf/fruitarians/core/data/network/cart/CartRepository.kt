package com.althaaf.fruitarians.core.data.network.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.network.dashboard.membership.GeneralMembershipResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CartRepository(private val apiService: ApiService, private val dataStore: UserPreference) {


    fun getCartUser(): LiveData<ApiResult<CartResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val response = apiService.getCartUser(token = "${userSession.tokenType} ${userSession.accessToken}")
            emit(ApiResult.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, GeneralMembershipResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun deleteFruitCart(idCart: String, idBuah: String): LiveData<ApiResult<GeneralMembershipResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val response = apiService.deleteFruitCart(token = "${userSession.tokenType} ${userSession.accessToken}", id_cart = idCart, id_buah = idBuah)
            emit(ApiResult.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, GeneralMembershipResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun deleteStoreCart(idCart: String): LiveData<ApiResult<GeneralMembershipResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val response = apiService.deleteStoreCart(token = "${userSession.tokenType} ${userSession.accessToken}", id_cart = idCart)
            emit(ApiResult.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, GeneralMembershipResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    companion object {
        @Volatile
        private var instance: CartRepository? = null
        fun getInstance(apiService: ApiService, dataStore: UserPreference): CartRepository =
            instance ?: synchronized(this) {
                instance ?: CartRepository(apiService, dataStore)
            }.also { instance = it }

    }
}