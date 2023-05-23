package com.althaaf.fruitarians.core.data.network.authentication.response.login

import com.google.gson.annotations.SerializedName

data class SentTokenResponse(

	@field:SerializedName("data")
	val data: DataSentToken,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)

data class User(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)

data class DataSentToken(

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("token")
	val token: String
)
