package com.dicoding.submission.imam.storyapp.data

import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity
import com.dicoding.submission.imam.storyapp.data.model.User
import com.dicoding.submission.imam.storyapp.data.remote.story.AddStoryResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.GetStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummyFakeStoryService {

    fun generateDummyListStory(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..50) {
            val story = StoryEntity(
                "$i",
                "Story $i",
                "Story Description",
                "Photo URL Story $i",
                "2022-09-22T22:22:22Z",
                0.2,
                0.4
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyLoginResult(): User {
        return User(
            "123",
            "user",
            "token"
        )
    }

    fun generateDummyStoryResponseSuccess(): GetStoryResponse {
        return GetStoryResponse(
            error = false,
            message = "success",
            listStory = generateDummyListStory()
        )
    }

    fun generateDummyAddStoryResponseSuccess(): AddStoryResponse {
        return AddStoryResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "dummyText"
        return dummyText.toRequestBody()
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "dummyText"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }
}