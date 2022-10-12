package com.dicoding.submission.imam.storyapp.data.source

import com.dicoding.submission.imam.storyapp.data.local.dao.StoryDao
import com.dicoding.submission.imam.storyapp.data.mapper.storyToStoryEntity
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.AddStoryResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.GetStoryResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.StoryService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryDataSource @Inject constructor(
    private val storyService: StoryService,
    private val storyDao: StoryDao
) {

    suspend fun getAllStory(token: String): Flow<ApiResponse<GetStoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.getAllStory(token)
                if (!response.error) {
                    storyDao.deleteAllStories()
                    val storyList = response.listStory.map {
                        storyToStoryEntity(it)
                    }
                    storyDao.insertStories(storyList)
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }

    suspend fun getStoryWithLocation(token: String): Flow<ApiResponse<GetStoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.getStoryWithLocation(token, 1)
                if (!response.error) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }

    suspend fun addNewStory(token: String, file: MultipartBody.Part, description: RequestBody, lat: RequestBody, lon: RequestBody): Flow<ApiResponse<AddStoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.addNewStory(token, file, description, lat, lon)
                if (!response.error) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }
}