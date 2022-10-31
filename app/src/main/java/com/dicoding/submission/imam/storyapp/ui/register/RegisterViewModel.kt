package com.dicoding.submission.imam.storyapp.ui.register

import androidx.lifecycle.*
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegBody
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegResponse
import com.dicoding.submission.imam.storyapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    suspend fun registerUser(regBody: RegBody): Flow<ApiResponse<Response<RegResponse>>> {
        return authRepository.registerUser(regBody)
    }
}