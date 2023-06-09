package com.althaaf.fruitarians.ui.membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.althaaf.fruitarians.core.data.network.dashboard.DashboardRepository
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem
import com.althaaf.fruitarians.core.data.network.dashboard.membership.GeneralMembershipResponse
import com.althaaf.fruitarians.core.data.network.dashboard.membership.UserIsMemberResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class MembershipViewModel(private val dashboardRepository: DashboardRepository): ViewModel() {

    fun getUserMembership(): LiveData<PagingData<DataItem>> {
        return dashboardRepository.getUserMembership().cachedIn(viewModelScope)
    }

}