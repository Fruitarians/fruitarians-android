package com.althaaf.fruitarians.ui.mystore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.fruitarians.core.adapter.LoadingStateAdapter
import com.althaaf.fruitarians.core.adapter.TokoBuahListAdapter
import com.althaaf.fruitarians.core.helper.MyStoreViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityMyStoreBinding
import com.althaaf.fruitarians.ui.product.ProductAddUpdateActivity

class MyStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyStoreBinding
    private lateinit var myStoreViewModel: MyStoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
        setupListBuah()
    }

    override fun onResume() {
        super.onResume()
        setupListBuah()
    }

    private fun setupListBuah() {
        val adapter = TokoBuahListAdapter(this)
        binding.rvProductStore.layoutManager = LinearLayoutManager(this)
        binding.rvProductStore.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        myStoreViewModel.getTokoBuah().observe(this) { data ->
            adapter.submitData(lifecycle,data)
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                binding.rvProductStore.visibility = View.GONE
                binding.productNotFound.visibility = View.VISIBLE
            } else {
                binding.rvProductStore.visibility = View.VISIBLE
                binding.productNotFound.visibility = View.GONE
            }
        }
    }

    private fun setupViewModel() {
        myStoreViewModel = ViewModelProvider(this, MyStoreViewModelFactory.getInstance(this))[MyStoreViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnAddProduct.setOnClickListener{
            val intent = Intent(this@MyStoreActivity, ProductAddUpdateActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }
}