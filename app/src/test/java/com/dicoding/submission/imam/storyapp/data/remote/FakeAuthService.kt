package com.dicoding.submission.imam.storyapp.data.remote

import com.dicoding.submission.imam.storyapp.data.DataDummyFakeAuthService
import com.dicoding.submission.imam.storyapp.data.remote.auth.*
import retrofit2.Response

class FakeAuthService : AuthService {

    private val dummyRegisterResponseSuccess =
        DataDummyFakeAuthService.generateDummyRegisterResponseSuccess()
    private val dummyLoginResponseSuccess =
        DataDummyFakeAuthService.generateDummyLoginResponseSuccess()

    override suspend fun registerUser(regBody: RegBody): Response<RegResponse> {
        return dummyRegisterResponseSuccess
    }

    override suspend fun loginUser(loginBody: LoginBody): LoginResponse {
        return dummyLoginResponseSuccess
    }
}