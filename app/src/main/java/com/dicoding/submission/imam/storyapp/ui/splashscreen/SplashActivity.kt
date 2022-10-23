package com.dicoding.submission.imam.storyapp.ui.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.submission.imam.storyapp.R
import com.dicoding.submission.imam.storyapp.ui.MainActivity
import com.dicoding.submission.imam.storyapp.ui.login.LoginActivity
import com.dicoding.submission.imam.storyapp.utils.SessionManager


@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        pref = SessionManager(this)
        val isLogin = pref.isLogin

        Handler().postDelayed({
            when {
                isLogin -> {
                    MainActivity.start(this)
                    finish()
                }
                else -> {
                    LoginActivity.start(this)
                    finish()
                }
            }
        }, 2000)
    }

}