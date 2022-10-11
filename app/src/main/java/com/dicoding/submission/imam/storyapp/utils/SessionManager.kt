package com.dicoding.submission.imam.storyapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.KEY_EMAIL
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.KEY_IS_LOGIN
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.KEY_TOKEN
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.KEY_USER_ID
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.KEY_USER_NAME
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.PREFS_NAME

class SessionManager(context: Context) {
    private var preferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun setStringPreference(prefKey: String, value: String) {
        editor.putString(prefKey, value)
        editor.apply()
    }

    fun setIntPreference(prefKey: String, value: Int) {
        editor.putInt(prefKey, value)
        editor.apply()
    }

    fun setBooleanPreference(prefKey: String, value: Boolean) {
        editor.putBoolean(prefKey, value)
        editor.apply()
    }

    fun clearPreferenceByKey(prefKey: String) {
        editor.remove(prefKey)
        editor.apply()
    }

    fun clearPreferences() {
        editor.clear().apply()
    }

    val getToken = preferences.getString(KEY_TOKEN, "")
    val getUserId = preferences.getString(KEY_USER_ID, "")
    val isLogin = preferences.getBoolean(KEY_IS_LOGIN, false)
    val getUserName = preferences.getString(KEY_USER_NAME, "")
    val getEmail = preferences.getString(KEY_EMAIL, "")
}