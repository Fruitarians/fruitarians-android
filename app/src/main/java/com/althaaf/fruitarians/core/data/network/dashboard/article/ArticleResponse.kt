package com.althaaf.fruitarians.core.data.network.dashboard.article

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("result")
	val result: List<ResultItem>,

	@field:SerializedName("totalData")
	val totalData: Int,

	@field:SerializedName("message")
	val message: String
)

data class ResultItem(

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
