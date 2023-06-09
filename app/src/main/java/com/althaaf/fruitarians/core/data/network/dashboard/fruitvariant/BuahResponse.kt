package com.althaaf.fruitarians.core.data.network.dashboard.fruitvariant

import com.google.gson.annotations.SerializedName

data class BuahResponse(

	@field:SerializedName("totalData")
	val totalData: Int,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)

data class DataItem(

	@field:SerializedName("creator")
	val creator: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("idBuah")
	val idBuah: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("stok")
	val stok: Int,

	@field:SerializedName("gambar")
	val gambar: String
)
