package com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detailfruit

import com.google.gson.annotations.SerializedName

data class DetailSpecificFruitResponse(

	@field:SerializedName("buah")
	val buah: Buah?,

	@field:SerializedName("toko")
	val toko: Toko,

	@field:SerializedName("message")
	val message: String,

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

	@field:SerializedName("deskripsi")
	val deskripsi: String?,

	@field:SerializedName("alamat")
	val alamat: Alamat
)

data class Buah(

	@field:SerializedName("creator")
	val creator: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("satuan")
	val satuan: String,

	@field:SerializedName("idBuah")
	val idBuah: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("stok")
	val stok: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("gambar")
	val gambar: String
)
