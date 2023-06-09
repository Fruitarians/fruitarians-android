package com.althaaf.fruitarians.core.data.network.profile.deleteproduct

import com.google.gson.annotations.SerializedName

data class DeleteProductResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean,

	@field:SerializedName("picture")
	val picture: Picture
)

data class Picture(

	@field:SerializedName("has_picture")
	val hasPicture: Boolean,

	@field:SerializedName("success_delete_picture")
	val successDeletePicture: Boolean
)

data class Data(

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("satuan")
	val satuan: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("gambar")
	val gambar: String
)
