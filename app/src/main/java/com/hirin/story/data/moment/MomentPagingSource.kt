package com.hirin.story.data.moment

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hirin.story.data.moment.response.MomentListStoryResponse

class MomentPagingSource(private val apiService: MomentService): PagingSource<Int, MomentListStoryResponse>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, MomentListStoryResponse>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MomentListStoryResponse> {
        return try {
            val position =  params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getMoment(position, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (ex: Exception) {
            return LoadResult.Error(ex)
        }
    }
}