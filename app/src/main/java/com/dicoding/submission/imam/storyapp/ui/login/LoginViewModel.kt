package com.dicoding.submission.imam.storyapp.ui.login

import androidx.lifecycle.ViewModel
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginBody
import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginResponse
import com.dicoding.submission.imam.storyapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    suspend fun loginUser(loginBody: LoginBody): Flow<ApiResponse<LoginResponse>> {
        return authRepository.loginUser(loginBody)
    }
}