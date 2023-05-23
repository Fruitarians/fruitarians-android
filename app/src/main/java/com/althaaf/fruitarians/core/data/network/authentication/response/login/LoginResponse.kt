package com.althaaf.fruitarians.core.data.network.authentication.response.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLogin?,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)

data class DataLogin(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("token_type")
	val tokenType: String,

	@field:SerializedName("email")
	val email: String
)
