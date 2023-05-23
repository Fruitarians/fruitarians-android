package com.althaaf.fruitarians.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.MainActivity
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.ProfileViewModelFactory
import com.althaaf.fruitarians.databinding.FragmentHomeBinding
import com.althaaf.fruitarians.databinding.FragmentProfileBinding
import com.althaaf.fruitarians.ui.authentication.login.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProvider(
            requireActivity(),
            ProfileViewModelFactory.getInstance(requireActivity())
        )[ProfileViewModel::class.java]
    }

    private fun setupAction() {
        binding.buttonLogout.setOnClickListener {
            profileViewModel.logout().observe(requireActivity()) {
                it?.let { response ->
                    when (response) {
                        is ApiResult.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.buttonLogout.visibility = View.GONE
                        }

                        is ApiResult.Success -> {
                            Toast.makeText(
                                requireActivity(),
                                response.data.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            profileViewModel.clearUserSession()
                            moveActivity()
                        }

                        is ApiResult.Error -> {
                            Toast.makeText(requireActivity(), response.error, Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBar.visibility = View.GONE
                            binding.buttonLogout.visibility = View.VISIBLE
                        }
                        else -> {
                            Toast.makeText(
                                requireActivity(),
                                "Failed, try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun moveActivity() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}