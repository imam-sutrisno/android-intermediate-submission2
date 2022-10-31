package com.dicoding.submission.imam.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicoding.submission.imam.storyapp.data.DataDummyFakeAuthService
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegBody
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegResponse
import com.dicoding.submission.imam.storyapp.data.repository.AuthRepository
import com.dicoding.submission.imam.storyapp.util.MainCourotineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCourotineRule = MainCourotineRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var registerViewModel: RegisterViewModel

    private var dummyRegisterSuccess =
        DataDummyFakeAuthService.generateDummyRegisterResponseSuccess()
    private val regBody: RegBody = RegBody(
        name = "name",
        email = "name@mail.com",
        password = "password"
    )

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(authRepository)
    }

    @Test
    fun `Register success and get result success`() = runTest {
        val expectedResponse = flow<ApiResponse<Response<RegResponse>>> {
            emit(ApiResponse.Success(dummyRegisterSuccess))
        }

        `when`(registerViewModel.registerUser(regBody)).thenReturn(expectedResponse)

        registerViewModel.registerUser(regBody).collect { response ->
            Assert.assertTrue(response is ApiResponse.Success)
            if (response is ApiResponse.Success) {
                Assert.assertNotNull(response)
                Assert.assertSame(dummyRegisterSuccess.body(), response.data.body())
            }
        }
    }

    @Test
    fun `Register error and get result error`() = runTest {
        val expectedResponse = flow<ApiResponse<Response<RegResponse>>> {
            emit(ApiResponse.Error("Register failed"))
        }

        `when`(registerViewModel.registerUser(regBody)).thenReturn(expectedResponse)

        registerViewModel.registerUser(regBody).collect { response ->
            Assert.assertTrue(response is ApiResponse.Error)
            if (response is ApiResponse.Error) {
                Assert.assertNotNull(response)
                Assert.assertSame("Register failed", response.errorMessage)
            }
        }
    }
}