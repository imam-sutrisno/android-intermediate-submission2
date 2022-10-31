package com.dicoding.submission.imam.storyapp.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.submission.imam.storyapp.R
import com.dicoding.submission.imam.storyapp.data.remote.ApiResponse
import com.dicoding.submission.imam.storyapp.data.remote.auth.RegBody
import com.dicoding.submission.imam.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.submission.imam.storyapp.ui.login.LoginActivity
import com.dicoding.submission.imam.storyapp.utils.TimeConstValue
import com.dicoding.submission.imam.storyapp.utils.ext.showOkDialog
import com.dicoding.submission.imam.storyapp.utils.ext.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()

    private var _activityRegisterBinding: ActivityRegisterBinding? = null
    private val binding get() = _activityRegisterBinding!!

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }

        private val TAG = RegisterActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_activityRegisterBinding?.root)

        initAct()
    }

    private fun initAct() {
        binding.btnRegister.setOnClickListener {
            val userName = binding.editName.text.toString()
            val userEmail = binding.editEmail.text.toString()
            val userPassword = binding.editPassword.text.toString()

            Handler(Looper.getMainLooper()).postDelayed({
                when {
                    userName.isBlank() -> binding.editName.error =
                        getString(R.string.error_empty_name)
                    userEmail.isBlank() -> binding.editEmail.error =
                        getString(R.string.error_empty_email)
                    userPassword.isBlank() -> binding.editPassword.error =
                        getString(R.string.error_empty_password)
                    else -> {
                        val req = RegBody(
                            userName, userEmail, userPassword
                        )
                        registerUser(req)
                    }
                }
            }, TimeConstValue.ACTION_DELAYED_TIME)
        }
        binding.tvLogin.setOnClickListener {
            LoginActivity.start(this)
        }
    }

    private fun registerUser(req: RegBody) {
        lifecycleScope.launch {
            registerViewModel.registerUser(req).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        showLoading(true)
                    }
                    is ApiResponse.Success -> {
                        try {
                            showLoading(false)
                        } finally {
                            LoginActivity.start(this@RegisterActivity)
                            finish()
                            showToast(getString(R.string.message_register_success))
                            val messageRes = response.data.body()?.message
                            Timber.tag(TAG).i(messageRes.toString())
                        }
                    }
                    is ApiResponse.Error -> {
                        showLoading(false)
                        Timber.tag(TAG).e(response.errorMessage)
                        showOkDialog(getString(R.string.title_dialog_error), response.errorMessage)
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
        binding.editName.isClickable = !isLoading
        binding.editName.isEnabled = !isLoading
        binding.editEmail.isClickable = !isLoading
        binding.editEmail.isEnabled = !isLoading
        binding.editPassword.isClickable = !isLoading
        binding.editPassword.isEnabled = !isLoading
        binding.btnRegister.isClickable = !isLoading
    }
}