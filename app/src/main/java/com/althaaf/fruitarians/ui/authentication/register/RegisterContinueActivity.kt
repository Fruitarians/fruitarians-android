package com.althaaf.fruitarians.ui.authentication.register

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_DATA
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.authentication.request.register.RegisterRequest
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.AuthViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityRegisterBinding
import com.althaaf.fruitarians.databinding.ActivityRegisterContinueBinding
import com.althaaf.fruitarians.ui.authentication.login.LoginActivity

class RegisterContinueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterContinueBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterContinueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupText()
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(this, AuthViewModelFactory.getInstance(this))[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        val dataRegister = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, RegisterRequest::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        binding.buttonRegister.setOnClickListener {
            if (dataRegister != null) {
                val registerRequest = RegisterRequest(
                    email = dataRegister.email,
                    password = dataRegister.password,
                    password_konfir = dataRegister.password_konfir,
                    name = dataRegister.name,
                    role = dataRegister.role,
                    telepon = binding.inputRegisterPhoneNumber.text.toString().trim(),
                    negara = binding.autoCompleteTextState.text.toString().trim(),
                    kota = binding.inputRegisterCity.text.toString().trim(),
                    deskripsi_alamat = binding.inputRegisterAddress.text.toString().trim()
                )

                Log.d(TAG, "email: ${dataRegister.email}," +
                        " password: ${dataRegister.password}," +
                        " conf_password: ${dataRegister.password_konfir}," +
                        " name: ${dataRegister.name}," +
                        " role: ${dataRegister.role}"
                )

                if (formIsValidated()) {
                    registerViewModel.registerUser(registerRequest = registerRequest).observe(this) {
                        it?.let { response ->
                            when(response) {
                                is ApiResult.Loading -> {
                                    binding.lottieLoading.visibility = View.VISIBLE
                                    binding.buttonRegister.visibility = View.GONE
                                }

                                is ApiResult.Success -> {
                                    Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                                    moveActivity()
                                }

                                is ApiResult.Error -> {
                                    Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                                    binding.lottieLoading.visibility = View.GONE
                                    binding.buttonRegister.visibility = View.VISIBLE
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
    }

    private fun moveActivity() {
        val intent = Intent(this@RegisterContinueActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun formIsValidated(): Boolean {
        val address = binding.inputRegisterAddress.text.toString().trim()
        val city = binding.inputRegisterCity.text.toString().trim()
        val state = binding.autoCompleteTextState.text.toString().trim()
        val phoneNumber = binding.inputRegisterPhoneNumber.text.toString().trim()

        if (address.isEmpty()) {
            binding.edRegisterAddress.error = "Address cannot be empty"
            return false
        }

        if (city.isEmpty()) {
            binding.edRegisterCity.error = "City cannot be empty"
            return false
        }

        if (state.isEmpty()) {
            binding.edRegisterState.error = "State cannot be empty"
            return false
        }

        if (phoneNumber.isEmpty()) {
            binding.edRegisterPhoneNumber.error = "Phone cannot be empty"
            return false
        }

        return true
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

    companion object {
        const val EXTRA_DATA = "extra_data"
        private const val TAG = "RegisterContinueActivit"
    }
}