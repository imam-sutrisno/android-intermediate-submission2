package com.dicoding.submission.imam.storyapp.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicoding.submission.imam.storyapp.data.local.StoryAppDatabase
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.StoryRemoteMediator
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
    private val database: StoryAppDatabase
) {

    suspend fun getAllStory(token: String): Flow<ApiResponse<GetStoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.getAllStory(token)
                if (!response.error) {
                    database.getStoryDao().deleteAllStories()
                    val storyList = response.listStory
                    database.getStoryDao().insertStories(storyList)
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }

    fun getAllStoryPaging(token: String): Flow<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, storyService, token),
            pagingSourceFactory = {
                database.getStoryDao().getAllStoriesPaging()
            }
        ).flow
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