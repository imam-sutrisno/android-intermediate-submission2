package com.dicoding.submission.imam.storyapp.ui.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.submission.imam.storyapp.data.model.Story
import com.dicoding.submission.imam.storyapp.databinding.ItemRowStoryBinding
import com.dicoding.submission.imam.storyapp.ui.story.detail.DetailStoryActivity
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.BUNDLE_KEY_STORY
import com.dicoding.submission.imam.storyapp.utils.ext.setImageUrl
import com.dicoding.submission.imam.storyapp.utils.ext.timeStamptoString
import androidx.core.util.Pair

class StoryAdapter(private val storyList: List<Story>): RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        storyList[position].let { story ->
            holder.bind(story)
        }
    }

    override fun getItemCount(): Int = storyList.size

    inner class StoryViewHolder(private val binding: ItemRowStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
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
}