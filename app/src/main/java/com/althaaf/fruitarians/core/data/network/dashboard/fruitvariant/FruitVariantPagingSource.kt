package com.althaaf.fruitarians.core.data.network.dashboard.fruitvariant

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class FruitVariantPagingSource(private val dataStore: UserPreference, private val apiService: ApiService, private val q: String?):
    PagingSource<Int, DataItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItem> {
        return try {
            val dataSessionUser = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val page = params.key ?: INITIAL_PAGE_INDEX

            val response = apiService.getAllBuah(token = "${dataSessionUser.tokenType} ${dataSessionUser.accessToken}", page = page, size = 2, search = q)
            val responseListData = response.data

            LoadResult.Page(
                data = responseListData,
                prevKey = if (page == 1) null else page -1,
                nextKey = if (responseListData.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}