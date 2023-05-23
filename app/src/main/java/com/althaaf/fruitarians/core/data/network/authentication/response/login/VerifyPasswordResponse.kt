package com.althaaf.fruitarians.core.data.network.authentication.response.login

import com.google.gson.annotations.SerializedName

data class VerifyPasswordResponse(

	@field:SerializedName("data")
	val data: DataVerifyPassword,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)


data class DataVerifyPassword(

	@field:SerializedName("user")
	val user: User
)
