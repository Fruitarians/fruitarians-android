package com.althaaf.fruitarians.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.network.profile.MyStoreRepository
import com.althaaf.fruitarians.core.data.network.profile.addproduct.AddProductResponse
import com.althaaf.fruitarians.core.data.network.profile.deleteproduct.DeleteProductResponse
import com.althaaf.fruitarians.core.data.network.profile.updateproduct.UpdateProductResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProductAddUpdateViewModel(private val myStoreRepository: MyStoreRepository): ViewModel() {

    fun addProductBuah(
        name: RequestBody,
        harga: Int,
        satuan: RequestBody,
        deskripsi: RequestBody,
        stok: Int,
        file: MultipartBody.Part
    ) : LiveData<ApiResult<AddProductResponse>> {
        return myStoreRepository.addProductBuah(
            name = name,
            harga = harga,
            satuan = satuan,
            deskripsi = deskripsi,
            stok = stok,
            file = file
        )
    }

    fun updateProductBuah(
        buahId: RequestBody,
        name: RequestBody,
        harga: Int,
        satuan: RequestBody,
        stok: Int,
        deskripsi: RequestBody,
        file: MultipartBody.Part?
    ): LiveData<ApiResult<UpdateProductResponse>> {
        return myStoreRepository.updateProductBuah(
            buahId = buahId,
            name = name,
            harga = harga,
            satuan = satuan,
            stok = stok,
            deskripsi = deskripsi,
            file = file
        )
    }

    fun deleteProductBuah(idBuah: String) : LiveData<ApiResult<DeleteProductResponse>> {
        return myStoreRepository.deleteProductBuah(idBuah)
    }
}