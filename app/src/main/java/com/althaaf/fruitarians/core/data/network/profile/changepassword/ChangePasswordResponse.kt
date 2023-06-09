package com.althaaf.fruitarians.core.data.network.profile.changepassword

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)
