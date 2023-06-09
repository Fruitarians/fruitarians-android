package com.althaaf.fruitarians.core.data.network.profile.addproduct

import com.google.gson.annotations.SerializedName

data class AddProductResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean,

	@field:SerializedName("picture")
	val picture: Picture
)

data class Data(

	@field:SerializedName("creator")
	val creator: Creator,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("satuan")
	val satuan: String,

	@field:SerializedName("stok")
	val stok: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("gambar")
	val gambar: String
)

data class Picture(

	@field:SerializedName("with_picture")
	val withPicture: Boolean,

	@field:SerializedName("success_upload")
	val successUpload: Boolean
)

data class Creator(

	@field:SerializedName("toko")
	val toko: String,

	@field:SerializedName("id")
	val id: String
)
