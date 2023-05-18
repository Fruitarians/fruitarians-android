package com.althaaf.fruitarians.ui.login

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.WindowInsets
import android.view.WindowManager
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupText()
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