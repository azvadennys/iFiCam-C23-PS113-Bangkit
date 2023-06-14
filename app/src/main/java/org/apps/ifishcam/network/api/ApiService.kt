package org.apps.ifishcam.network.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.apps.ifishcam.R
import org.apps.ifishcam.model.HistoryReq
import org.apps.ifishcam.model.StoryReq
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserReq
import org.apps.ifishcam.model.artikel.ArticleResponse
import org.apps.ifishcam.model.story.StoriesItem
import org.apps.ifishcam.model.story.StoryResponse
import org.apps.ifishcam.network.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/users/{uid}")
    fun postUid(
        @Path("uid") uid: String,
        @Body userReq: UserReq
    ): Call<UserResponse>

    @GET("api/articles")
    fun getArticles(): Call<ArticleResponse>

    @GET("api/stories")
    fun getStories(): Call<StoryResponse>

    @GET("api/stories/{uid}")
    fun getStoriesUid(
        @Path("uid") uid: String
    ): Call<StoryResponse>

    @HTTP(method = "DELETE", path = "api/stories/{uid}", hasBody = true)
    fun deleteStory(
        @Path("uid") uid: String,
        @Body storyReq: StoryReq
    ): Call<DeleteResponse>

    @HTTP(method = "DELETE", path = "api/histories/{uid}", hasBody = true)
    fun deleteHistory(
        @Path("uid") uid: String,
        @Body historyReq: HistoryReq
    ): Call<DeleteResponse>

    @GET("api/recipes")
    fun getRecipes(): Call<PredictResponse>

    @GET("api/histories/{uid}")
    fun getHistoriesByUid(
        @Path("uid") uid: String
    ): Call<HistoryResponse>

    @GET("api/histories/{uid}/{historyid}")
    fun getDetailHistories(
        @Path("uid") uid: String,
        @Path("historyid") historyid: String,
    ): Call<DetailHistoryResponse>

    @Multipart
    @POST("api/predicts/{uid}")
    fun predictImage(
        @Path("uid") uid: String,
        @Part image: MultipartBody.Part,
    ): Call<PredictResponse>

    @Multipart
    @POST("/api/stories/{uid}")
    fun uploadImageStory(
        @Path("uid") uid: String,
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("address") address: RequestBody,
        @Part("latitude") latitude: String,
        @Part("longitude") longitude: String,
    ): Call<UploadStoryResponse>

    @GET("/api/stories?location=1")
    fun getLocation(): Call<StoryResponse>
}