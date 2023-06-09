package com.althaaf.fruitarians.ui.mystore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.althaaf.fruitarians.core.data.network.profile.MyStoreRepository
import com.althaaf.fruitarians.core.data.network.profile.mystore.BuahItem

class MyStoreViewModel(private val myStoreRepository: MyStoreRepository): ViewModel() {

    fun getTokoBuah(): LiveData<PagingData<BuahItem>> {
        return myStoreRepository.getTokoBuah().cachedIn(viewModelScope)
    }
}