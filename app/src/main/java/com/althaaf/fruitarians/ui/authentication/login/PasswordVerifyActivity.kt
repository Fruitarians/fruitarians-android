package com.althaaf.fruitarians.ui.authentication.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.authentication.request.login.VerifyPasswordRequest
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.AuthViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityPasswordVerifyBinding

class PasswordVerifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordVerifyBinding
    private lateinit var forgotPasswordVerifyViewModel: ForgotPasswordVerifyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.buttonVerify.setOnClickListener {
            if (formIsValidated()) {
                val verifyPasswordRequest = VerifyPasswordRequest(
                    change_password_token = binding.inputVerifyToken.text.toString().trim(),
                    password = binding.inputVerifyPassword.text.toString()
                )

                forgotPasswordVerifyViewModel.verifyPassword(verifyPasswordRequest).observe(this) {
                    it?.let { response ->
                        when(response) {
                            is ApiResult.Loading -> {
                                binding.lottieLoading.visibility = View.VISIBLE
                                binding.buttonVerify.visibility = View.GONE
                            }

                            is ApiResult.Success -> {
                                Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                                moveActivity()
                            }

                            is ApiResult.Error -> {
                                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                                binding.lottieLoading.visibility = View.GONE
                                binding.buttonVerify.visibility = View.VISIBLE
                            }
                            else -> {
                                Toast.makeText(this, "Failed, try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun moveActivity() {
        val intent = Intent(this@PasswordVerifyActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun formIsValidated(): Boolean {
        val token = binding.inputVerifyToken.text.toString()
        val password = binding.inputVerifyPassword.text.toString()
        val confirmPassword = binding.inputVerifyConfirmPassword.text.toString()

        if (token.isEmpty()) {
            binding.edVerifyToken.error = "Token cannot be empty"
            return false
        }

        if (password.isEmpty() && confirmPassword.isEmpty()) {
            binding.edVerifyPassword.error = "Password cannot be empty"
            binding.edVerifyConfirmPassword.error = "Confirmation password cannot be empty"
            return false
        } else if (password.length <= 7 && confirmPassword.length <= 7) {
            binding.edVerifyPassword.error = "Password must be at least 8 characters"
            binding.edVerifyConfirmPassword.error = "Password must be at least 8 characters"
            return false
        } else if (password != confirmPassword) {
            binding.edVerifyPassword.error = "Password inputs are not the same"
            return false
        } else if (password.firstOrNull { it.isDigit() } == null) {
            binding.edVerifyPassword.error = "Password must contain at least 1 number and 1 uppercase letter"
            return false
        } else if(password.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) {
            binding.edVerifyPassword.error = "Password must contain at least 1 number and 1 uppercase letter"
            return false
        }

        return true
    }

    private fun setupViewModel() {
        forgotPasswordVerifyViewModel = ViewModelProvider(this, AuthViewModelFactory.getInstance(this))[ForgotPasswordVerifyViewModel::class.java]
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        val dataEmail = intent.getStringExtra(EXTRA_EMAIL)
        Log.d(TAG, dataEmail.toString())
        binding.headerVerification.text = getString(R.string.email_code, dataEmail)
    }

    companion object {
        private const val TAG = "ForgotPasswordVerify"
        const val EXTRA_EMAIL = "extra_email"
    }

}