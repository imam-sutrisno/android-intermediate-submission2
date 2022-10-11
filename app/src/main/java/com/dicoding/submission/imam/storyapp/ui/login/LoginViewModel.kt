package com.dicoding.submission.imam.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginBody
import com.dicoding.submission.imam.storyapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    fun loginUser(loginBody: LoginBody): LiveData<ApiResponse<LoginResponse>> {
        val result = MutableLiveData<ApiResponse<LoginResponse>>()
        viewModelScope.launch {
            authRepository.loginUser(loginBody).collect{
                result.postValue(it)
            }
        }
        return result
    }
}