package com.althaaf.fruitarians.core.data.network.profile

import android.util.Log
import androidx.datastore.preferences.protobuf.Api
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.network.profile.addproduct.AddProductResponse
import com.althaaf.fruitarians.core.data.network.profile.deleteproduct.DeleteProductResponse
import com.althaaf.fruitarians.core.data.network.profile.deleteproduct.deleteRequest
import com.althaaf.fruitarians.core.data.network.profile.mystore.BuahItem
import com.althaaf.fruitarians.core.data.network.profile.mystore.TokoBuahPagingSource
import com.althaaf.fruitarians.core.data.network.profile.updateproduct.UpdateProductResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class MyStoreRepository(private val apiService: ApiService, private val dataStore: UserPreference) {

    fun getTokoBuah(): LiveData<PagingData<BuahItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                TokoBuahPagingSource(apiService, dataStore)
            }
        ).liveData
    }

    fun addProductBuah(
        name: RequestBody,
        harga: Int,
        satuan: RequestBody,
        deskripsi: RequestBody,
        stok: Int,
        file: MultipartBody.Part
    ): LiveData<ApiResult<AddProductResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val response = apiService.addProductBuah(
                token = "${userSession.tokenType} ${userSession.accessToken}",
                name = name,
                harga = harga,
                satuan = satuan,
                deskripsi = deskripsi,
                stok = stok,
                file = file
            )

            when (response.errors) {
                false -> {
                    Log.d(TAG, "AddNewProduct: Success Add New Productr")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "AddNewProduct: failed ${response.message}")
                    emit(ApiResult.Error(response.message))
                }
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, AddProductResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun updateProductBuah(
        buahId: RequestBody,
        name: RequestBody,
        harga: Int,
        satuan: RequestBody,
        stok: Int,
        deskripsi: RequestBody,
        file: MultipartBody.Part?
    ): LiveData<ApiResult<UpdateProductResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val response = apiService.updateProductBuah(
                token = "${userSession.tokenType} ${userSession.accessToken}",
                buahId = buahId,
                name = name,
                harga = harga,
                satuan = satuan,
                stok = stok,
                deskripsi = deskripsi,
                file = file
            )

            when (response.errors) {
                false -> {
                    Log.d(TAG, "UpdateNewProduct: Success Update Product")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "UpdateNewProduct: failed ${response.message}")
                    emit(ApiResult.Error(response.message))
                }
            }

        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, UpdateProductResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun deleteProductBuah(
        idBuah: String
    ): LiveData<ApiResult<DeleteProductResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val response = apiService.deleteProductBuah(
                token = "${userSession.tokenType} ${userSession.accessToken}",
                idBuah
            )

            when (response.errors) {
                false -> {
                    Log.d(TAG, "DeleteProduct: Success Delete Product")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "DeleteProduct: failed ${response.message}")
                    emit(ApiResult.Error(response.message))
                }
            }

        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, DeleteProductResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    companion object {
        private const val TAG = "MyStoreRepository"
        @Volatile
        private var instance: MyStoreRepository? = null
        fun getInstance(apiService: ApiService, dataStore: UserPreference): MyStoreRepository =
            instance ?: synchronized(this) {
                instance ?: MyStoreRepository(apiService, dataStore)
            }.also { instance = it }

    }
}