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
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.authentication.request.login.SentTokenRequest
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.AuthViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupText()
        setupAction()
        setupViewModel()
    }

    private fun setupAction() {
        binding.buttonSentEmail.setOnClickListener {
            if (formIsValidated()) {
                val sentTokenRequest = SentTokenRequest(email = binding.inputSentEmail.text.toString().trim())
                forgotPasswordViewModel.sentToken(sentTokenRequest).observe(this) {
                    it?.let { response ->
                        when(response) {
                            is ApiResult.Loading -> {
                                binding.lottieLoading.visibility = View.VISIBLE
                                binding.buttonSentEmail.visibility = View.GONE
                            }

                            is ApiResult.Success -> {
                                val emailSent = response.data.data.user.email
                                Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                                moveActivity(emailSent)
                            }

                            is ApiResult.Error -> {
                                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                                binding.lottieLoading.visibility = View.GONE
                                binding.buttonSentEmail.visibility = View.VISIBLE
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

    private fun moveActivity(emailSent: String) {
        val intent = Intent(this@ForgotPasswordActivity, PasswordVerifyActivity::class.java)
        intent.putExtra(PasswordVerifyActivity.EXTRA_EMAIL, emailSent)
        startActivity(intent)
        finish()
    }


    private fun formIsValidated(): Boolean {
        val email = binding.inputSentEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.edSentEmail.error = "Email cannot be empty"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edSentEmail.error = "Invalid email format"
            return false
        }

        binding.edSentEmail.error = null
        return true
    }

    private fun setupViewModel() {
        forgotPasswordViewModel = ViewModelProvider(this, AuthViewModelFactory.getInstance(this))[ForgotPasswordViewModel::class.java]
    }

    private fun setupText() {
        val fullTextHeader = getString(R.string.token_have)
        val targetTextHeader = getString(R.string.token)

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
        binding.tvHaveToken.text = spannableStringHeader
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