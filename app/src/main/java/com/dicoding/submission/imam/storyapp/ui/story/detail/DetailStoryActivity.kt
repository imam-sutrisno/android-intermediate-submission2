package com.dicoding.submission.imam.storyapp.ui.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity
import com.dicoding.submission.imam.storyapp.data.model.Story
import com.dicoding.submission.imam.storyapp.databinding.ActivityDetailStoryBinding
import com.dicoding.submission.imam.storyapp.utils.TextConstValue
import com.dicoding.submission.imam.storyapp.utils.ext.setImageUrl

class DetailStoryActivity : AppCompatActivity() {

    private var _activityDetailStoryBinding: ActivityDetailStoryBinding? = null
    private val binding get() = _activityDetailStoryBinding!!

    private lateinit var story: StoryEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityDetailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(_activityDetailStoryBinding?.root)

        initIntent()
        initUI()
    }

    private fun initUI() {
        binding.apply {
            imgStoryDetail.setImageUrl(story.photoUrl, true)
            tvNameStory.text = story.name
            tvDescStory.text = story.description
        }
        title = "Detail Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initIntent() {
        story = intent.getParcelableExtra(TextConstValue.BUNDLE_KEY_STORY)!!
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }
}