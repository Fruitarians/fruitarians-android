package com.althaaf.fruitarians.core.data.network.authentication.request.register

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterRequest(
    val email: String,
    val password: String,
    val password_konfir: String,
    val name: String,
    val role: String,
    val telepon: String,
    val negara: String,
    val kota: String,
    val deskripsi_alamat: String
) : Parcelable
