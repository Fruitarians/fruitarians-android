package com.althaaf.fruitarians.core.data.network.profile.myvendor.addsubs

import com.google.gson.annotations.SerializedName

data class AddSubsResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)
