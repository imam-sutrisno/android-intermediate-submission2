package com.dicoding.submission.imam.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.AddStoryResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.GetStoryResponse
import com.dicoding.submission.imam.storyapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {
    fun getAllStory(token: String): LiveData<ApiResponse<GetStoryResponse>> {
        val result = MutableLiveData<ApiResponse<GetStoryResponse>>()
        viewModelScope.launch {
            storyRepository.getAllStory(token).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getAllStoryPaging(token: String): LiveData<PagingData<StoryEntity>> {
        val result = MutableLiveData<PagingData<StoryEntity>>()
        viewModelScope.launch {
            storyRepository.getAllStoryPaging(token).cachedIn(viewModelScope).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody
    ): LiveData<ApiResponse<AddStoryResponse>> {
        val result = MutableLiveData<ApiResponse<AddStoryResponse>>()
        viewModelScope.launch {
            storyRepository.addNewStory(token, file, description, lat, lon).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getStoryWithLocation(token: String): LiveData<ApiResponse<GetStoryResponse>> {
        val result = MutableLiveData<ApiResponse<GetStoryResponse>>()
        viewModelScope.launch {
            storyRepository.getStoryWithLocation(token).collect {
                result.postValue(it)
            }
        }
        return result
    }
}