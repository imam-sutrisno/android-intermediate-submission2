package com.dicoding.submission.imam.storyapp.data.remote.auth

import com.dicoding.submission.imam.storyapp.data.model.User

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: User
)