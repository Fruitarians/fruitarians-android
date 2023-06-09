package com.althaaf.fruitarians.ui.myvendor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.althaaf.fruitarians.core.data.network.profile.MyVendorRepository
import com.althaaf.fruitarians.core.data.network.profile.myvendor.VendorSubsItem
import com.althaaf.fruitarians.core.data.network.profile.myvendor.addsubs.AddSubsRequest
import com.althaaf.fruitarians.core.data.network.profile.myvendor.addsubs.AddSubsResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class MyVendorViewModel(private val myVendorRepository: MyVendorRepository): ViewModel() {

    fun getAllSubsVendor(): LiveData<PagingData<VendorSubsItem>> {
        return myVendorRepository.getAllSubsVendor().cachedIn(viewModelScope)
    }

    fun addSubVendor(addSubsRequest: AddSubsRequest): LiveData<ApiResult<AddSubsResponse>> {
        return myVendorRepository.addSubVendor(addSubsRequest)
    }

    fun deleteSubVendor(id_subs: String): LiveData<ApiResult<AddSubsResponse>> {
        return myVendorRepository.deleteSubVendor(id_subs)
    }

    fun changeStatusSub(id_subs: String): LiveData<ApiResult<AddSubsResponse>> {
        return myVendorRepository.changeSubStatus(id_subs)
    }

}