package com.dicoding.submission.imam.storyapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submission.imam.storyapp.R
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.databinding.ActivityMainBinding
import com.dicoding.submission.imam.storyapp.ui.profile.ProfileActivity
import com.dicoding.submission.imam.storyapp.ui.story.StoryAdapter
import com.dicoding.submission.imam.storyapp.ui.story.StoryViewModel
import com.dicoding.submission.imam.storyapp.ui.story.add.AddStoryActivity
import com.dicoding.submission.imam.storyapp.ui.story.maps.MapsActivity
import com.dicoding.submission.imam.storyapp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val storyViewModel: StoryViewModel by viewModels()

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding!!

    private lateinit var pref: SessionManager
    private var token: String? = null

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_activityMainBinding?.root)
        pref = SessionManager(this)
        token = pref.getToken
        // cek token preferences
        Timber.tag(TAG).i(token)

        initUI()
        initAct()

        getAllStory("Bearer $token")
    }

    private fun initUI() {
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.tvGreetingName.text = getString(R.string.label_greeting_user, pref.getUserName)
    }

    private fun initAct() {
        binding.btnProfile.setOnClickListener {
            // ProfileActivity
            ProfileActivity.start(this)
        }
        binding.addNewStory.setOnClickListener {
            // AddStoryActivity
            AddStoryActivity.start(this)
        }
    }

    private fun getAllStory(token: String) {
        storyViewModel.getAllStory(token).observe(this) { response ->
            when (response) {
                is ApiResponse.Loading -> showLoading(true)
                is ApiResponse.Success -> {
                    showLoading(false)
                    if (response.data.listStory.isEmpty()) {
                        // jika tidak ada story
                        binding.emptyStory.visibility = View.VISIBLE
                    } else {
                        binding.emptyStory.visibility = View.INVISIBLE
                        val adapter = StoryAdapter(response.data.listStory)
                        binding.rvStory.adapter = adapter
                    }
                }
                is ApiResponse.Error -> showLoading(false)
                else -> {
                    Timber.tag(TAG).e(getString(R.string.message_unknown_error))
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        getAllStory("Bearer $token")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuSetting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.menuStoryLocation -> {
                MapsActivity.start(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}