package com.dicoding.submission.imam.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.submission.imam.storyapp.data.DataDummyFakeAuthService
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegBody
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegResponse
import com.dicoding.submission.imam.storyapp.data.repository.AuthRepository
import com.dicoding.submission.imam.storyapp.util.MainCourotineRule
import com.dicoding.submission.imam.storyapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
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


    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCourotineRule = MainCourotineRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
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
        authRepository = mock(AuthRepository::class.java)
    }

    @Test
    fun `Register success and get result success`() = runTest {
        val expectedResponse = MutableLiveData<ApiResponse<Response<RegResponse>>>()
        expectedResponse.value = ApiResponse.Success(dummyRegisterSuccess)

        `when`(registerViewModel.registerUser(regBody)).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.registerUser(regBody).getOrAwaitValue()
        verify(registerViewModel).registerUser(regBody)
        advanceUntilIdle()

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ApiResponse.Success)
        if (actualResponse is ApiResponse.Success) {
            Assert.assertSame(dummyRegisterSuccess.body(), actualResponse.data.body())
        }
    }

    @Test
    fun `Register error and get result error`() = runTest {
        val expectedResponse = MutableLiveData<ApiResponse<Response<RegResponse>>>()
        expectedResponse.value = ApiResponse.Error("Register failed")

        `when`(registerViewModel.registerUser(regBody)).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.registerUser(regBody).getOrAwaitValue()
        verify(registerViewModel).registerUser(regBody)
        advanceUntilIdle()

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ApiResponse.Error)
        if (actualResponse is ApiResponse.Error) {
            Assert.assertSame(
                (expectedResponse.value as ApiResponse.Error).errorMessage,
                actualResponse.errorMessage
            )
        }
    }
}