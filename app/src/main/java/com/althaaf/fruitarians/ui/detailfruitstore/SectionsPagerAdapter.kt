package com.althaaf.fruitarians.ui.detailfruitstore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.Alamat
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.JamOperasional

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    var dataDetail: DataItem = DataItem(
        bergabung = "",
        jamOperasional = JamOperasional(
            jamBuka = "",
            jamTutup = "",
            hariBukaAkhir = "",
            hariBukaAwal = ""
        ),
        waLink = "",
        name = "",
        telepon = "",
        gambarProfil = "",
        id = "",
        deskripsi = "",
        email = "",
        alamat = Alamat(
            kota = "",
            negara = "",
            deskripsiAlamat = ""
        )
    )

    var dataId: String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> {
                fragment = DetailDescFragment()
                fragment.arguments = Bundle().apply {
                    putParcelable(DetailDescFragment.ARG_EXTRA_DATA, dataDetail)
                }
            }

            1 -> {
                fragment = DetailProductFragment()
                fragment.arguments = Bundle().apply {
                    putString(DetailProductFragment.ARG_DATA_ID, dataId)
                }
            }
        }

        return fragment as Fragment
    }
}