package com.dicoding.submission.imam.storyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.submission.imam.storyapp.data.local.dao.StoryDao
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StoryAppDatabase: RoomDatabase() {
    abstract fun getStoryDao(): StoryDao
}