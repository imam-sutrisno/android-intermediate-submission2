package com.dicoding.submission.imam.storyapp.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.dicoding.submission.imam.storyapp.JsonConverter
import com.dicoding.submission.imam.storyapp.R
import com.dicoding.submission.imam.storyapp.utils.BASE_URL
import com.dicoding.submission.imam.storyapp.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getAllStory_Success() {

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("response.json"))
        mockWebServer.enqueue(mockResponse)

        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.rvStory)).check(matches(isDisplayed()))

        onView(withText("pakani")).check(matches(isDisplayed()))

        onView(withId(R.id.rvStory)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                6
            )
        )
    }
}