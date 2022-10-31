package com.dicoding.submission.imam.storyapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submission.imam.storyapp.R
import com.dicoding.submission.imam.storyapp.databinding.ActivityMainBinding
import com.dicoding.submission.imam.storyapp.ui.profile.ProfileActivity
import com.dicoding.submission.imam.storyapp.ui.story.LoadingStateAdapter
import com.dicoding.submission.imam.storyapp.ui.story.StoryAdapter
import com.dicoding.submission.imam.storyapp.ui.story.StoryViewModel
import com.dicoding.submission.imam.storyapp.ui.story.add.AddStoryActivity
import com.dicoding.submission.imam.storyapp.ui.story.maps.MapsActivity
import com.dicoding.submission.imam.storyapp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val storyViewModel: StoryViewModel by viewModels()

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding!!

    private lateinit var pref: SessionManager
    private var token: String? = null

    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_activityMainBinding?.root)
        pref = SessionManager(this)
        token = pref.getToken
        // cek token preferences
        Timber.tag(TAG).i(token)

        initUI()
        initSwipeToRefresh()
        initAct()

    }

    private fun initUI() {

        // adapter
        adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateHeaderAndFooter(
            footer = LoadingStateAdapter { adapter.retry() },
            header = LoadingStateAdapter { adapter.retry() }
        )
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.setHasFixedSize(true)

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collect {
                binding.swipeRefresh.isRefreshing = it.mediator?.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.viewError.root.isVisible = loadStates.refresh is LoadState.Error
            }
            if (adapter.itemCount < 1) binding.viewError.root.visibility = View.VISIBLE
            else binding.viewError.root.visibility = View.GONE
        }

        storyViewModel.getAllStoryPaging(token!!).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun initAct() {
        binding.addNewStory.setOnClickListener {
            // AddStoryActivity
            AddStoryActivity.start(this)
        }
    }

    // update data when swipe
    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSetting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.menuStoryLocation -> {
                MapsActivity.start(this)
            }
            R.id.menuProfile -> {
                ProfileActivity.start(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        private val TAG = MainActivity::class.java.simpleName
    }
}