package com.dicoding.submission.imam.storyapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(storyList: List<StoryEntity>)

    @Query("SELECT * FROM tb_story")
    fun getAllStories(): List<StoryEntity>

    @Query("DELETE FROM tb_story")
    suspend fun deleteAllStories()

    @Query("SELECT * FROM tb_story")
    fun getAllStoriesPaging(): PagingSource<Int, StoryEntity>
}