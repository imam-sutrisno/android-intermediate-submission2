package com.dicoding.submission.imam.storyapp.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dicoding.submission.imam.storyapp.R
import com.dicoding.submission.imam.storyapp.ui.story.add.AddStoryActivity
import com.dicoding.submission.imam.storyapp.ui.story.detail.DetailStoryActivity
import com.dicoding.submission.imam.storyapp.ui.story.maps.MapsActivity
import com.dicoding.submission.imam.storyapp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityEndToEndTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadStory_Success() {
        onView(withId(R.id.rvStory)).check(matches(isDisplayed()))
        onView(withId(R.id.swipeRefresh)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyStory)).check(matches(isDisplayed()))
        onView(withId(R.id.addNewStory)).check(matches(isDisplayed()))
        onView(withId(R.id.view_error)).check(matches(isDisplayed()))
        onView(withId(R.id.rvStory)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                7
            )
        )
    }

    @Test
    fun loadDetailStory_Success() {
        Intents.init()
        onView(withId(R.id.rvStory)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1, click()
            )
        )
        intended(hasComponent(DetailStoryActivity::class.java.name))
        onView(withId(R.id.imgStoryDetail)).check(matches(isDisplayed()))
        onView(withId(R.id.tvNameStory)).check(matches(isDisplayed()))
        onView(withId(R.id.tvDescStory)).check(matches(isDisplayed()))
        Intents.release()
    }

    @Test
    fun loadMapStory_Success() {
        Intents.init()
        onView(withId(R.id.menuStoryLocation)).perform(click())
        intended(hasComponent(MapsActivity::class.java.name))
        onView(withId(R.id.map_view)).check(matches(isDisplayed()))
        Intents.release()
    }

    @Test
    fun loadAddStory_Success() {
        Intents.init()
        onView(withId(R.id.addNewStory)).perform(click())
        intended(hasComponent(AddStoryActivity::class.java.name))
        onView(withId(R.id.imgPreview)).check(matches(isDisplayed()))
        onView(withId(R.id.btnOpenCamera)).check(matches(isDisplayed()))
        onView(withId(R.id.btnOpenGallery)).check(matches(isDisplayed()))
        onView(withId(R.id.edtStoryDesc)).check(matches(isDisplayed()))
        onView(withId(R.id.imageLokasi)).check(matches(isDisplayed()))
        onView(withId(R.id.tvLocation)).check(matches(isDisplayed()))
        onView(withId(R.id.btnUpload)).check(matches(isDisplayed()))
        Intents.release()
    }
}