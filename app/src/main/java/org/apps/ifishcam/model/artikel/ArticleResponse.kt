package org.apps.ifishcam.model.artikel

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("articles")
	val articles: List<ArticlesItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ArticlesItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("linkUrl")
	val linkUrl: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
