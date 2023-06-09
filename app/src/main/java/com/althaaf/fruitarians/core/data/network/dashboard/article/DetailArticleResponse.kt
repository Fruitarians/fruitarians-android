package com.althaaf.fruitarians.core.data.network.dashboard.article

import com.google.gson.annotations.SerializedName

data class DetailArticleResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("randomItem")
	val randomItem: RandomItem,

	@field:SerializedName("message")
	val message: String
)

data class RandomItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("konten")
	val konten: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String
)

data class Data(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("konten")
	val konten: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String
)
