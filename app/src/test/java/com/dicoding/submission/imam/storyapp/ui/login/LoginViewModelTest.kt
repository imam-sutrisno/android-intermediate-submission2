package com.dicoding.submission.imam.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicoding.submission.imam.storyapp.data.DataDummyFakeAuthService
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginBody
import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginResponse
import com.dicoding.submission.imam.storyapp.data.repository.AuthRepository
import com.dicoding.submission.imam.storyapp.util.MainCourotineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCourotineRule = MainCourotineRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyResult = DataDummyFakeAuthService.generateDummyLoginResponseSuccess()

    @Mock
    private val loginBody: LoginBody = LoginBody(
        email = "email",
        password = "password"
    )

    @Before
    fun setUp() {
        authRepository = mock(AuthRepository::class.java)
        loginViewModel = LoginViewModel(authRepository)
    }

    @Test
    fun `when loginUser() is Called Should Return Success and Data`() = runTest {
        val expectedResponse = flow<ApiResponse<LoginResponse>> {
            emit(ApiResponse.Success(dummyResult))
        }
        `when`(loginViewModel.loginUser(loginBody)).thenReturn(expectedResponse)

        loginViewModel.loginUser(loginBody).collect { response ->
            Assert.assertTrue(response is ApiResponse.Success)
            if (response is ApiResponse.Success) {
                Assert.assertNotNull(response)
                Assert.assertSame(dummyResult, response.data)
            }
        }

        verify(authRepository).loginUser(loginBody)
    }

    @Test
    fun `when loginUser() failed and get error result Exception`() = runTest {
        val expectedResponse =
            flowOf<ApiResponse<LoginResponse>>(ApiResponse.Error("Failed to Login"))

        `when`(loginViewModel.loginUser(loginBody)).thenReturn(expectedResponse)

        loginViewModel.loginUser(loginBody).collect { response ->
            Assert.assertTrue(response is ApiResponse.Error)
            if (response is ApiResponse.Error) {
                Assert.assertNotNull(response)
                Assert.assertSame("Failed to Login", response.errorMessage)
            }
        }

        verify(authRepository).loginUser(loginBody)
    }
}