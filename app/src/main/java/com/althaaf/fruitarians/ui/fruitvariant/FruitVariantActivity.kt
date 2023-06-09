package com.althaaf.fruitarians.ui.fruitvariant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.adapter.FruitVariantListAdapter
import com.althaaf.fruitarians.core.adapter.LoadingStateAdapter
import com.althaaf.fruitarians.core.adapter.ProductFruitListAdapter
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityFruitVariantBinding

class FruitVariantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitVariantBinding
    private lateinit var fruitVariantViewModel: FruitVariantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitVariantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAllFruit(query = null)
        setupAction()
        searchHandler()
    }

    private fun searchHandler() {
        binding.etSearch.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                setupAllFruit(query)
            }
        })
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupAllFruit(query: String?) {
        val adapter = FruitVariantListAdapter(this)
        binding.rvFruitVariant.layoutManager = GridLayoutManager(this, 2)
        binding.rvFruitVariant.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        fruitVariantViewModel.getAllFruit(q = query).observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                binding.rvFruitVariant.visibility = View.GONE
                binding.noProduct.visibility = View.VISIBLE
            } else {
                binding.rvFruitVariant.visibility = View.VISIBLE
                binding.noProduct.visibility = View.GONE
            }
        }

    }

    private fun setupViewModel() {
        fruitVariantViewModel = ViewModelProvider(
            this,
            HomeViewModelFactory.getInstance(this)
        )[FruitVariantViewModel::class.java]
    }
}