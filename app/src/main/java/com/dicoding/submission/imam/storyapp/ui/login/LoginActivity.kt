package com.dicoding.submission.imam.storyapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.submission.imam.storyapp.R
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.LoginBody
import com.dicoding.submission.imam.storyapp.databinding.ActivityLoginBinding
import com.dicoding.submission.imam.storyapp.ui.MainActivity
import com.dicoding.submission.imam.storyapp.ui.register.RegisterActivity
import com.dicoding.submission.imam.storyapp.utils.SessionManager
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.KEY_EMAIL
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.KEY_IS_LOGIN
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.KEY_TOKEN
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.KEY_USER_ID
import com.dicoding.submission.imam.storyapp.utils.TextConstValue.KEY_USER_NAME
import com.dicoding.submission.imam.storyapp.utils.ext.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    private var _activityLoginBinding: ActivityLoginBinding? = null
    private val binding get() = _activityLoginBinding!!

    private lateinit var pref: SessionManager

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }

        private val TAG = LoginActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_activityLoginBinding?.root)

        pref = SessionManager(this)

        initAct()
    }

    private fun initAct() {
        // on click button login
        binding.btnLogin.setOnClickListener {
            val userEmail = binding.editEmail.text.toString()
            val userPassword = binding.editPassword.text.toString()

            when {
                userEmail.isBlank() -> {
                    binding.editEmail.requestFocus()
                    binding.editEmail.error = getString(R.string.error_empty_email)
                }
                userPassword.isBlank() -> {
                    binding.editPassword.requestFocus()
                    binding.editPassword.error = getString(R.string.error_empty_password)
                }
                else -> {
                    val req = LoginBody(
                        userEmail, userPassword
                    )
                    loginUser(req, userEmail)
                }
            }
        }

        // to register page
        binding.tvRegister.setOnClickListener {
            RegisterActivity.start(this)
        }
    }

    private fun loginUser(req: LoginBody, email: String) {
        lifecycleScope.launch {
            loginViewModel.loginUser(req).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        showLoading(true)
                    }
                    is ApiResponse.Success -> {
                        try {
                            showLoading(false)
                            val userData = response.data.loginResult
                            // set session di preference
                            pref.apply {
                                setStringPreference(KEY_USER_ID, userData.userId)
                                setStringPreference(KEY_TOKEN, userData.token)
                                setStringPreference(KEY_USER_NAME, userData.name)
                                setStringPreference(KEY_EMAIL, email)
                                setBooleanPreference(KEY_IS_LOGIN, true)
                            }
                        } finally {
                            MainActivity.start(this@LoginActivity)
                            finish()
                        }
                    }
                    is ApiResponse.Error -> {
                        showLoading(false)
                        Timber.tag(TAG).e(response.errorMessage)
                    }
                    else -> {
                        showToast(getString(R.string.message_unknown_error))
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.editEmail.isClickable = !isLoading
        binding.editEmail.isEnabled = !isLoading
        binding.editPassword.isClickable = !isLoading
        binding.editPassword.isEnabled = !isLoading
        binding.btnLogin.isClickable = !isLoading
    }
}