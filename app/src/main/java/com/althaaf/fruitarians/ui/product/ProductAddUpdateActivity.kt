package com.althaaf.fruitarians.ui.product

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.profile.deleteproduct.deleteRequest
import com.althaaf.fruitarians.core.data.network.profile.mystore.BuahItem
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.MyStoreViewModelFactory
import com.althaaf.fruitarians.core.helper.reduceFileImage
import com.althaaf.fruitarians.core.helper.uriToFile
import com.althaaf.fruitarians.databinding.ActivityProductAddUpdateBinding
import com.althaaf.fruitarians.ui.mystore.MyStoreActivity
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProductAddUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductAddUpdateBinding
    private lateinit var productAddUpdateViewModel: ProductAddUpdateViewModel
    private var getFile: File? = null
    private var buahItem: BuahItem? = null
    private var isEdit = false
    private lateinit var buahId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupLayout()
        setupData()
        setupAction()
    }

    private fun setupLayout() {
        buahItem = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DETAIL, BuahItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DETAIL)
        }

        isEdit = buahItem != null
        val toolbarTitle: String
        val btnTitle: String

        if (isEdit) {
            toolbarTitle = " Update Product"
            btnTitle = "Update Product"
            binding.btnDelete.visibility = View.VISIBLE
            if (buahItem != null) {
                buahItem?.let { buahItem ->
                    Glide.with(this)
                        .load(buahItem.gambar)
                        .into(binding.uploadPhotoProduct)
                    binding.uploadPhotoProduct.layoutParams.height = 550
                    binding.uploadPhotoProduct.scaleType = ImageView.ScaleType.FIT_CENTER
                    binding.tvUpload.visibility = View.GONE

                    binding.inputProductName.setText(buahItem.name)
                    binding.inputProductDesc.setText(buahItem.deskripsi)
                    binding.inputProductStock.setText(buahItem.stok.toString())
                    binding.inputProductPrice.setText(buahItem.harga.toString())
                    binding.autoCompleteTextUnit.setText(buahItem.satuan)
                    buahId = buahItem.id
                }
            }
        } else {
            binding.btnDelete.visibility = View.GONE
            toolbarTitle = " Add New Product"
            btnTitle = "Add New Product"
        }

        binding.toolbarTitle.text = toolbarTitle
        binding.btnAddNewProduct.text = btnTitle
    }

    private fun setupData() {
        val units = listOf("Kilogram", "Gram", "Newton")
        val adapterState = ArrayAdapter(this, R.layout.list_select, units)
        (binding.autoCompleteTextUnit as? AutoCompleteTextView)?.setAdapter(adapterState)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this)
                getFile = myFile
                binding.uploadPhotoProduct.setImageURI(uri)
                binding.uploadPhotoProduct.layoutParams.height = 550
                binding.uploadPhotoProduct.scaleType = ImageView.ScaleType.FIT_CENTER
                binding.tvUpload.visibility = View.GONE
            }
        }
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        binding.btnDelete.setOnClickListener {
            showAlertDialog()
        }

        binding.uploadPhotoProduct.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }

        binding.btnAddNewProduct.setOnClickListener {
            val name = binding.inputProductName.text.toString().trim()
            val description = binding.inputProductDesc.text.toString().trim()
            val price = binding.inputProductPrice.text.toString()
            val stock = binding.inputProductStock.text.toString()
            val unit = binding.autoCompleteTextUnit.text.toString()

            when {
                name.isEmpty() -> {
                    binding.edProductName.error = "Please fill out this field"
                }

                description.isEmpty() -> {
                    binding.edProductDesc.error = "Please fill this field"
                }

                price.isEmpty() -> {
                    binding.edProductPrice.error = "Please fill out this field"
                }

                stock.isEmpty() -> {
                    binding.edProductStock.error = "Please fill out this field"
                }

                unit.isEmpty() -> {
                    binding.edAddUnit.error = "Please fill out this field"
                }

                else -> {

                    if (isEdit) {
                        if (getFile != null) {
                            val file = reduceFileImage(getFile as File)
                            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                            val imageMultipart: MultipartBody.Part =
                                MultipartBody.Part.createFormData(
                                    "file",
                                    file.name,
                                    requestImageFile
                                )

                            updateProduct(
                                buahId.toRequestBody("text/plain".toMediaType()),
                                name.toRequestBody("text/plain".toMediaType()),
                                price.toInt(),
                                unit.toRequestBody("text/plain".toMediaType()),
                                stock.toInt(),
                                description.toRequestBody("text/plain".toMediaType()),
                                imageMultipart
                            )
                        } else {
                            updateProduct(
                                buahId.toRequestBody("text/plain".toMediaType()),
                                name.toRequestBody("text/plain".toMediaType()),
                                price.toInt(),
                                unit.toRequestBody("text/plain".toMediaType()),
                                stock.toInt(),
                                description.toRequestBody("text/plain".toMediaType()),
                                null
                            )
                        }
                    } else {
                        if (getFile != null) {
                            val file = reduceFileImage(getFile as File)
                            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                            val imageMultipart: MultipartBody.Part =
                                MultipartBody.Part.createFormData(
                                    "file",
                                    file.name,
                                    requestImageFile
                                )

                            addNewProduct(
                                name.toRequestBody("text/plain".toMediaType()),
                                price.toInt(),
                                unit.toRequestBody("text/plain".toMediaType()),
                                description.toRequestBody("text/plain".toMediaType()),
                                stock.toInt(),
                                imageMultipart
                            )
                        } else {
                            Toast.makeText(this, "Please insert an image first", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun showAlertDialog() {
        val message = "Are you sure you want to delete this item?"
        val title = "Delete"

        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            setPositiveButton("Yes") { _, _ ->
                deleteItem()
            }
            setNegativeButton("No") { dialog, _ -> dialog.cancel()}
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun deleteItem() {
        Log.d(TAG, "Buah ID : $buahId")
        productAddUpdateViewModel.deleteProductBuah(buahId).observe(this){
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {}

                    is ApiResult.Success -> {
                        Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                        moveActivity()
                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, response.error)
                    }
                    else -> {
                        Toast.makeText(this, "Failed, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateProduct(
        buahId: RequestBody,
        name: RequestBody,
        price: Int,
        unit: RequestBody,
        stock: Int,
        description: RequestBody,
        imageMultipart: MultipartBody.Part?
    ) {
        productAddUpdateViewModel.updateProductBuah(
            buahId = buahId,
            name = name,
            harga = price,
            satuan = unit,
            stok = stock,
            deskripsi = description,
            file = imageMultipart
        ).observe(this) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoading.visibility = View.VISIBLE
                        binding.btnAddNewProduct.visibility = View.GONE
                    }

                    is ApiResult.Success -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnAddNewProduct.visibility = View.VISIBLE
                        Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                        moveActivity()
                    }

                    is ApiResult.Error -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnAddNewProduct.visibility = View.VISIBLE
                        Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, response.error)
                    }
                    else -> {
                        Toast.makeText(this, "Failed, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun addNewProduct(
        name: RequestBody,
        price: Int,
        unit: RequestBody,
        description: RequestBody,
        stock: Int,
        imageMultipart: MultipartBody.Part
    ) {
        productAddUpdateViewModel.addProductBuah(
            name = name,
            harga = price,
            satuan = unit,
            deskripsi = description,
            stok = stock,
            file = imageMultipart
        ).observe(this) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoading.visibility = View.VISIBLE
                        binding.btnAddNewProduct.visibility = View.GONE
                    }

                    is ApiResult.Success -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnAddNewProduct.visibility = View.VISIBLE
                        Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                        moveActivity()
                    }

                    is ApiResult.Error -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnAddNewProduct.visibility = View.VISIBLE
                        Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, response.error)
                    }
                    else -> {
                        Toast.makeText(this, "Failed, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun moveActivity() {
        finish()
    }

    private fun setupViewModel() {
        productAddUpdateViewModel = ViewModelProvider(
            this,
            MyStoreViewModelFactory.getInstance(this)
        )[ProductAddUpdateViewModel::class.java]
    }

    companion object {
        private const val TAG = "ProductAddUpdateActivity"
        const val EXTRA_DETAIL = "extra_detail"
    }
}