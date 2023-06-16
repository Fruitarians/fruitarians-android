package com.althaaf.fruitarians.ui.authentication.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.authentication.request.register.RegisterRequest
import com.althaaf.fruitarians.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupText()
        setupAction()
    }

    private fun setupAction() {
        binding.buttonContinue.setOnClickListener {
            if (formIsValidated()) {
                val intent = Intent(this@RegisterActivity, RegisterContinueActivity::class.java)
                intent.putExtra(RegisterContinueActivity.EXTRA_DATA, RegisterRequest(
                    name = binding.inputRegisterName.text.toString(),
                    role = responseRole(),
                    email = binding.inputRegisterEmail.text.toString(),
                    password = binding.inputRegisterPassword.text.toString(),
                    password_konfir = binding.inputRegisterConfirmPassword.text.toString(),
                    deskripsi_alamat = "",
                    kota = "",
                    negara = "",
                    telepon = ""
                ))
                startActivity(intent)
            }
        }
    }

    private fun responseRole(): String {
        return if (binding.autoCompleteTextRole.text.toString() == "Buyer") {
            "user"
        } else if (binding.autoCompleteTextRole.text.toString() == "Seller") {
            "toko"
        } else if (binding.autoCompleteTextRole.text.toString() == "Vendor") {
            "vendor"
        } else {
            ""
        }
    }

    private fun formIsValidated(): Boolean {
        val name = binding.inputRegisterName.text.toString().trim()
        val role = binding.autoCompleteTextRole.text.toString().trim()
        val email = binding.inputRegisterEmail.text.toString().trim()
        val password = binding.inputRegisterPassword.text.toString()
        val confirmPassword = binding.inputRegisterConfirmPassword.text.toString()

        if (name.isEmpty()) {
            binding.edRegisterName.error = "Name cannot be empty"
            return false
        }

        if (role.isEmpty()) {
            binding.edRegisterRole.error = "Role cannot be empty"
            return false
        }

        if (email.isEmpty()) {
            binding.edRegisterEmail.error = "Email cannot be empty"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edRegisterEmail.error = "Invalid email format"
            return false
        }

        if (password.isEmpty() && confirmPassword.isEmpty()) {
            binding.edRegisterPassword.error = "Password cannot be empty"
            binding.edRegisterConfirmPassword.error = "Confirmation password cannot be empty"
            return false
        } else if (password.length <= 7 && confirmPassword.length <= 7) {
            binding.edRegisterPassword.error = "Password must be at least 8 characters"
            binding.edRegisterConfirmPassword.error = "Password must be at least 8 characters"
            return false
        } else if (password != confirmPassword) {
            binding.edRegisterConfirmPassword.error = "Password inputs are not the same"
            return false
        } else if (password.firstOrNull { it.isDigit() } == null) {
            binding.edRegisterPassword.error = "Password must contain at least 1 number and 1 uppercase letter"
            return false
        } else if(password.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) {
            binding.edRegisterPassword.error = "Password must contain at least 1 number and 1 uppercase letter"
            return false
        }

        binding.edRegisterName.error = null
        binding.edRegisterEmail.error = null
        binding.edRegisterRole.error = null
        binding.edRegisterPassword.error = null
        binding.edRegisterConfirmPassword.error = null
        return true
    }

    private fun setupText() {
        val fullTextHeader = getString(R.string.header_text_register)
        val targetTextHeader = getString(R.string.target_text_register)

        val spannableStringHeader = SpannableString(fullTextHeader)
        val changeColor = ForegroundColorSpan(ContextCompat.getColor(this, R.color.green))

        val startIndexHeader = fullTextHeader.indexOf(targetTextHeader)
        val endIndexHeader = startIndexHeader + targetTextHeader.length

        spannableStringHeader.setSpan(
            changeColor,
            startIndexHeader,
            endIndexHeader,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.headerTextRegister.text = spannableStringHeader

        val items = listOf("Buyer", "Seller", "Vendor")
        val adapter = ArrayAdapter(this, R.layout.list_select, items)
        (binding.autoCompleteTextRole as? AutoCompleteTextView)?.setAdapter(adapter)
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