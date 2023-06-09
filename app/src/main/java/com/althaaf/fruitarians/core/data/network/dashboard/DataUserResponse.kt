package com.althaaf.fruitarians.core.data.network.dashboard

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DataUserResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)

@Parcelize
data class JamOperasional(

	@field:SerializedName("jam_buka")
	val jamBuka: String?,

	@field:SerializedName("jam_tutup")
	val jamTutup: String?,

	@field:SerializedName("hari_buka_akhir")
	val hariBukaAkhir: String?,

	@field:SerializedName("hari_buka_awal")
	val hariBukaAwal: String?
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("bergabung")
	val bergabung: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("jam_operasional")
	val jamOperasional: JamOperasional?,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("telepon")
	val telepon: String,

	@field:SerializedName("gambar_profil")
	val gambarProfil: String?,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String?,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("alamat")
	val alamat: Alamat
): Parcelable

@Parcelize
data class Alamat(

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("negara")
	val negara: String,

	@field:SerializedName("deskripsi_alamat")
	val deskripsiAlamat: String
): Parcelable
