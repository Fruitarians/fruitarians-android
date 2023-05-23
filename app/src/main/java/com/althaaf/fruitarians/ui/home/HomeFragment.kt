package com.althaaf.fruitarians.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.authentication.response.login.User
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupView()
    }

    private fun setupView() {
        homeViewModel.getUserSession().observe(requireActivity()) {
            binding.titleMyname.text = getString(R.string.myname, it.name)
        }
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(requireActivity(), HomeViewModelFactory.getInstance(requireActivity()))[HomeViewModel::class.java]
    }
}