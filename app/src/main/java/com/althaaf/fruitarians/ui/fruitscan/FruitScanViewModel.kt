package com.althaaf.fruitarians.ui.fruitscan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.network.fruitscan.FruitScanRepository
import com.althaaf.fruitarians.core.data.network.fruitscan.FruitScanResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import okhttp3.MultipartBody

class FruitScanViewModel(private val fruitScanRepository: FruitScanRepository): ViewModel() {

    fun fruitScanDetection(image_data: MultipartBody.Part): LiveData<ApiResult<FruitScanResponse>> {
        return fruitScanRepository.fruitScanDetection(image_data)
    }
}