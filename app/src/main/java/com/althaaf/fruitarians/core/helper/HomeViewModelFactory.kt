package com.althaaf.fruitarians.core.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.core.data.network.dashboard.DashboardRepository
import com.althaaf.fruitarians.core.di.Injection
import com.althaaf.fruitarians.ui.article.ArticleViewModel
import com.althaaf.fruitarians.ui.detailfruitstore.DetailFruitStoreViewModel
import com.althaaf.fruitarians.ui.fruitstore.FruitStoreViewModel
import com.althaaf.fruitarians.ui.fruitvariant.FruitVariantViewModel
import com.althaaf.fruitarians.ui.home.HomeViewModel
import com.althaaf.fruitarians.ui.membership.MembershipViewModel

class HomeViewModelFactory private constructor(private val dashboardRepository: DashboardRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return  HomeViewModel(dashboardRepository) as T
        }

        if (modelClass.isAssignableFrom(FruitStoreViewModel::class.java)) {
            return FruitStoreViewModel(dashboardRepository) as T
        }

        if (modelClass.isAssignableFrom(DetailFruitStoreViewModel::class.java)) {
            return DetailFruitStoreViewModel(dashboardRepository) as T
        }

        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(dashboardRepository) as T
        }

        if (modelClass.isAssignableFrom(FruitVariantViewModel::class.java)) {
            return FruitVariantViewModel(dashboardRepository) as T
        }

        if (modelClass.isAssignableFrom(MembershipViewModel::class.java)) {
            return MembershipViewModel(dashboardRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null
        fun getInstance(context: Context): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeViewModelFactory(Injection.provideHomeRepository(context))
            }.also { instance = it }
    }

}