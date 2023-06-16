package com.althaaf.fruitarians.ui.myvendor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.profile.myvendor.addsubs.AddSubsRequest
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.MyVendorViewModelFactory
import com.althaaf.fruitarians.databinding.ActivitySubsAddUpdateBinding

class SubsAddUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubsAddUpdateBinding
    private lateinit var myVendorViewModel: MyVendorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubsAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
        setupData()
    }

    private fun setupData() {
        val category = listOf("High Priority", "Priority", "Low Priority")
        val adapterState = ArrayAdapter(this, R.layout.list_select, category)
        (binding.autoCompleteTextCategory as? AutoCompleteTextView)?.setAdapter(adapterState)
    }

    private fun setupAction() {
        binding.btnAddNewPartner.setOnClickListener {
            val fruitStoreName = binding.inputPartnerName.text.toString().trim()
            val owner = binding.inputPartnerOwner.text.toString().trim()
            val address = binding.inputPartnerAddress.text.toString().trim()
            val telepon = binding.inputPartnerTelepon.text.toString().trim()
            val category = binding.autoCompleteTextCategory.text.toString().trim()
            val schedule = binding.inputPartnerSchedule.text.toString().trim()
            val detail = binding.inputPartnerDetail.text.toString().trim()

            if (formIsValidated()) {
                val addSubsRequest = AddSubsRequest(
                    name = fruitStoreName,
                    owner = owner,
                    telepon = telepon,
                    alamat = address,
                    category = category,
                    schedule = schedule,
                    deskripsi = detail
                )

                myVendorViewModel.addSubVendor(addSubsRequest).observe(this) {
                    it?.let { response ->
                        when (response) {
                            is ApiResult.Loading -> {
                                binding.lottieLoading.visibility = View.VISIBLE
                                binding.btnAddNewPartner.visibility = View.GONE
                            }

                            is ApiResult.Success -> {
                                binding.lottieLoading.visibility = View.GONE
                                binding.btnAddNewPartner.visibility = View.VISIBLE
                                Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(
                                    Intent(
                                        this@SubsAddUpdateActivity,
                                        MyVendorActivity::class.java
                                    )
                                )
                                finish()
                            }

                            is ApiResult.Error -> {
                                binding.lottieLoading.visibility = View.GONE
                                binding.btnAddNewPartner.visibility = View.VISIBLE
                                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                                Log.e(TAG, response.error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun formIsValidated(): Boolean {
        val fruitStoreName = binding.inputPartnerName.text.toString().trim()
        val owner = binding.inputPartnerOwner.text.toString().trim()
        val address = binding.inputPartnerAddress.text.toString().trim()
        val telepon = binding.inputPartnerTelepon.text.toString().trim()
        val category = binding.autoCompleteTextCategory.text.toString().trim()
        val schedule = binding.inputPartnerSchedule.text.toString().trim()
        val detail = binding.inputPartnerDetail.text.toString().trim()

        if (fruitStoreName.isEmpty() || owner.isEmpty() || address.isEmpty() || telepon.isEmpty() || category.isEmpty() || schedule.isEmpty() || detail.isEmpty()) {
            binding.apply {
                edPartnerName.error = "Please fill out this field"
                edPartnerOwner.error = "Please fill out this field"
                edPartnerAddress.error = "Please fill out this field"
                edPartnerTelepon.error = "Please fill out this field"
                edPartnerCategory.error = "Please fill out this field"
                edPartnerSchedule.error = "Please fill out this field"
                edPartnerDetail.error = "Please fill out this field"
            }
            return false
        }

        return true
    }

    private fun setupViewModel() {
        myVendorViewModel = ViewModelProvider(
            this,
            MyVendorViewModelFactory.getInstance(this)
        )[MyVendorViewModel::class.java]
    }

    companion object {
        private const val TAG = "SubsAddUpdateActivity"
    }

}