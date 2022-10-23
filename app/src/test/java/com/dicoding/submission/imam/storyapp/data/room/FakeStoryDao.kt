package com.dicoding.submission.imam.storyapp.data.room

import androidx.paging.PagingSource
import com.dicoding.submission.imam.storyapp.data.local.dao.StoryDao
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity

class FakeStoryDao : StoryDao {
    private var storyData = mutableListOf<StoryEntity>()

    override suspend fun insertStories(storyList: List<StoryEntity>) {
        for (row in storyList) {
            storyData.add(row)
        }
    }

    override fun getAllStories(): List<StoryEntity> {
        return storyData
    }

    override suspend fun deleteAllStories() {
        storyData.clear()
    }

    override fun getAllStoriesPaging(): PagingSource<Int, StoryEntity> {
        TODO("Not yet implemented")
    }
}