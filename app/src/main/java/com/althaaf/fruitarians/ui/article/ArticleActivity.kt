package com.althaaf.fruitarians.ui.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.fruitarians.core.adapter.ArticleListAdapter
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        articleViewModel.getArticles().observe(this){
            it?.let { response ->
                    when (response) {
                        is ApiResult.Loading -> {
                            binding.lottieLoading.visibility = View.VISIBLE
                        }

                        is ApiResult.Success -> {
                            binding.lottieLoading.visibility = View.GONE
                            val adapter = ArticleListAdapter(response.data.result)
                            binding.rvArticle.adapter = adapter
                            binding.rvArticle.layoutManager = LinearLayoutManager(this)
                            binding.rvArticle.setHasFixedSize(true)
                        }

                        is ApiResult.Error -> {
                            binding.lottieLoading.visibility = View.GONE
                            Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                            Log.d(TAG, response.error)
                        }
                    }
            }
        }
    }

    private fun setupViewModel() {
        articleViewModel = ViewModelProvider(this, HomeViewModelFactory.getInstance(this))[ArticleViewModel::class.java]
    }

    companion object {
        private const val TAG = "ArticleActivity"
    }
}