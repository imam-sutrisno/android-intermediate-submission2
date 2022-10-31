package com.dicoding.submission.imam.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicoding.submission.imam.storyapp.data.DataDummyFakeAuthService
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.*
import com.dicoding.submission.imam.storyapp.data.source.AuthDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var authService: AuthService

    @Mock
    private lateinit var authDataSource: AuthDataSource
    private lateinit var authRepository: AuthRepository


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
        authService = mock(AuthService::class.java)
        authDataSource = AuthDataSource(authService)
        authRepository = AuthRepository(authDataSource)
    }

    @Test
    fun `when registerUser() is called Should  Not Null`() = runTest {
        val expectedResponse = flow<ApiResponse<Response<RegResponse>>> {
            this.emit(ApiResponse.Success(DataDummyFakeAuthService.generateDummyRegisterResponseSuccess()))
        }
        `when`(authService.registerUser(regBody)).thenReturn(DataDummyFakeAuthService.generateDummyRegisterResponseSuccess())

        authRepository.registerUser(regBody).collect { response ->
            if (response is ApiResponse.Success) {
                Assert.assertNotNull(response)
                expectedResponse.collect {
                    Assert.assertEquals(it, response)
                }
            } else if (response is ApiResponse.Error) {
                Assert.assertNull(response)
            }
        }
    }


    @Test
    fun `when loginUser() is called Should  Not Null`() = runTest {
        val expectedResponse = flow<ApiResponse<LoginResponse>> {
            this.emit(ApiResponse.Success(DataDummyFakeAuthService.generateDummyLoginResponseSuccess()))
        }
        `when`(authService.loginUser(loginBody)).thenReturn(DataDummyFakeAuthService.generateDummyLoginResponseSuccess())

        authRepository.loginUser(loginBody).collect { response ->
            if (response is ApiResponse.Success) {
                Assert.assertNotNull(response)
                expectedResponse.collect {
                    Assert.assertEquals(it, response)
                }
            } else if (response is ApiResponse.Error) {
                Assert.assertNull(response)
            }
        }

    }
}

