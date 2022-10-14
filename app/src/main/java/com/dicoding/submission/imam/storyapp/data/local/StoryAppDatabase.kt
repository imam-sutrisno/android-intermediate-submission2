package com.dicoding.submission.imam.storyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.submission.imam.storyapp.data.local.dao.RemoteKeysDao
import com.dicoding.submission.imam.storyapp.data.local.dao.StoryDao
import com.dicoding.submission.imam.storyapp.data.local.entity.RemoteKeysEntity
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeysEntity::class],
    version = 2,
    exportSchema = false
)
abstract class StoryAppDatabase: RoomDatabase() {
    abstract fun getStoryDao(): StoryDao

    abstract fun getRemoteKeysDao(): RemoteKeysDao
}