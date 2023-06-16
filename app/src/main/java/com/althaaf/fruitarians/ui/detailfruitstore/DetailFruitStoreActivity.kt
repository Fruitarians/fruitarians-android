package com.althaaf.fruitarians.ui.detailfruitstore

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityDetailFruitStoreBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class DetailFruitStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFruitStoreBinding
    private lateinit var detailFruitStoreViewModel: DetailFruitStoreViewModel
    private lateinit var idToko: String
    private lateinit var namaToko: String
    private var isMembership = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFruitStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupDetailData()
        setupMembership()
        setupAction()
    }

    private fun setupMembership() {
        detailFruitStoreViewModel.getUserisMember(idToko).observe(this) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {}

                    is ApiResult.Success -> {
                       if (response.data.data.bookmarked) {
                           isMembership = true
                           binding.btnAddMembership.text = getString(R.string.membership)
                           binding.btnAddMembership.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                           val newPaddingEnd = resources.getDimensionPixelSize(R.dimen.new_padding_end2)
                           binding.btnAddMembership.setPaddingRelative(binding.btnAddMembership.paddingStart, binding.btnAddMembership.paddingTop, newPaddingEnd, binding.btnAddMembership.paddingBottom)
                       } else {
                           isMembership = false
                           val iconFollow = ContextCompat.getDrawable(this, R.drawable.ic_plus)
                           binding.btnAddMembership.setCompoundDrawablesWithIntrinsicBounds(null, null, iconFollow, null)
                           val newPaddingEnd = resources.getDimensionPixelSize(R.dimen.new_padding_end)
                           binding.btnAddMembership.setPaddingRelative(binding.btnAddMembership.paddingStart, binding.btnAddMembership.paddingTop, newPaddingEnd, binding.btnAddMembership.paddingBottom)
                           binding.btnAddMembership.text = getString(R.string.follow)
                       }
                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Error: ${response.error}")
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        detailFruitStoreViewModel = ViewModelProvider(this, HomeViewModelFactory.getInstance(this))[DetailFruitStoreViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        binding.btnAddMembership.setOnClickListener {
            if (isMembership) {
                showUnmembershipDialog()
            } else {
                showMembershipDialog()
            }
        }
    }

    private fun showMembershipDialog() {
        val message = "Are you sure you want to join the membership of this shop ?"
        val title = "Join $namaToko Membership"

        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            setPositiveButton("Yes") { _, _ ->
                addStateButton()
                addMembership()
            }
            setNegativeButton("No") { dialog, _ -> dialog.cancel()}
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun addStateButton() {
        binding.btnAddMembership.text = getString(R.string.membership)
        binding.btnAddMembership.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        val newPaddingEnd = resources.getDimensionPixelSize(R.dimen.new_padding_end2)
        binding.btnAddMembership.setPaddingRelative(binding.btnAddMembership.paddingStart, binding.btnAddMembership.paddingTop, newPaddingEnd, binding.btnAddMembership.paddingBottom)
    }

    private fun addMembership() {
        detailFruitStoreViewModel.addUserMembership(idToko).observe(this) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {}

                    is ApiResult.Success -> {
                        isMembership = true
                        Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Error: ${response.error}")
                    }

                }
            }
        }
    }

    private fun showUnmembershipDialog() {
        val message = "Are you sure you want to quit the membership of this shop ?"
        val title = "Quit $namaToko Membership"

        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            setPositiveButton("Yes") { _, _ ->
                stateButtonCancel()
                cancelMembership()
            }
            setNegativeButton("No") { dialog, _ -> dialog.cancel()}
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun stateButtonCancel() {
        val iconFollow = ContextCompat.getDrawable(this, R.drawable.ic_plus)
        binding.btnAddMembership.setCompoundDrawablesWithIntrinsicBounds(null, null, iconFollow, null)
        val newPaddingEnd = resources.getDimensionPixelSize(R.dimen.new_padding_end)
        binding.btnAddMembership.setPaddingRelative(binding.btnAddMembership.paddingStart, binding.btnAddMembership.paddingTop, newPaddingEnd, binding.btnAddMembership.paddingBottom)
        binding.btnAddMembership.text = getString(R.string.follow)
    }

    private fun cancelMembership() {
        detailFruitStoreViewModel.deleteUserMembership(idToko).observe(this) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {}

                    is ApiResult.Success -> {
                        isMembership = false
                        Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Error: ${response.error}")
                    }
                }
            }
        }
    }


    private fun setupDetailData() {
        val dataDetailToko = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, DataItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        val profileToko = dataDetailToko?.gambarProfil
        if (profileToko == null) {
            binding.profileDetail.setImageDrawable(ContextCompat.getDrawable(binding.profileDetail.context, R.drawable.default_img_toko))
        } else {
            Glide.with(this)
                .load(dataDetailToko.gambarProfil)
                .into(binding.profileDetail)
        }

        binding.detailNamaToko.text = dataDetailToko?.name
        binding.detailCreatedToko.text = getString(R.string.created, dataDetailToko?.bergabung)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)

        if(dataDetailToko != null) {
            idToko = dataDetailToko.id
            namaToko = dataDetailToko.name
            sectionsPagerAdapter.dataDetail = DataItem(
                bergabung = dataDetailToko.bergabung,
                jamOperasional = dataDetailToko.jamOperasional,
                waLink = dataDetailToko.waLink,
                name = dataDetailToko.name,
                telepon = dataDetailToko.telepon,
                gambarProfil = dataDetailToko.gambarProfil,
                id = dataDetailToko.id,
                deskripsi = dataDetailToko.deskripsi,
                email = dataDetailToko.email,
                alamat = dataDetailToko.alamat
            )

            sectionsPagerAdapter.dataId = dataDetailToko.id
        }


        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        const val EXTRA_DATA = "extra_data"
        private const val TAG = "DetailFruitStoreActivity"
    }
}