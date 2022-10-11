package com.dicoding.submission.imam.storyapp.data.repository

import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginBody
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegBody
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegResponse
import com.dicoding.submission.imam.storyapp.data.source.AuthDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val authDataSource: AuthDataSource) {

    suspend fun registerUser(regBody: RegBody): Flow<ApiResponse<Response<RegResponse>>> {
        return authDataSource.registerUser(regBody).flowOn(Dispatchers.IO)
    }

    suspend fun loginUser(loginBody: LoginBody): Flow<ApiResponse<LoginResponse>> {
        return authDataSource.loginUser(loginBody).flowOn(Dispatchers.IO)
    }
}