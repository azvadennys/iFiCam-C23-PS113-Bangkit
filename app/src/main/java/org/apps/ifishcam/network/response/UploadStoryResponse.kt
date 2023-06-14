package org.apps.ifishcam.network.response

import com.google.gson.annotations.SerializedName

data class UploadStoryResponse(

	@field:SerializedName("storyId")
	val storyId: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)
