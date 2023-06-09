package com.althaaf.fruitarians.core.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.core.data.network.profile.MyVendorRepository
import com.althaaf.fruitarians.core.di.Injection
import com.althaaf.fruitarians.ui.myvendor.MyVendorViewModel

class MyVendorViewModelFactory private constructor(private val myVendorRepository: MyVendorRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MyVendorViewModel::class.java)) {
            return  MyVendorViewModel(myVendorRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MyVendorViewModelFactory? = null
        fun getInstance(context: Context): MyVendorViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MyVendorViewModelFactory(Injection.provideMyVendorRepository(context))
            }.also { instance = it }
    }

}