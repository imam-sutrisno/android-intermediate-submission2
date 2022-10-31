package com.dicoding.submission.imam.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.submission.imam.storyapp.data.DataDummyFakeStoryService
import com.dicoding.submission.imam.storyapp.data.local.StoryAppDatabase
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.story.StoryService
import com.dicoding.submission.imam.storyapp.data.source.StoryDataSource
import com.dicoding.submission.imam.storyapp.ui.story.StoryAdapter
import com.dicoding.submission.imam.storyapp.util.MainCourotineRule
import com.dicoding.submission.imam.storyapp.util.PagedTestDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyDataSource: StoryDataSource
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyRepositoryMock: StoryRepository
    private lateinit var storyService: StoryService

    @Mock
    private lateinit var storyAppDatabase: StoryAppDatabase

    @Mock
    private var dummyMultipart = DataDummyFakeStoryService.generateDummyMultipartFile()
    private var dummyDescription = DataDummyFakeStoryService.generateDummyRequestBody()
    private var dummyLatitude = DataDummyFakeStoryService.generateDummyRequestBody()
    private var dummyLongitude = DataDummyFakeStoryService.generateDummyRequestBody()

    @Before
    fun setUp() {
        storyService = mock(StoryService::class.java)
        storyAppDatabase = mock(StoryAppDatabase::class.java)
        storyDataSource = StoryDataSource(storyService, storyAppDatabase)

        storyRepositoryMock = mock(StoryRepository::class.java)
        storyRepository = StoryRepository(storyDataSource)
    }

    @Test
    fun `when getAllStoryPaging() is called should not null`() = runTest {
        val mainCoroutineRule = MainCourotineRule()

        val dummyStory = DataDummyFakeStoryService.generateDummyListStory()
        val data = PagedTestDataSource.snapshot(dummyStory)

        val expectedResult = flowOf(data)
        `when`(storyRepositoryMock.getAllStoryPaging("token")).thenReturn(expectedResult)

        storyRepositoryMock.getAllStoryPaging("token").collect {
            val differ = AsyncPagingDataDiffer(
                StoryAdapter.DIFF_CALLBACK,
                listUpdateCallback,
                mainCoroutineRule.dispatcher,
                mainCoroutineRule.dispatcher
            )

            differ.submitData(it)
            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(
                DataDummyFakeStoryService.generateDummyStoryResponseSuccess().listStory.size,
                differ.snapshot().size
            )
        }
    }

    @Test
    fun `when addNewStory() is called should not null`() = runTest {
        val expectedResponses = DataDummyFakeStoryService.generateDummyAddStoryResponseSuccess()

        `when`(
            storyService.addNewStory(
                "token",
                dummyMultipart,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            )
        ).thenReturn(expectedResponses)

        storyRepository.addNewStory(
            "token",
            dummyMultipart,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        ).collect { response ->
            if (response is ApiResponse.Success) {
                Assert.assertNotNull(response)
                Assert.assertEquals(expectedResponses, response.data)
            }
        }
    }

    @Test
    fun `when getStoryWithLocation() is called should not null`() = runTest {
        val expectedResponse = DataDummyFakeStoryService.generateDummyStoryResponseSuccess()

        `when`(storyService.getStoryWithLocation("token", 1)).thenReturn(expectedResponse)

        storyRepository.getStoryWithLocation("token").collect { response ->
            if (response is ApiResponse.Success) {
                Assert.assertNotNull(response)
                Assert.assertEquals(expectedResponse, response.data)
            }
        }
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}