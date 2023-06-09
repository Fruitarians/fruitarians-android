package com.althaaf.fruitarians.core.data.network.profile.myvendor.addsubs

data class AddSubsRequest(
    val name: String,
    val owner: String,
    val telepon: String,
    val alamat: String,
    val category: String,
    val schedule: String,
    val deskripsi: String
)
