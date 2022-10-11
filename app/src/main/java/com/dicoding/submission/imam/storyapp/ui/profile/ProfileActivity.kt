package com.dicoding.submission.imam.storyapp.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.dicoding.submission.imam.storyapp.databinding.ActivityProfileBinding
import com.dicoding.submission.imam.storyapp.ui.login.LoginActivity
import com.dicoding.submission.imam.storyapp.utils.SessionManager

class ProfileActivity : AppCompatActivity() {

    private var _activityProfileBinding: ActivityProfileBinding? = null
    private val binding get() = _activityProfileBinding!!

    private lateinit var pref: SessionManager

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityProfileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(_activityProfileBinding?.root)

        pref = SessionManager(this)
        initUI()
        initAct()
    }

    private fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Profile"

        binding.tvUserName.text = pref.getUserName
        binding.tvUserEmail.text = pref.getEmail
    }

    private fun initAct() {
        binding.btnLogout.setOnClickListener {
            openLogoutDialog()
        }
    }

    private fun openLogoutDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Are you sure want logout ?")
            ?.setPositiveButton("Logout") { _, _ ->
                pref.clearPreferences()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finishAffinity()
            }
            ?.setNegativeButton("Cancel", null)
        val alert = alertDialog.create()
        alert.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}