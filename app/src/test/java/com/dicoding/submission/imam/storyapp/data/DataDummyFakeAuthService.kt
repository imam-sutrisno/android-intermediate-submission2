package com.dicoding.submission.imam.storyapp.data

import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegResponse
import retrofit2.Response

object DataDummyFakeAuthService {

    fun generateDummyRegisterResponseSuccess(): Response<RegResponse> {
        return Response.success(
            RegResponse(
                error = false,
                message = "success"
            )
        )
    }

    fun generateDummyLoginResponseSuccess(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = DataDummyFakeStoryService.generateDummyLoginResult()
        )
    }

}