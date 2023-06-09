package com.althaaf.fruitarians.core.data.network.profile

import androidx.datastore.preferences.protobuf.Api
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.network.profile.addproduct.AddProductResponse
import com.althaaf.fruitarians.core.data.network.profile.mystore.TokoBuahPagingSource
import com.althaaf.fruitarians.core.data.network.profile.myvendor.SubsVendorPagingSource
import com.althaaf.fruitarians.core.data.network.profile.myvendor.VendorSubsItem
import com.althaaf.fruitarians.core.data.network.profile.myvendor.addsubs.AddSubsRequest
import com.althaaf.fruitarians.core.data.network.profile.myvendor.addsubs.AddSubsResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MyVendorRepository(private val apiService: ApiService, private val dataStore: UserPreference) {

    fun getAllSubsVendor(): LiveData<PagingData<VendorSubsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                SubsVendorPagingSource(apiService, dataStore)
            }
        ).liveData
    }

    fun addSubVendor(addSubsRequest: AddSubsRequest): LiveData<ApiResult<AddSubsResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val response = apiService.addSubsVendor(token = "${userSession.tokenType} ${userSession.accessToken}", addSubsRequest = addSubsRequest)
            emit(ApiResult.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, AddSubsResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun deleteSubVendor(id_subs: String): LiveData<ApiResult<AddSubsResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val response = apiService.deleteSubVendor(token = "${userSession.tokenType} ${userSession.accessToken}", id_subs = id_subs)
            emit(ApiResult.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, AddSubsResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun changeSubStatus(id_subs: String): LiveData<ApiResult<AddSubsResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val response = apiService.changeSubStatus(token = "${userSession.tokenType} ${userSession.accessToken}", id_subs = id_subs, delivered = "delivered")
            emit(ApiResult.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, AddSubsResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    companion object {
        private const val TAG = "MyVendorRepository"
        @Volatile
        private var instance: MyVendorRepository? = null
        fun getInstance(apiService: ApiService, dataStore: UserPreference): MyVendorRepository =
            instance ?: synchronized(this) {
                instance ?: MyVendorRepository(apiService, dataStore)
            }.also { instance = it }

    }
}