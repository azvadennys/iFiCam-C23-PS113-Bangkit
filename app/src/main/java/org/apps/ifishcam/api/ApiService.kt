package org.apps.ifishcam.api

import org.apps.ifishcam.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines?country=us&category=business")
    fun getNews(@Query("apiKey") apiKey: String): Call<NewsResponse>
}