package com.dicoding.submission.imam.storyapp.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.submission.imam.storyapp.data.DataDummyFakeStoryService
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.AddStoryResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.GetStoryResponse
import com.dicoding.submission.imam.storyapp.data.repository.StoryRepository
import com.dicoding.submission.imam.storyapp.util.MainCourotineRule
import com.dicoding.submission.imam.storyapp.util.PagedTestDataSource
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCourotineRule = MainCourotineRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var storyViewModel: StoryViewModel

    private var dummyMultipart = DataDummyFakeStoryService.generateDummyMultipartFile()
    private var dummyDescription = DataDummyFakeStoryService.generateDummyRequestBody()
    private var dummyLatitude = DataDummyFakeStoryService.generateDummyRequestBody()
    private var dummyLongitude = DataDummyFakeStoryService.generateDummyRequestBody()
    private val dummyResult = DataDummyFakeStoryService.generateDummyStoryResponseSuccess()
    private val dummyAddStoryResponse =
        DataDummyFakeStoryService.generateDummyAddStoryResponseSuccess()

    @Before
    fun setUp() {
        storyRepository = mock(StoryRepository::class.java)
    }

    @Test
    fun `getStoryWithLocation() successfully`() = runTest {
        val expectedResponse = MutableLiveData<ApiResponse<GetStoryResponse>>()
        expectedResponse.value = ApiResponse.Success(dummyResult)

        `when`(storyViewModel.getStoryWithLocation("token")).thenReturn(expectedResponse)

        val actualResponse = storyViewModel.getStoryWithLocation("token").getOrAwaitValue()
        verify(storyViewModel).getStoryWithLocation("token")
        advanceUntilIdle()

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ApiResponse.Success)
        Assert.assertSame(expectedResponse.value, actualResponse)
    }

    @Test
    fun `getStoryWithLocation() error`() = runTest {
        val expectedResponse = MutableLiveData<ApiResponse<GetStoryResponse>>()
        expectedResponse.value = ApiResponse.Error("get story with location failed")

        `when`(storyViewModel.getStoryWithLocation("token")).thenReturn(expectedResponse)

        val actualResponse = storyViewModel.getStoryWithLocation("token").getOrAwaitValue()
        verify(storyViewModel).getStoryWithLocation("token")
        advanceUntilIdle()

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ApiResponse.Error)
        Assert.assertSame(expectedResponse.value, actualResponse)
    }

    @Test
    fun `getAllStoryPaging() successfully`() = runTest {
        val dummyStory = DataDummyFakeStoryService.generateDummyListStory()
        val dataPaging = PagedTestDataSource.snapshot(dummyStory)

        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = dataPaging

        `when`(storyViewModel.getAllStoryPaging("token")).thenReturn(expectedStory)

        val actualStory = storyViewModel.getAllStoryPaging("token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = mainCourotineRule.dispatcher,
            workerDispatcher = mainCourotineRule.dispatcher
        )
        differ.submitData(actualStory)

        advanceUntilIdle()
        verify(storyViewModel).getAllStoryPaging("token")

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
    }

    @Test
    fun `addNewStory() successfully`() = runTest {
        val expectedResponse = MutableLiveData<ApiResponse<AddStoryResponse>>()
        expectedResponse.value = ApiResponse.Success(dummyAddStoryResponse)

        `when`(
            storyViewModel.addNewStory(
                "token",
                dummyMultipart,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            )
        ).thenReturn(expectedResponse)

        val actualResponse = storyViewModel.addNewStory(
            "token",
            dummyMultipart,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        ).getOrAwaitValue()

        advanceUntilIdle()

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ApiResponse.Success)
    }

    @Test
    fun `addNewStory() error`() = runTest {
        val expectedResponse = MutableLiveData<ApiResponse<AddStoryResponse>>()
        expectedResponse.value = ApiResponse.Error("Add story failed")

        `when`(
            storyViewModel.addNewStory(
                "token",
                dummyMultipart,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            )
        ).thenReturn(expectedResponse)

        val actualResponse = storyViewModel.addNewStory(
            "token",
            dummyMultipart,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        ).getOrAwaitValue()

        advanceUntilIdle()

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ApiResponse.Error)
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}