package com.althaaf.fruitarians.core.data.network.profile.logout

import com.google.gson.annotations.SerializedName

data class LogoutResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)
