package com.dicoding.submission.imam.storyapp.data.remote.story

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryService {

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token: String
    ): GetStoryResponse

    @GET("stories")
    suspend fun getAllStoryPaging(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody
    ): AddStoryResponse

    @GET("stories")
    suspend fun getStoryWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int
    ): GetStoryResponse
}