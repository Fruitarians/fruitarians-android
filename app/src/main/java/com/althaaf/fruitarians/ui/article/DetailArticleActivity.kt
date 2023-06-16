package com.althaaf.fruitarians.ui.article

import android.content.Intent
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityDetailArticleBinding
import com.bumptech.glide.Glide

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding
    private lateinit var detailArticleViewModel: ArticleViewModel

    var idCardArticle = 0
    private var idArticle = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupData()
        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        binding.cardRead.setOnClickListener {
            val intent = Intent(this@DetailArticleActivity, DetailArticleActivity::class.java)
            intent.putExtra(CARD_ID, idCardArticle)
            startActivity(intent)
        }
    }

    private fun setupData() {
        if (idCardArticle != 0) {
            idArticle = idCardArticle
        } else {
            idArticle = intent.getIntExtra(EXTRA_ID, 0)
        }

        detailArticleViewModel.getArticleById(idArticle).observe(this){
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoading.visibility = View.VISIBLE
                    }

                    is ApiResult.Success -> {
                        binding.apply {
                            lottieLoading.visibility = View.GONE
                            detailTitle.text = response.data.data.title
                            detailCreator.text = getString(R.string.creator, response.data.data.author)
                            detailDate.text = getString(R.string.dated, response.data.data.createdAt)
                            detailDescription.text = response.data.data.konten
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                detailDescription.justificationMode = JUSTIFICATION_MODE_INTER_WORD
                            }
                            titleArticle.text = response.data.randomItem.title
                            descArticle.text = response.data.randomItem.konten
                        }

                        Glide.with(this)
                            .load(response.data.data.photo)
                            .into(binding.detailImg)

                        Glide.with(this)
                            .load(response.data.randomItem.photo)
                            .into(binding.imageArticle)

                        idCardArticle = response.data.randomItem.id
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
        detailArticleViewModel = ViewModelProvider(this, HomeViewModelFactory.getInstance(this))[ArticleViewModel::class.java]
    }

    companion object {
        private const val TAG = "DetailArticleActivity"
        const val EXTRA_ID = ""
        const val CARD_ID = ""
    }
}