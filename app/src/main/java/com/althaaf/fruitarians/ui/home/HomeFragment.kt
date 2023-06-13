package com.althaaf.fruitarians.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.adapter.MarginItemDecoration
import com.althaaf.fruitarians.core.adapter.RecommendationAdapter
import com.althaaf.fruitarians.core.data.local.model.DataRecommendation
import com.althaaf.fruitarians.core.data.network.authentication.response.login.User
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var roleUser: String

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
        val window: Window? = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)

        setupViewModel()
        setupRecyclerView()
        setupView()
        setupAction()
    }

    private fun setupAction() {
        binding.btnFruitStore.setOnClickListener {
            val toFruitStoreActivity = HomeFragmentDirections.actionNavigationHomeToFruitStoreActivity()
            view?.findNavController()?.navigate(toFruitStoreActivity)
        }

        binding.btnMemberships.setOnClickListener { view ->
            if (roleUser != "user") {
                showToast()
            } else {
                val toMembershipActivity = HomeFragmentDirections.actionNavigationHomeToMembershipActivity()
                view?.findNavController()?.navigate(toMembershipActivity)
            }
        }

        binding.btnArticles.setOnClickListener { view ->
            val toArticleActivity = HomeFragmentDirections.actionNavigationHomeToArticleActivity()
            view.findNavController().navigate(toArticleActivity)
        }

        binding.btnFruitVariant.setOnClickListener { view ->
            val toFruitVariantActivity = HomeFragmentDirections.actionNavigationHomeToFruitVariantActivity()
            view.findNavController().navigate(toFruitVariantActivity)
        }
    }

    private fun showToast() {
        Toast.makeText(requireActivity(), "This Feature Ony For User !", Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView() {
        val margin = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        val itemDecoration = MarginItemDecoration(margin)

        val adapter = RecommendationAdapter(DataRecommendation.dataFruitRecommendation)
        binding.rvRecommendation.adapter = adapter
        binding.rvRecommendation.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendation.addItemDecoration(itemDecoration)
        binding.rvRecommendation.setHasFixedSize(true)
    }

    private fun setupView() {
        homeViewModel.getUserSession().observe(requireActivity()) {
            binding.titleMyname.text = getString(R.string.myname, it.name)
            roleUser = it.role
            Log.d("Role USER:", it.role)
        }
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(requireActivity(), HomeViewModelFactory.getInstance(requireActivity()))[HomeViewModel::class.java]
    }
}