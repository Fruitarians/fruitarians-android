package com.althaaf.fruitarians.ui.detailfruitstore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.adapter.LoadingStateAdapter
import com.althaaf.fruitarians.core.adapter.ProductFruitListAdapter
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.databinding.FragmentDetailProductBinding

class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailFruitStoreViewModel: DetailFruitStoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailProductBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
    }

    private fun setupViewModel() {
        detailFruitStoreViewModel = ViewModelProvider(requireActivity(), HomeViewModelFactory.getInstance(requireActivity()))[DetailFruitStoreViewModel::class.java]
    }

    private fun setupRecyclerView() {
        val dataId = arguments?.getString(ARG_DATA_ID)

        if (dataId != null) {
            val adapter = ProductFruitListAdapter(requireActivity(), dataId)
            binding.rvListProduct.layoutManager = GridLayoutManager(requireActivity(), 2)
            binding.rvListProduct.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )

            detailFruitStoreViewModel.getDetailFruitStore(dataId).observe(requireActivity()) {
                adapter.submitData(lifecycle, it)
            }

            adapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                    binding.rvListProduct.visibility = View.GONE
                    binding.noProduct.visibility = View.VISIBLE
                } else {
                    binding.rvListProduct.visibility = View.VISIBLE
                    binding.noProduct.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        const val ARG_DATA_ID = "data"
    }

}