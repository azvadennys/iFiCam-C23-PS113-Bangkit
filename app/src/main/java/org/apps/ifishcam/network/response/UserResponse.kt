package org.apps.ifishcam.network.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
	@field:SerializedName("storyId")
	val storyId: List<Any?>? = null,

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("profile_photo")
	val profilePhoto: String? = null,

	@field:SerializedName("historyId")
	val historyId: List<Any?>? = null,

	@field:SerializedName("signedIn")
	val signedIn: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)


