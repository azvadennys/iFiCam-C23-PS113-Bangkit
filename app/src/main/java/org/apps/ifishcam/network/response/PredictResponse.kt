package org.apps.ifishcam.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictResponse(
	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("recipes")
	val recipes: List<RecipesItem?>? = null,

	@field:SerializedName("fishName")
	val fishName: String? = null,

	@field:SerializedName("nutrition")
	val nutrition: List<String?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("predictionAccuracy")
	val predictionAccuracy: String? = null
) : Parcelable


@Parcelize
data class RecipesItem(
	@field:SerializedName("difficulty")
	val difficulty: String? = null,

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("linkUrl")
	val linkUrl: String? = null,

	@field:SerializedName("title")
	val title: String? = null
): Parcelable
