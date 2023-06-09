package com.althaaf.fruitarians.core.data.network.profile.updateproduct

import com.google.gson.annotations.SerializedName

data class UpdateProductResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("new_buah_data")
	val newBuahData: NewBuahData,

	@field:SerializedName("errors")
	val errors: Boolean,

	@field:SerializedName("picture")
	val picture: Picture
)

data class NewBuahData(

	@field:SerializedName("creator")
	val creator: Creator,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("satuan")
	val satuan: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("stok")
	val stok: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("gambar")
	val gambar: String
)

data class Creator(

	@field:SerializedName("toko")
	val toko: String,

	@field:SerializedName("userId")
	val userId: String
)

data class Picture(

	@field:SerializedName("new_picture")
	val newPicture: Boolean?,

	@field:SerializedName("success_upload")
	val successUpload: Boolean?
)
