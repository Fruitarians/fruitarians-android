package com.althaaf.fruitarians.core.helper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.core.data.network.fruitscan.FruitScanRepository
import com.althaaf.fruitarians.core.di.Injection
import com.althaaf.fruitarians.ui.fruitscan.FruitScanViewModel

class ScanViewModelFactory private constructor(private val fruitScanRepository: FruitScanRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FruitScanViewModel::class.java)) {
            return FruitScanViewModel(fruitScanRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ScanViewModelFactory? = null
        fun getInstance(): ScanViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ScanViewModelFactory(Injection.provideFruitScanRepository())
            }.also { instance = it }
    }
}