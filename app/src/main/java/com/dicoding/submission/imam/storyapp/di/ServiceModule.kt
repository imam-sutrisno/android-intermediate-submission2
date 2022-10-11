package com.dicoding.submission.imam.storyapp.di

import com.dicoding.submission.imam.storyapp.data.remote.auth.AuthService
import com.dicoding.submission.imam.storyapp.data.remote.story.StoryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    fun provideStoryService(retrofit: Retrofit): StoryService = retrofit.create(StoryService::class.java)
}