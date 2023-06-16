package com.althaaf.fruitarians.ui.fruitstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.adapter.FruitStoreListAdapter
import com.althaaf.fruitarians.core.adapter.LoadingStateAdapter
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityFruitStoreBinding
import com.althaaf.fruitarians.ui.detailfruitstore.DetailFruitStoreActivity
import com.bumptech.glide.Glide

class FruitStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitStoreBinding
    private lateinit var fruitStoreViewModel: FruitStoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupInfo()
        setupCardData()
        setupRecyclerView()
        setupAction()
    }

    private fun setupCardData() {
        fruitStoreViewModel.getOneFruitStore().observe(this) {
            it?.let { response ->
                when(response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoadingCard.visibility = View.VISIBLE
                        binding.cardToko.alpha = 0F
                    }

                    is ApiResult.Success -> {
                        binding.cardTitle.text = response.data.data.name
                        binding.locationCard.text = "${response.data.data.alamat.negara}, ${response.data.data.alamat.kota}"
                        if (response.data.data.gambarProfil == null) {
                            binding.imageToko.setImageDrawable(ContextCompat.getDrawable(binding.imageToko.context, R.drawable.store_hd))
                            binding.imageToko.scaleType = ImageView.ScaleType.CENTER_INSIDE
                            binding.imageToko.strokeWidth = 0F
                        } else {
                            Glide.with(this)
                                .load(response.data.data.gambarProfil)
                                .into(binding.imageToko)
                        }
                        binding.cardToko.alpha = 1F
                        binding.lottieLoadingCard.visibility = View.GONE

                        binding.cardToko.setOnClickListener {
                            val intent = Intent(this@FruitStoreActivity, DetailFruitStoreActivity::class.java)
                            intent.putExtra(DetailFruitStoreActivity.EXTRA_DATA, response.data.data)
                            startActivity(intent)
                        }
                    }

                    is ApiResult.Error -> {
                        binding.cardToko.alpha = 1F
                        binding.lottieLoadingCard.visibility = View.GONE
                        Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Error: ${response.error}")
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupInfo() {
        fruitStoreViewModel.getAllDataUser().observe(this){
            it?.let { response ->
                when(response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoadingCard.visibility = View.VISIBLE
                    }

                    is ApiResult.Success -> {
                        binding.tvLocation.text = "${response.data.data.alamat.negara}, ${response.data.data.alamat.kota}"
                        binding.lottieLoadingCard.visibility = View.GONE
                    }

                    is ApiResult.Error -> {
                        binding.lottieLoadingCard.visibility = View.GONE
                        Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val adapter = FruitStoreListAdapter()
        binding.rvFruitStore.layoutManager = LinearLayoutManager(this)
        binding.rvFruitStore.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        fruitStoreViewModel.getAllFruitStore().observe(this){
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setupViewModel() {
        fruitStoreViewModel = ViewModelProvider(this, HomeViewModelFactory.getInstance(this))[FruitStoreViewModel::class.java]
    }

    companion object {
        private const val TAG = "FruitStoreActivity"
    }
}