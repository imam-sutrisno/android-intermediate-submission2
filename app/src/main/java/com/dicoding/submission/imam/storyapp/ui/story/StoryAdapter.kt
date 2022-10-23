package com.dicoding.submission.imam.storyapp.ui.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity
import com.dicoding.submission.imam.storyapp.databinding.ItemRowStoryBinding
import com.dicoding.submission.imam.storyapp.ui.story.detail.DetailStoryActivity
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.BUNDLE_KEY_STORY
import com.dicoding.submission.imam.storyapp.utils.ext.setImageUrl
import com.dicoding.submission.imam.storyapp.utils.ext.timeStamptoString

class StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class StoryViewHolder(private val binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryEntity) {
            with(binding) {
                tvNameStory.text = story.name
                tvDateStory.text = story.createdAt.timeStamptoString()

                imageStory.setImageUrl(story.photoUrl, true)
            }
            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailStoryActivity::class.java)
                intent.putExtra(BUNDLE_KEY_STORY, story)

                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.imageStory, "thumbnail"),
                    Pair(binding.tvNameStory, "title"),
                )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}