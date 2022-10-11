package com.dicoding.submission.imam.storyapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Query("SELECT * FROM tb_story")
    fun getAllStories(): List<StoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStories(storyList: List<StoryEntity>)

    @Query("DELETE FROM tb_story")
    suspend fun deleteAllStories()
}