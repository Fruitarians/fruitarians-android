package com.althaaf.fruitarians.ui.detailfruitstore

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.cart.CartRequest
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.core.helper.formatToFormattedString
import com.althaaf.fruitarians.databinding.ActivityDetailSpecificFruitBinding
import com.bumptech.glide.Glide

class DetailSpecificFruitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSpecificFruitBinding
    private lateinit var detailFruitStoreViewModel: DetailFruitStoreViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSpecificFruitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupDataDetail()
        setupAction()
    }

    private fun setupAction() {
        val idBuah = intent.getStringExtra(EXTRA_ID_BUAH) ?: ""
        val idToko = intent.getStringExtra(EXTRA_ID_TOKO) ?: ""


        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        binding.btnCart.setOnClickListener {
            detailFruitStoreViewModel.addFruitToCart(cartRequest = CartRequest(idBuah, idToko)).observe(this) {
                it?.let { response ->
                    when(response) {
                        is ApiResult.Loading -> {
                            binding.lottieLoading.visibility = View.VISIBLE
                        }

                        is ApiResult.Success -> {
                            binding.lottieLoading.visibility = View.GONE
                            Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                        }

                        is ApiResult.Error -> {
                            binding.lottieLoading.visibility = View.GONE
                            Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "Error: ${response.error}")
                        }
                    }
                }
            }
        }
    }

    private fun setupDataDetail() {
        val idBuah = intent.getStringExtra(EXTRA_ID_BUAH)
        val idToko = intent.getStringExtra(EXTRA_ID_TOKO)

        if (idBuah != null && idToko != null) {
            detailFruitStoreViewModel.getSpecificFruit(idToko, idBuah).observe(this) {
                it?.let { response ->
                    when (response) {
                        is ApiResult.Loading -> {
                            binding.lottieLoading.visibility = View.VISIBLE
                        }

                        is ApiResult.Success -> {
                            val formatPrice = response.data.buah?.harga?.formatToFormattedString()

                            binding.specificToko.text = response.data.toko.name
                            binding.specificBuah.text = response.data.buah?.name
                            binding.specificStock.text = getString(R.string.stock, response.data.buah?.stok)
                            binding.specificUnit.text = getString(R.string.unit, response.data.buah?.satuan)
                            binding.specificPrice.text = getString(R.string.priceFormat, formatPrice)
                            binding.specificDescription.text = response.data.buah?.deskripsi

                            Glide.with(this)
                                .load(response.data.buah?.gambar)
                                .into(binding.specificImage)

                            binding.lottieLoading.visibility = View.GONE

                            binding.btnMoveWa.setOnClickListener {
                                val order =
                                    "Selamat malam mas/mba, benarkah ini dari Toko buah '${response.data.toko.name}', saya ingin memesan ${response.data.buah?.name}, apakah masih tersedia?"
                                val url =
                                    "https://api.whatsapp.com/send?phone=+62${response.data.toko.telepon}&text=$order"
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(url)
                                startActivity(intent)
                            }
                        }

                        is ApiResult.Error -> {
                            binding.lottieLoading.visibility = View.GONE
                            Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "Error: ${response.error}")
                        }
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        detailFruitStoreViewModel = ViewModelProvider(
            this,
            HomeViewModelFactory.getInstance(this)
        )[DetailFruitStoreViewModel::class.java]
    }

    companion object {
        private const val TAG = "DetailSpecificFruitActivity"
        const val EXTRA_ID_BUAH = "extra_id_buah"
        const val EXTRA_ID_TOKO = "extra_id_toko"
    }
}