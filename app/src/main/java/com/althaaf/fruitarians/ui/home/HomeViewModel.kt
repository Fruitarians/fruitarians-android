package com.althaaf.fruitarians.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.local.model.UserModel
import com.althaaf.fruitarians.core.data.network.dashboard.DashboardRepository

class HomeViewModel(private val dashboardRepository: DashboardRepository) : ViewModel() {

    fun getUserSession(): LiveData<UserModel> {
        return dashboardRepository.getUserSession()
    }
}

