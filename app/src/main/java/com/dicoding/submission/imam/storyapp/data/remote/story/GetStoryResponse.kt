package com.dicoding.submission.imam.storyapp.data.remote.story

import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity
import com.dicoding.submission.imam.storyapp.data.model.Story

data class GetStoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<StoryEntity>
)