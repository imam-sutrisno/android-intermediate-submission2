package com.dicoding.submission.imam.storyapp.data.remote.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {

    @POST("register")
    suspend fun registerUser(
        @Body regBody: RegBody
    ): Response<RegResponse>

    @POST("login")
    suspend fun loginUser(
        @Body loginBody: LoginBody
    ): LoginResponse
}