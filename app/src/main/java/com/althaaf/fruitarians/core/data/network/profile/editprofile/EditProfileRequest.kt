package com.althaaf.fruitarians.core.data.network.profile.editprofile

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class EditProfileRequest(
    val name: RequestBody,
    val negara: RequestBody,
    val kota: RequestBody,
    val deskripsi_alamat: RequestBody,
    val telepon: RequestBody,
    val deskripsi: RequestBody,
    val jam_buka: RequestBody,
    val jam_tutup: RequestBody,
    val hari_buka_awal: RequestBody,
    val hari_buka_akhir: RequestBody,
    val file: MultipartBody.Part?
)
