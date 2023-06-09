package com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detail

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.FruitStorePagingSource
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DetailFruitStorePagingSource(private val dataStore: UserPreference, private val apiService: ApiService, private val id: String): PagingSource<Int, BuahItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BuahItem> {
        return try {
            val dataSessionUser = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getDetailToko(token = "${dataSessionUser.tokenType} ${dataSessionUser.accessToken}", page = page, size = 2, role = "toko", id = id)
            val responseListData = responseData.data.buah

            LoadResult.Page(
                data = responseListData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseListData.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BuahItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}