package com.althaaf.fruitarians.core.data.local.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendationModel(
    val id: Int,
    val name: String,
    val search: String,
    val description: String,
    val image:Int
): Parcelable
