package com.althaaf.fruitarians.ui.membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.althaaf.fruitarians.core.data.network.dashboard.DashboardRepository
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem

class MembershipViewModel(private val dashboardRepository: DashboardRepository): ViewModel() {

    fun getUserMembership(): LiveData<PagingData<DataItem>> {
        return dashboardRepository.getUserMembership().cachedIn(viewModelScope)
    }

}