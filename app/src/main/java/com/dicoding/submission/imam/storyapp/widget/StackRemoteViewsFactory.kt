package com.dicoding.submission.imam.storyapp.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.room.Room
import com.dicoding.submission.imam.storyapp.R
import com.dicoding.submission.imam.storyapp.data.local.StoryAppDatabase
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity
import com.dicoding.submission.imam.storyapp.utils.urlToBitmap

class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private var stories: MutableList<StoryEntity> = mutableListOf()

    override fun onCreate() {
        // do nothing
    }

    override fun onDataSetChanged() {
        val database = Room.databaseBuilder(
            mContext.applicationContext, StoryAppDatabase::class.java,
            "storyApp_db"
        ).build()

        database.getStoryDao().getAllStories().forEach {
            stories.add(
                StoryEntity(
                    it.id,
                    it.name,
                    it.description,
                    it.photoUrl,
                    it.createdAt,
                    it.lat,
                    it.lon
                )
            )
        }
    }

    override fun onDestroy() {
        // do nothing
    }

    override fun getCount(): Int = stories.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, urlToBitmap(stories[position].photoUrl))

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}