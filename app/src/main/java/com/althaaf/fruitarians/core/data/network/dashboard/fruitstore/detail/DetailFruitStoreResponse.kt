package com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detail

import com.google.gson.annotations.SerializedName

data class DetailFruitStoreResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("errors")
	val errors: Boolean
)

data class Alamat(

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("negara")
	val negara: String,

	@field:SerializedName("deskripsi_alamat")
	val deskripsiAlamat: String
)

data class BuahItem(

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("satuan")
	val satuan: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("stok")
	val stok: Int,

	@field:SerializedName("gambar")
	val gambar: String
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

data class Data(

	@field:SerializedName("bergabung")
	val bergabung: String,

	@field:SerializedName("buah")
	val buah: List<BuahItem>,

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

	@field:SerializedName("alamat")
	val alamat: Alamat
)
