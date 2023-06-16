package com.althaaf.fruitarians.ui.fruitvariant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.althaaf.fruitarians.core.data.network.dashboard.DashboardRepository
import com.althaaf.fruitarians.core.data.network.dashboard.fruitvariant.BuahResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class FruitVariantViewModel(private val dashboardRepository: DashboardRepository): ViewModel() {

    fun getAllFruit(q: String?): LiveData<PagingData<com.althaaf.fruitarians.core.data.network.dashboard.fruitvariant.DataItem>> {
        return dashboardRepository.getAllBuah(q).cachedIn(viewModelScope)
    }

}