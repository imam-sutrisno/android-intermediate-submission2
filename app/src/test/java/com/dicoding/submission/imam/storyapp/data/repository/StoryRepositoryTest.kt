package com.dicoding.submission.imam.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.submission.imam.storyapp.data.DataDummyFakeStoryService
import com.dicoding.submission.imam.storyapp.data.remote.FakeStoryService
import com.dicoding.submission.imam.storyapp.data.remote.story.StoryService
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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyService: StoryService

    @Mock
    private var dummyMultipart = DataDummyFakeStoryService.generateDummyMultipartFile()
    private var dummyDescription = DataDummyFakeStoryService.generateDummyRequestBody()
    private var dummyLatitude = DataDummyFakeStoryService.generateDummyRequestBody()
    private var dummyLongitude = DataDummyFakeStoryService.generateDummyRequestBody()

    @Before
    fun setUp() {
        storyService = FakeStoryService()
    }

    @Test
    fun `when getAllStoryPaging() is called should not null`() = runTest {
        val mainCoroutineRule = MainCourotineRule()

        val dummyStory = DataDummyFakeStoryService.generateDummyListStory()
        val data = PagedTestDataSource.snapshot(dummyStory)

        val expectedResult = flowOf(data)
        `when`(storyRepository.getAllStoryPaging("token")).thenReturn(expectedResult)

        storyRepository.getAllStoryPaging("token").collect {
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
        val actualResponse = storyService.addNewStory(
            "token",
            dummyMultipart,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        )
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(expectedResponses, actualResponse)
    }

    @Test
    fun `when getStoryWithLocation() is called should not null`() = runTest {
        val expectedResponse = DataDummyFakeStoryService.generateDummyStoryResponseSuccess()
        val actualResponse = storyService.getStoryWithLocation("token", 1)

        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(expectedResponse.listStory.size, actualResponse.listStory.size)
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}