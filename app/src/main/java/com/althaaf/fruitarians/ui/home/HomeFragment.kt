package com.althaaf.fruitarians.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        setupRecyclerView()
        setupView()
        setupAction()
    }

    private fun setupAction() {
        binding.btnFruitStore.setOnClickListener {
            val toFruitStoreActivity = HomeFragmentDirections.actionNavigationHomeToFruitStoreActivity()
            view?.findNavController()?.navigate(toFruitStoreActivity)
        }

        binding.btnMemberships.setOnClickListener {view ->
            val toMembershipActivity = HomeFragmentDirections.actionNavigationHomeToMembershipActivity()
            view?.findNavController()?.navigate(toMembershipActivity)
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
        }
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(requireActivity(), HomeViewModelFactory.getInstance(requireActivity()))[HomeViewModel::class.java]
    }
}