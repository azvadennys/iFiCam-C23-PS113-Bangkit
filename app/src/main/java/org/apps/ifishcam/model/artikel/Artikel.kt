package org.apps.ifishcam.model.artikel

data class Artikel(
    val title: String,
    val publishedAt: String,
    val urlToImage: String? = null,
    val url: String? = null,
)
