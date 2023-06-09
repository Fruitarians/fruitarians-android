package com.althaaf.fruitarians.core.data.network.profile.myvendor

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SubsVendorResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)

@Parcelize
data class VendorSubsItem(

	@field:SerializedName("owner")
	val owner: String,

	@field:SerializedName("schedule")
	val schedule: String,

	@field:SerializedName("bergabung")
	val bergabung: String,

	@field:SerializedName("wa_link")
	val waLink: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("telepon")
	val telepon: String,

	@field:SerializedName("delivered")
	val delivered: Boolean,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("alamat")
	val alamat: String
): Parcelable

data class Data(

	@field:SerializedName("per_page")
	val perPage: Int,

	@field:SerializedName("vendor_subs")
	val vendorSubs: List<VendorSubsItem>,

	@field:SerializedName("total_data")
	val totalData: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
)
