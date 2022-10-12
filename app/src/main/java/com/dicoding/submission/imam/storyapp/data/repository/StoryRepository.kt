package com.dicoding.submission.imam.storyapp.data.repository

import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.AddStoryResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.GetStoryResponse
import com.dicoding.submission.imam.storyapp.data.source.StoryDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(private val storyDataSource: StoryDataSource) {
    suspend fun getAllStory(token: String): Flow<ApiResponse<GetStoryResponse>> {
        return storyDataSource.getAllStory(token).flowOn(Dispatchers.IO)
    }

    suspend fun addNewStory(token: String, file: MultipartBody.Part, description: RequestBody, lat: RequestBody, lon: RequestBody): Flow<ApiResponse<AddStoryResponse>> {
        return storyDataSource.addNewStory(token, file, description, lat, lon).flowOn(Dispatchers.IO)
    }

    suspend fun getStoryWithLocation(token: String): Flow<ApiResponse<GetStoryResponse>> {
        return storyDataSource.getStoryWithLocation(token)
    }
}