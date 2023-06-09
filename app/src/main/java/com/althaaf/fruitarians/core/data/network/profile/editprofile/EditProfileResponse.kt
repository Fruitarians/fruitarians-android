package com.althaaf.fruitarians.core.data.network.profile.editprofile

import com.althaaf.fruitarians.core.data.network.dashboard.Data
import com.google.gson.annotations.SerializedName

data class EditProfileResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)