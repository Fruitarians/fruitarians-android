package com.althaaf.fruitarians.ui.changepassword

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.authentication.request.register.RegisterRequest
import com.althaaf.fruitarians.core.data.network.profile.changepassword.ChangePasswordRequest
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.ProfileViewModelFactory
import com.althaaf.fruitarians.databinding.FragmentChangePasswordBinding
import com.althaaf.fruitarians.ui.authentication.register.RegisterContinueActivity
import com.althaaf.fruitarians.ui.edit_profile.EditProfileFragmentArgs
import com.bumptech.glide.Glide

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window? = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.green)

        setupViewModel()
        setupAction()
        setupImage()
    }

    private fun setupImage() {
        val imageUser = ChangePasswordFragmentArgs.fromBundle(arguments as Bundle).userImage

        if (imageUser != null) {
            Glide.with(this)
                .load(imageUser)
                .into(binding.imgProfile)
            binding.imgProfile.elevation = 6F

        } else {
            binding.imgProfile.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.imgProfile.context,
                    R.drawable.default_img_toko
                )
            )
            binding.imgProfile.elevation = 6F
        }

    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnChangePassword.setOnClickListener {
            if (formIsValidated()) {

                val changePasswordRequest = ChangePasswordRequest(
                    password_lama = binding.inputOldPassword.text.toString().trim(),
                    password_baru = binding.inputChangeNewPassword.text.toString().trim()
                )

                changePasswordViewModel.changePasswordUser(changePasswordRequest = changePasswordRequest).observe(requireActivity()) {
                    it?.let { response ->
                        when(response) {
                            is ApiResult.Loading -> {
                                binding.lottieLoading.visibility = View.VISIBLE
                                binding.btnChangePassword.visibility = View.GONE
                            }

                            is ApiResult.Success -> {
                                binding.lottieLoading.visibility = View.GONE
                                binding.btnChangePassword.visibility = View.VISIBLE
                                Toast.makeText(requireActivity(), response.data.message, Toast.LENGTH_SHORT).show()
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                            }

                            is ApiResult.Error -> {
                                Toast.makeText(requireActivity(), response.error, Toast.LENGTH_SHORT).show()
                                binding.lottieLoading.visibility = View.GONE
                                binding.btnChangePassword.visibility = View.VISIBLE
                            }
                            else -> {
                                Toast.makeText(requireActivity(), "Failed, try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun formIsValidated(): Boolean {
        val oldPassword = binding.inputOldPassword.text.toString()
        val newPassword = binding.inputChangeNewPassword.text.toString()
        val confirmPassword = binding.inputConfirmPassword.text.toString()

        if (newPassword.isEmpty()) {
            binding.edChangeNewPassword.error = "Confirmation password cannot be empty"
            return false
        } else if (oldPassword.isEmpty()) {
            binding.edOldPassword.error = "Old password cannot be empty"
            return false
        }else if (confirmPassword.isEmpty()) {
            binding.edRegisterConfirmPassword.error = "Old password cannot be empty"
            return false
        } else if (oldPassword.length <= 7) {
            binding.edOldPassword.error = "Password must be at least 8 characters"
            return false
        } else if (newPassword.length <= 7) {
            binding.edChangeNewPassword.error = "Password must be at least 8 characters"
            return false
        } else if (newPassword != confirmPassword) {
            binding.edRegisterConfirmPassword.error = "Password inputs are not the same"
            return false
        } else if (newPassword.firstOrNull { it.isDigit() } == null) {
            binding.edChangeNewPassword.error = "Password must contain at least 1 number and 1 uppercase letter"
            return false
        } else if(newPassword.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) {
            binding.edChangeNewPassword.error = "Password must contain at least 1 number and 1 uppercase letter"
            return false
        }

        binding.edChangeNewPassword.error = null
        binding.edOldPassword.error = null
        binding.edRegisterConfirmPassword.error = null
        return true
    }

    private fun setupViewModel() {
        changePasswordViewModel = ViewModelProvider(
            requireActivity(),
            ProfileViewModelFactory.getInstance(requireActivity())
        )[ChangePasswordViewModel::class.java]
    }
}