package com.dicoding.submission.imam.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicoding.submission.imam.storyapp.data.DataDummyFakeAuthService
import com.dicoding.submission.imam.storyapp.data.remote.FakeAuthService
import com.dicoding.submission.imam.storyapp.data.remote.auth.AuthService
import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginBody
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegBody
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authService: AuthService

    @Mock
    private var regBody: RegBody = RegBody(
        name = "user",
        email = "user@mail.com",
        password = "password"
    )
    private var loginBody: LoginBody = LoginBody(
        email = "user@mail.com",
        password = "password"
    )

    @Before
    fun setUp() {
        authService = FakeAuthService()
    }

    @Test
    fun `when registerUser() is called Should  Not Null`() = runTest {
        val expectedResponse = DataDummyFakeAuthService.generateDummyRegisterResponseSuccess()
        val actualResponse = authService.registerUser(regBody)

        Assert.assertNotNull(actualResponse.message())
        Assert.assertEquals(expectedResponse.message(), actualResponse.message())
    }

    @Test
    fun `when loginUser() is called Should  Not Null`() = runTest {
        val expectedResponse = DataDummyFakeAuthService.generateDummyLoginResponseSuccess()
        val actualResponse = authService.loginUser(loginBody)

        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(expectedResponse, actualResponse)
    }
}