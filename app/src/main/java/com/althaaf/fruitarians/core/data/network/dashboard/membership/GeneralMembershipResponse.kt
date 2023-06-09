package com.althaaf.fruitarians.core.data.network.dashboard.membership

import com.google.gson.annotations.SerializedName

data class GeneralMembershipResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)
