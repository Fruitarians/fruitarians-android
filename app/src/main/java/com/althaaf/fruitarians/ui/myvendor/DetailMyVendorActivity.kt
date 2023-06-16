package com.althaaf.fruitarians.ui.myvendor

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.profile.myvendor.VendorSubsItem
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.MyVendorViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityDetailMyVendorBinding

class DetailMyVendorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMyVendorBinding
    private lateinit var myVendorViewModel: MyVendorViewModel
    private lateinit var idPartner: String
    private var statusDelivered = false
    private var backgroundStatusTrue: Drawable? = null
    private var backgroundStatusFalse: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMyVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backgroundStatusTrue = ContextCompat.getDrawable(this, R.drawable.bg_status_complete)
        backgroundStatusFalse = ContextCompat.getDrawable(this, R.drawable.bg_status_incomplete)

        setupViewModel()
        setupData()
        setupAction()
    }

    private fun setupAction() {
        binding.btnChangeStatus.setOnClickListener {
            myVendorViewModel.changeStatusSub(idPartner).observe(this) {
                it?.let { response ->
                    when (response) {
                        is ApiResult.Loading -> {
                            binding.lottieLoading.visibility =  View.VISIBLE
                            binding.btnChangeStatus.visibility = View.GONE
                        }

                        is ApiResult.Success -> {
                            Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                            binding.lottieLoading.visibility =  View.GONE
                            binding.btnChangeStatus.visibility = View.VISIBLE
                            binding.btnChangeStatus.text = "Completed"
                            binding.btnChangeStatus.background = ContextCompat.getDrawable(this, R.drawable.bg_button_logout)
                            binding.btnChangeStatus.isEnabled = false
                            binding.detailPartnerStatus.text = "Delivered"
                            binding.detailPartnerStatus.background = backgroundStatusTrue
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

        binding.btnDelete.setOnClickListener {
            showAlertDialog()
        }

        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
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
        myVendorViewModel.deleteSubVendor(idPartner).observe(this){
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {}

                    is ApiResult.Success -> {
                        Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                        finish()
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

    private fun setupData() {
        val detailPartner = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DETAIL, VendorSubsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DETAIL)
        }
        detailPartner?.let {
            binding.apply {
                detailPartnerName.text = detailPartner.name
                detailPartnerJoined.text = detailPartner.bergabung
                detailPartnerOwner.text = detailPartner.owner
                detailPartnerIdToko.text = detailPartner.id
                idPartner = detailPartner.id
                detailPartnerTelepon.text = detailPartner.telepon
                detailPartnerCategory.text = detailPartner.category
                detailPartnerDetails.text = detailPartner.deskripsi
                detailPartnerScheduled.text = detailPartner.schedule
                statusDelivered = detailPartner.delivered
            }

            if (statusDelivered){
                binding.detailPartnerStatus.text = "Delivered"
                binding.detailPartnerStatus.background = backgroundStatusTrue
                binding.btnChangeStatus.text = "Completed"
                binding.btnChangeStatus.isEnabled = false
                binding.btnChangeStatus.background = ContextCompat.getDrawable(this, R.drawable.bg_button_logout)
            } else {
                binding.detailPartnerStatus.text = "On progress"
                binding.detailPartnerStatus.background = backgroundStatusFalse
            }
        }
    }

    private fun setupViewModel() {
        myVendorViewModel = ViewModelProvider(this, MyVendorViewModelFactory.getInstance(this))[MyVendorViewModel::class.java]
    }

    companion object {
        private const val TAG = "DetailMyVendorActivity"
        const val EXTRA_DETAIL = "extra_detail"
    }
}