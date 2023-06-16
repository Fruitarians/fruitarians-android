package com.althaaf.fruitarians.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.network.cart.CartRepository
import com.althaaf.fruitarians.core.data.network.cart.CartResponse
import com.althaaf.fruitarians.core.data.network.dashboard.membership.GeneralMembershipResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class CartViewModel(private val cartRepository: CartRepository): ViewModel() {

    fun getUserCart(): LiveData<ApiResult<CartResponse>> {
        return cartRepository.getCartUser()
    }

    fun deleteFruitCart(idCart: String, idBuah: String): LiveData<ApiResult<GeneralMembershipResponse>> {
        return cartRepository.deleteFruitCart(idCart = idCart, idBuah = idBuah)
    }

    fun deleteStoreCart(idCart: String): LiveData<ApiResult<GeneralMembershipResponse>> {
        return cartRepository.deleteStoreCart(idCart = idCart)
    }

}