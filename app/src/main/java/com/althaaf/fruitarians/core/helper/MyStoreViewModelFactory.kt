package com.althaaf.fruitarians.core.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.core.data.network.profile.MyStoreRepository
import com.althaaf.fruitarians.core.di.Injection
import com.althaaf.fruitarians.ui.mystore.MyStoreViewModel
import com.althaaf.fruitarians.ui.product.ProductAddUpdateViewModel

class MyStoreViewModelFactory private constructor(private val myStoreRepository: MyStoreRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MyStoreViewModel::class.java)) {
            return  MyStoreViewModel(myStoreRepository) as T
        }

        if (modelClass.isAssignableFrom(ProductAddUpdateViewModel::class.java)) {
            return  ProductAddUpdateViewModel(myStoreRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MyStoreViewModelFactory? = null
        fun getInstance(context: Context): MyStoreViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MyStoreViewModelFactory(Injection.provideMyStoreRepository(context))
            }.also { instance = it }
    }
}