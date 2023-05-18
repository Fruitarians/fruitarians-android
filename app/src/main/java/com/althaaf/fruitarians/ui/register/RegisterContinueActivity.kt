package com.althaaf.fruitarians.ui.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.databinding.ActivityRegisterBinding
import com.althaaf.fruitarians.databinding.ActivityRegisterContinueBinding
import com.althaaf.fruitarians.ui.login.LoginActivity

class RegisterContinueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterContinueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterContinueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupText()
        setupAction()
    }

    private fun setupAction() {
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this@RegisterContinueActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupText() {
        val fullTextHeader = getString(R.string.header_text_register)
        val targetTextHeader = getString(R.string.target_text_register)

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
        binding.headerTextRegister.text = spannableStringHeader

        val items = listOf("Indonesia", "Jepang")
        val adapter = ArrayAdapter(this, R.layout.list_items, items)
        (binding.autoCompleteTextState as? AutoCompleteTextView)?.setAdapter(adapter)
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