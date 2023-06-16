package com.althaaf.fruitarians.ui.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.althaaf.fruitarians.core.data.network.profile.ProfileRepository
import com.althaaf.fruitarians.core.data.network.profile.editprofile.EditProfileResponse
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileViewModel(private val profileRepository: ProfileRepository): ViewModel() {

    fun editDataUser(
        name: RequestBody,
        negara: RequestBody,
        kota: RequestBody,
        deskripsi_alamat: RequestBody,
        telepon: RequestBody,
        deskripsi: RequestBody,
        jam_buka: RequestBody,
        jam_tutup: RequestBody,
        hari_buka_awal: RequestBody,
        hari_buka_akhir: RequestBody,
        file: MultipartBody.Part?
    ): LiveData<ApiResult<EditProfileResponse>> {
        return profileRepository.editProfileUser(
            name = name,
            negara = negara,
            kota = kota,
            deskripsi_alamat = deskripsi_alamat,
            telepon = telepon,
            deskripsi = deskripsi,
            jam_buka = jam_buka,
            jam_tutup = jam_tutup,
            hari_buka_awal = hari_buka_awal,
            hari_buka_akhir = hari_buka_akhir,
            file = file
        )
    }
}