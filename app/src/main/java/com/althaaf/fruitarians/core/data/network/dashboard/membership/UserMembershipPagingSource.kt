package com.althaaf.fruitarians.core.data.network.dashboard.membership

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.althaaf.fruitarians.core.data.local.datastore.UserPreference
import com.althaaf.fruitarians.core.data.network.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem

class UserMembershipPagingSource(private val dataStore: UserPreference, private val apiService: ApiService): PagingSource<Int, DataItem>() {

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItem> {
        return try {
            val dataSession = withContext(Dispatchers.IO) {
                dataStore.getUser().first()
            }

            val page = params.key ?: INITIAL_PAGE_INDEX

            val response = apiService.getUserMembership(token = "${dataSession.tokenType} ${dataSession.accessToken}", page = page, size = 2 )
            val listResponse = response.data

            LoadResult.Page(
                data = listResponse,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (listResponse.isEmpty()) null else page + 1
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
}