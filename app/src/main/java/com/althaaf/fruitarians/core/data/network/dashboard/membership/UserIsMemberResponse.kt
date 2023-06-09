package com.althaaf.fruitarians.core.data.network.dashboard.membership

import com.google.gson.annotations.SerializedName

data class UserIsMemberResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)

data class Data(

	@field:SerializedName("bookmarked_userId")
	val bookmarkedUserId: String,

	@field:SerializedName("bookmarked")
	val bookmarked: Boolean
)
