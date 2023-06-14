package org.apps.ifishcam.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DetailHistoryResponse(
	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("detailedHistory")
	val detailedHistory: DetailedHistory? = null
)

@Parcelize
data class DetailedHistory(
	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("recipes")
	val recipes: List<RecipesItem?>? = null,

	@field:SerializedName("fishName")
	val fishName: String? = null,

	@field:SerializedName("nutrition")
	val nutrition: List<String?>? = null,

	@field:SerializedName("historyId")
	val historyId: String? = null,

	@field:SerializedName("fishId")
	val fishId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("predictionAccuracy")
	val predictionAccuracy: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("predictionValue")
	val predictionValue: String? = null
): Parcelable
