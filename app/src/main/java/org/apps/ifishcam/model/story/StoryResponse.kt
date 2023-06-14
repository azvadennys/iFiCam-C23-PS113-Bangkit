package org.apps.ifishcam.model.story

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
	@field:SerializedName("stories")
	val stories: List<StoriesItem?>? = null
)

@Parcelize
data class StoriesItem(

	@field:SerializedName("storyId")
	val storyId: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("userPhoto")
	val userPhoto: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("userName")
	val userName: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
) : Parcelable
