package com.althaaf.fruitarians.ui.myvendor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.adapter.LoadingStateAdapter
import com.althaaf.fruitarians.core.adapter.PartnerVendorListAdapter
import com.althaaf.fruitarians.core.adapter.TokoBuahListAdapter
import com.althaaf.fruitarians.core.helper.MyVendorViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityMyVendorBinding
import com.althaaf.fruitarians.ui.product.ProductAddUpdateActivity

class MyVendorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyVendorBinding
    private lateinit var myVendorViewModel: MyVendorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
        setupListPartner()
    }

    override fun onResume() {
        super.onResume()
        setupListPartner()
    }

    private fun setupListPartner() {
        val adapter = PartnerVendorListAdapter()
        binding.rvVendorPartner.layoutManager = LinearLayoutManager(this)
        binding.rvVendorPartner.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )

        myVendorViewModel.getAllSubsVendor().observe(this) { data ->
            adapter.submitData(lifecycle,data)
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                binding.rvVendorPartner.visibility = View.GONE
                binding.subsNotFound.visibility = View.VISIBLE
            } else {
                binding.rvVendorPartner.visibility = View.VISIBLE
                binding.subsNotFound.visibility = View.GONE
            }
        }
    }

    private fun setupAction() {
        binding.btnAddPartner.setOnClickListener{
            val intent = Intent(this@MyVendorActivity, SubsAddUpdateActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupViewModel() {
        myVendorViewModel = ViewModelProvider(this, MyVendorViewModelFactory.getInstance(this))[MyVendorViewModel::class.java]
    }
}