package com.dicoding.submission.imam.storyapp.data.source

import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(private val authService: AuthService) {

    suspend fun registerUser(regBody: RegBody): Flow<ApiResponse<Response<RegResponse>>> =
        flow {
            try {
                emit(ApiResponse.Loading)
                val response = authService.registerUser(regBody)
                if (response.code() == 201) {
                    emit(ApiResponse.Success(response))
                } else if (response.code() == 400) {
                    val errorBody = JSONObject(response.errorBody()!!.string())
                    emit(ApiResponse.Error(errorBody.getString("message")))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }

    suspend fun loginUser(loginBody: LoginBody): Flow<ApiResponse<LoginResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = authService.loginUser(loginBody)
                if (!response.error) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }
}