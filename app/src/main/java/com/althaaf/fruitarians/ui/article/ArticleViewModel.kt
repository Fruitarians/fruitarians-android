package com.althaaf.fruitarians.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.network.dashboard.DashboardRepository
import com.althaaf.fruitarians.core.data.network.dashboard.article.ArticleResponse
import com.althaaf.fruitarians.core.data.network.dashboard.article.DetailArticleResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult

class ArticleViewModel(private val dashboardRepository: DashboardRepository): ViewModel() {

    fun getArticles(): LiveData<ApiResult<ArticleResponse>> {
        return dashboardRepository.getArticles()
    }

    fun getArticleById(id: Int): LiveData<ApiResult<DetailArticleResponse>> {
        return dashboardRepository.getDetailArticles(id)
    }
}