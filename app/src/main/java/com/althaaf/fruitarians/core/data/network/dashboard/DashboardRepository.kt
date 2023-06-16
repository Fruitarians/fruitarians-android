package com.althaaf.fruitarians.core.data.network.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.local.model.UserModel
import com.althaaf.fruitarians.core.data.network.cart.CartRequest
import com.althaaf.fruitarians.core.data.network.dashboard.article.ArticleResponse
import com.althaaf.fruitarians.core.data.network.dashboard.article.DetailArticleResponse
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.FruitStorePagingSource
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.FruitStoreResponse
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detail.BuahItem
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detail.DetailFruitStorePagingSource
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detailfruit.DetailSpecificFruitResponse
import com.althaaf.fruitarians.core.data.network.dashboard.fruitvariant.BuahResponse
import com.althaaf.fruitarians.core.data.network.dashboard.fruitvariant.FruitVariantPagingSource
import com.althaaf.fruitarians.core.data.network.dashboard.membership.GeneralMembershipResponse
import com.althaaf.fruitarians.core.data.network.dashboard.membership.UserIsMemberResponse
import com.althaaf.fruitarians.core.data.network.dashboard.membership.UserMembershipPagingSource
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DashboardRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreference
) {

    fun getUserSession(): LiveData<UserModel> {
        return dataStore.getUser().asLiveData()
    }

    fun getAllDataUser(): LiveData<ApiResult<DataUserResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val dataUserSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val response =
                apiService.getAllDataUser("${dataUserSession.tokenType} ${dataUserSession.accessToken}")

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

    fun getFruitStore(): LiveData<PagingData<DataItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                FruitStorePagingSource(dataStore, apiService)
            }
        ).liveData
    }

    fun getDetailFruitStore(id: String): LiveData<PagingData<BuahItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                DetailFruitStorePagingSource(dataStore, apiService, id)
            }
        ).liveData
    }

    fun getSpecificFruit(idToko: String, idBuah: String): LiveData<ApiResult<DetailSpecificFruitResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val dataUserSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val response = apiService.getDetailTokoSpecificBuah(
                "${dataUserSession.tokenType} ${dataUserSession.accessToken}", idToko, idBuah
            )

            Log.d(TAG, "GetSpecificFruit: Success Get Specific Fruit")
            emit(ApiResult.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, DetailSpecificFruitResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun getOneFruitStore(): LiveData<ApiResult<FruitStoreResponse<DataItem>>> = liveData {
        emit(ApiResult.Loading)
        try {
            val dataUserSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val response = apiService.getOneListRole(
                token = "${dataUserSession.tokenType} ${dataUserSession.accessToken}",
                role = "toko",
                card = true
            )

            when (response.errors) {
                false -> {
                    Log.d(TAG, "getDataCard: Get Data Card Success")
                    emit(ApiResult.Success(response))
                }

                true -> {
                    Log.d(TAG, "getDataCard: failed")
                    emit(ApiResult.Error("Get data card failed"))
                }
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Get data card gagal, ${e.message}"))
        }
    }

    fun getArticles(): LiveData<ApiResult<ArticleResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val response = apiService.getArticles()

            Log.d(TAG, "GetAllArticles: Success Get All Articles")
            emit(ApiResult.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ArticleResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun getDetailArticles(id: Int): LiveData<ApiResult<DetailArticleResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val response = apiService.getArticleById(id, true)
            emit(ApiResult.Success(response))
        } catch (e: Exception) {
            emit(ApiResult.Error("Error $e"))
            Log.e(TAG, "$e")
        }
    }

    fun getAllBuah(q: String?): LiveData<PagingData<com.althaaf.fruitarians.core.data.network.dashboard.fruitvariant.DataItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                FruitVariantPagingSource(dataStore, apiService, q)
            }
        ).liveData
    }

    fun getUserMembership(): LiveData<PagingData<DataItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                UserMembershipPagingSource(dataStore, apiService)
            }
        ).liveData
    }

    fun getUserisMember(idToko: String): LiveData<ApiResult<UserIsMemberResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val response = apiService.getUserIsMember(token = "${userSession.tokenType} ${userSession.accessToken}", bookmark_userId = idToko)
            emit(ApiResult.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, UserIsMemberResponse::class.java)
                val errorMessage = errorResponse.message
                emit(ApiResult.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    fun addUserMembership(idToko: String): LiveData<ApiResult<GeneralMembershipResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val response = apiService.addUserMembership(token = "${userSession.tokenType} ${userSession.accessToken}", bookmark_userId = idToko)
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

    fun deleteUserMembership(idToko: String): LiveData<ApiResult<GeneralMembershipResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val response = apiService.deleteUserMembership(token = "${userSession.tokenType} ${userSession.accessToken}", delete_bookmark_userId = idToko)
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

    fun addToCart(cartRequest: CartRequest): LiveData<ApiResult<GeneralMembershipResponse>> = liveData {
        emit(ApiResult.Loading)
        try {
            val userSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val response = apiService.addtoCart(token = "${userSession.tokenType} ${userSession.accessToken}", cartRequest = cartRequest)
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
        private const val TAG = "DashboardRepository"

        @Volatile
        private var instance: DashboardRepository? = null
        fun getInstance(apiService: ApiService, dataStore: UserPreference): DashboardRepository =
            instance ?: synchronized(this) {
                instance ?: DashboardRepository(apiService, dataStore)
            }.also { instance = it }

    }
}