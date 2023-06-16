package com.althaaf.fruitarians.ui.fruitvariant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.althaaf.fruitarians.core.adapter.FruitVariantListAdapter
import com.althaaf.fruitarians.core.adapter.LoadingStateAdapter
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityFruitVariantBinding

class FruitVariantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitVariantBinding
    private lateinit var fruitVariantViewModel: FruitVariantViewModel

    private var searchResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitVariantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupData()
        setupAllFruit()
        setupAction()
        searchHandler()
    }

    private fun setupData() {
        val extraSearch = intent.getStringExtra(EXTRA_SEARCH) ?: ""
        if (extraSearch != "") {
            binding.tvSearch.visibility = View.VISIBLE
            binding.btnResultSearch.visibility = View.VISIBLE
            binding.btnResultSearch.text = extraSearch
            searchResult = extraSearch
        }
    }

    private fun searchHandler() {
        binding.etSearch.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchResult = s.toString()
                setupAllFruit()
            }
        })
    }


    private fun setupAllFruit() {
        val adapter = FruitVariantListAdapter(this)
        binding.rvFruitVariant.layoutManager = GridLayoutManager(this, 2)
        binding.rvFruitVariant.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        fruitVariantViewModel.getAllFruit(q = searchResult).observe(this) {
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

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        binding.btnResultSearch.setOnClickListener {
            binding.tvSearch.visibility = View.GONE
            binding.btnResultSearch.visibility = View.GONE
            searchResult = ""
            setupAllFruit()
        }
    }

    private fun setupViewModel() {
        fruitVariantViewModel = ViewModelProvider(
            this,
            HomeViewModelFactory.getInstance(this)
        )[FruitVariantViewModel::class.java]
    }

    companion object {
        const val EXTRA_SEARCH = "extra_search"
    }
}