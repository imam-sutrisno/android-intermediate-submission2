package com.dicoding.submission.imam.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegBody
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegResponse
import com.dicoding.submission.imam.storyapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    fun registerUser(regBody: RegBody): LiveData<ApiResponse<Response<RegResponse>>> {
        val result = MutableLiveData<ApiResponse<Response<RegResponse>>>()
        viewModelScope.launch {
            authRepository.registerUser(regBody).collect{
                result.postValue(it)
            }
        }
        return result
    }
}