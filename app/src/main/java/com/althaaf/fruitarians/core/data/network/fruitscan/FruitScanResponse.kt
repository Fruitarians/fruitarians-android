package com.althaaf.fruitarians.core.data.network.fruitscan

import com.google.gson.annotations.SerializedName

data class FruitScanResponse(

	@field:SerializedName("model-prediction-confidence-score")
	val modelPredictionConfidenceScore: Any,

	@field:SerializedName("model-prediction")
	val modelPrediction: String
)
