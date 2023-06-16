package com.althaaf.fruitarians.core.data.network.cart

import com.google.gson.annotations.SerializedName

data class CartResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)

data class Toko(

	@field:SerializedName("jam_operasional")
	val jamOperasional: JamOperasional,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("telepon")
	val telepon: String,

	@field:SerializedName("gambar_profil")
	val gambarProfil: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("alamat")
	val alamat: Alamat
)

data class Alamat(

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("negara")
	val negara: String,

	@field:SerializedName("deskripsi_alamat")
	val deskripsiAlamat: String
)

data class JamOperasional(

	@field:SerializedName("jam_buka")
	val jamBuka: String,

	@field:SerializedName("jam_tutup")
	val jamTutup: String,

	@field:SerializedName("hari_buka_akhir")
	val hariBukaAkhir: String,

	@field:SerializedName("hari_buka_awal")
	val hariBukaAwal: String
)

data class CartsItem(

	@field:SerializedName("id_cart")
	val idCart: String,

	@field:SerializedName("toko")
	val toko: Toko,

	@field:SerializedName("cart")
	val cart: List<CartItem>
)

data class CartItem(

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("id_buah")
	val idBuah: String,

	@field:SerializedName("satuan")
	val satuan: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("gambar")
	val gambar: String
)

data class Data(

	@field:SerializedName("carts")
	val carts: List<CartsItem>,

	@field:SerializedName("total_data")
	val totalData: Int
)
