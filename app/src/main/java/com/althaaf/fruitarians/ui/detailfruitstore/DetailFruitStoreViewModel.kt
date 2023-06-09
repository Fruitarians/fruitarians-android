package com.althaaf.fruitarians.ui.detailfruitstore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.althaaf.fruitarians.core.data.network.dashboard.DashboardRepository
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detail.BuahItem
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detailfruit.DetailSpecificFruitResponse
import com.althaaf.fruitarians.core.data.network.dashboard.membership.GeneralMembershipResponse
import com.althaaf.fruitarians.core.data.network.dashboard.membership.UserIsMemberResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class DetailFruitStoreViewModel(private val dashboardRepository: DashboardRepository): ViewModel() {

    fun getDetailFruitStore(id: String): LiveData<PagingData<BuahItem>> {
        return dashboardRepository.getDetailFruitStore(id).cachedIn(viewModelScope)
    }

    fun getSpecificFruit(idToko: String, idBuah: String): LiveData<ApiResult<DetailSpecificFruitResponse>> {
        return dashboardRepository.getSpecificFruit(idToko,idBuah)
    }

    fun addUserMembership(idToko: String): LiveData<ApiResult<GeneralMembershipResponse>> {
        return dashboardRepository.addUserMembership(idToko)
    }

    fun deleteUserMembership(idToko: String): LiveData<ApiResult<GeneralMembershipResponse>> {
        return dashboardRepository.deleteUserMembership(idToko)
    }

    fun getUserisMember(idToko: String): LiveData<ApiResult<UserIsMemberResponse>> {
        return dashboardRepository.getUserisMember(idToko)
    }

}