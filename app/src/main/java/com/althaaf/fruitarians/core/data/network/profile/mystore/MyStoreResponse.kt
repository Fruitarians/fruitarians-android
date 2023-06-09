package com.althaaf.fruitarians.core.data.network.profile.mystore

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MyStoreResponse(

	@field:SerializedName("totalBuah")
	val totalBuah: Int,

	@field:SerializedName("buah")
	val buah: List<BuahItem>?,

	@field:SerializedName("toko")
	val toko: Toko,

	@field:SerializedName("errors")
	val errors: Boolean
)

data class JamOperasional(

	@field:SerializedName("jam_buka")
	val jamBuka: String?,

	@field:SerializedName("jam_tutup")
	val jamTutup: String?,

	@field:SerializedName("hari_buka_akhir")
	val hariBukaAkhir: String?,

	@field:SerializedName("hari_buka_awal")
	val hariBukaAwal: String?
)

@Parcelize
data class BuahItem(

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("satuan")
	val satuan: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("stok")
	val stok: Int,

	@field:SerializedName("gambar")
	val gambar: String
): Parcelable

data class Alamat(

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("negara")
	val negara: String,

	@field:SerializedName("deskripsi_alamat")
	val deskripsiAlamat: String
)

data class Toko(

	@field:SerializedName("bergabung")
	val bergabung: String,

	@field:SerializedName("jam_operasional")
	val jamOperasional: JamOperasional?,

	@field:SerializedName("wa_link")
	val waLink: String,

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
)
