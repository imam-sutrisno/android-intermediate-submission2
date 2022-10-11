package com.dicoding.submission.imam.storyapp.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StoryService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}