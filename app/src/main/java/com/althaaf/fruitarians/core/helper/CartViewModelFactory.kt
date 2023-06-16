package com.althaaf.fruitarians.core.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.core.data.network.cart.CartRepository
import com.althaaf.fruitarians.core.di.Injection
import com.althaaf.fruitarians.ui.cart.CartViewModel

class CartViewModelFactory private constructor(private val cartRepository: CartRepository): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(cartRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: CartViewModelFactory? = null
        fun getInstance(context: Context): CartViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: CartViewModelFactory(Injection.provideCartRepository(context))
            }.also { instance = it }
    }

}