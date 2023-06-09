package com.althaaf.fruitarians.core.data.network.fruitscan

import androidx.datastore.preferences.protobuf.Api
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import okhttp3.MultipartBody

class FruitScanRepository(private val apiService: ApiService) {

    fun fruitScanDetection(image_data: MultipartBody.Part): LiveData<ApiResult<FruitScanResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val response = apiService.uploadScanDetection(image_data)
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            emit(ApiResult.Error("Error invalid image or $e"))
        }
    }

    companion object {
        private const val TAG = "FruitScanRepository"
        @Volatile
        private var instance: FruitScanRepository? = null
        fun getInstance(apiService: ApiService) : FruitScanRepository =
            instance ?: synchronized(this) {
                instance ?: FruitScanRepository(apiService)
            }.also { instance = it }
    }
}