package com.althaaf.fruitarians.ui.authentication.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.MainActivity
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.authentication.request.login.LoginRequest
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.AuthViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityLoginBinding
import com.althaaf.fruitarians.ui.authentication.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupText()
        setupAction()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(this, AuthViewModelFactory.getInstance(this))[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.forgetPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            val loginRequest = LoginRequest(
                email = binding.inputLoginEmail.text.toString().trim(),
                password = binding.inputLoginPassword.text.toString().trim()
            )

            if (formIsValidated()) {
                loginViewModel.loginUser(loginRequest).observe(this) {
                    it?.let { response ->
                        when(response) {
                            is ApiResult.Loading -> {
                                binding.lottieLoading.visibility = View.VISIBLE
                                binding.buttonLogin.visibility = View.GONE
                            }

                            is ApiResult.Success -> {
                                Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                                moveActivity()
                            }

                            is ApiResult.Error -> {
                                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                                binding.lottieLoading.visibility = View.GONE
                                binding.buttonLogin.visibility = View.VISIBLE
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
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun formIsValidated(): Boolean {
        val email = binding.inputLoginEmail.text.toString().trim()
        val password = binding.inputLoginPassword.text.toString()

        if (email.isEmpty()) {
            binding.edLoginEmail.error = "Email cannot be empty"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edLoginEmail.error = "Invalid email format"
            return false
        }

        if (password.isEmpty()) {
            binding.edLoginPassword.error = "Password cannot be empty"
            return false
        } else if (password.length <= 7) {
            binding.edLoginPassword.error = "Password must be at least 8 characters"
            return false
        }

        return true
    }


    private fun setupText() {
        val fullTextHeader = getString(R.string.header_text_login)
        val targetTextHeader = getString(R.string.login_text)

        val spannableStringHeader = SpannableString(fullTextHeader)
        val changeColor = ForegroundColorSpan(resources.getColor(R.color.green))

        val startIndexHeader = fullTextHeader.indexOf(targetTextHeader)
        val endIndexHeader = startIndexHeader + targetTextHeader.length

        spannableStringHeader.setSpan(
            changeColor,
            startIndexHeader,
            endIndexHeader,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.headerText.text = spannableStringHeader

        val fulltextSignUp = getString(R.string.tv_signup)
        val targetTextSignUp = getString(R.string.sign_up)

        val spannableStringSignUp = SpannableString(fulltextSignUp)
        val changeColorSignUp = ForegroundColorSpan(resources.getColor(R.color.green))

        val startIndexSignUp = fulltextSignUp.indexOf(targetTextSignUp)
        val endIndexSignUp = startIndexSignUp + targetTextSignUp.length

        spannableStringSignUp.setSpan(
            changeColorSignUp,
            startIndexSignUp,
            endIndexSignUp,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvSignup.text = spannableStringSignUp
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
    }
}