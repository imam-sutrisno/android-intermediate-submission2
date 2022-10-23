package com.dicoding.submission.imam.storyapp.util

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity

class PagedTestDataSource : PagingSource<Int, LiveData<List<StoryEntity>>>() {
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
            return PagingData.from(items)
        }
    }
}