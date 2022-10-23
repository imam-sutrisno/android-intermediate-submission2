package com.dicoding.submission.imam.storyapp.data.remote

import com.dicoding.submission.imam.storyapp.data.DataDummyFakeStoryService
import com.dicoding.submission.imam.storyapp.data.remote.story.AddStoryResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.GetStoryResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.StoryService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeStoryService : StoryService {

    private val dummyStoryResponseSuccess =
        DataDummyFakeStoryService.generateDummyStoryResponseSuccess()
    private val dummyAddStoryResponseSuccess =
        DataDummyFakeStoryService.generateDummyAddStoryResponseSuccess()

    override suspend fun getAllStory(token: String): GetStoryResponse {
        return dummyStoryResponseSuccess
    }

    override suspend fun getAllStoryPaging(token: String, page: Int, size: Int): GetStoryResponse {
        return dummyStoryResponseSuccess
    }

    override suspend fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody
    ): AddStoryResponse {
        return dummyAddStoryResponseSuccess
    }

    override suspend fun getStoryWithLocation(token: String, location: Int): GetStoryResponse {
        return dummyStoryResponseSuccess
    }
}