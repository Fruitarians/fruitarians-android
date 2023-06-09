package com.althaaf.fruitarians.ui.fruitstore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.althaaf.fruitarians.core.data.network.dashboard.DashboardRepository
import com.althaaf.fruitarians.core.data.network.dashboard.DataUserResponse
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.FruitStoreResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import kotlinx.coroutines.launch

class FruitStoreViewModel(private val dashboardRepository: DashboardRepository): ViewModel() {

    fun getAllFruitStore(): LiveData<PagingData<DataItem>> {
        return dashboardRepository.getFruitStore().cachedIn(viewModelScope)
    }

    fun getAllDataUser(): LiveData<ApiResult<DataUserResponse>> {
        return dashboardRepository.getAllDataUser()
    }

    fun getOneFruitStore(): LiveData<ApiResult<FruitStoreResponse<DataItem>>> {
        return dashboardRepository.getOneFruitStore()
    }


}