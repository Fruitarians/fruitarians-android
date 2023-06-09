package com.althaaf.fruitarians.ui.detailfruitstore

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.adapter.MarginItemDecoration
import com.althaaf.fruitarians.core.adapter.RecommendationAdapter
import com.althaaf.fruitarians.core.data.local.model.DataRecommendation
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem
import com.althaaf.fruitarians.databinding.FragmentDetailDescBinding
import com.bumptech.glide.Glide

class DetailDescFragment : Fragment() {

    private var _binding: FragmentDetailDescBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailDescBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDetailData()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val margin = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        val itemDecoration = MarginItemDecoration(margin)

        val adapter = RecommendationAdapter(DataRecommendation.dataFruitRecommendation)
        binding.rvRecommendation.adapter = adapter
        binding.rvRecommendation.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendation.addItemDecoration(itemDecoration)
        binding.rvRecommendation.setHasFixedSize(true)
    }

    private fun setupDetailData() {

        val dataDetail = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable(ARG_EXTRA_DATA, DataItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_EXTRA_DATA)
        }

        binding.namaTokoDetail.text = dataDetail?.name
        binding.alamatTokoDetail.text = "${dataDetail?.alamat?.deskripsiAlamat}, ${dataDetail?.alamat?.kota}, ${dataDetail?.alamat?.negara}"

        val deskripsiToko = dataDetail?.deskripsi
        if (deskripsiToko == null) {
            binding.deskripsiTokoDetail.text = "No Description"
        } else {
            binding.deskripsiTokoDetail.text = deskripsiToko
        }

        val jamTokoBuka = dataDetail?.jamOperasional?.jamBuka
        val jamTokoTutup = dataDetail?.jamOperasional?.jamTutup
        val hariTokoBuka = dataDetail?.jamOperasional?.hariBukaAwal
        val hariTokoTutup = dataDetail?.jamOperasional?.hariBukaAkhir

        if (jamTokoBuka == null && jamTokoTutup == null) {
            binding.jamOperasional.text = getString(R.string.open_jam, "Not set", "Not Set")
        } else {
            binding.jamOperasional.text = getString(R.string.open_jam, jamTokoBuka, jamTokoTutup)
        }

        if (hariTokoBuka == null && hariTokoTutup == null) {
            binding.hariOperasional.text = getString(R.string.open_jam, "Not set", "Not Set")
        } else {
            binding.hariOperasional.text = getString(R.string.open_jam, hariTokoBuka, hariTokoTutup)
        }

        binding.btnMoveWa.setOnClickListener {
            val order = "Selamat malam mas/mba, benarkah ini dari Toko buah '${dataDetail?.name}', bolehkah saya bertanya tanya terlebih dahulu?'"
            val url = "https://api.whatsapp.com/send?phone=+62${dataDetail?.telepon}&text=$order"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    companion object {
        const val ARG_EXTRA_DATA = "extra_data"
    }
}