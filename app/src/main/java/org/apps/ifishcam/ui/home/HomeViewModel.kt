package org.apps.ifishcam.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.apps.ifishcam.model.UserReq
import org.apps.ifishcam.model.artikel.ArticleResponse
import org.apps.ifishcam.model.artikel.ArticlesItem
import org.apps.ifishcam.network.api.ApiConfig
import org.apps.ifishcam.network.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _listNews = MutableLiveData<List<ArticlesItem>?>()
    val listNews: LiveData<List<ArticlesItem>?> = _listNews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    fun getArticle(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getArticles()
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                if (response.isSuccessful) {
                    Log.d("API_RESPONSE", "Data ArticlesItem: ${response.body()}")
                    _listNews.value = response.body()?.articles as List<ArticlesItem>
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun postId(uid: String, user: UserReq) {
        val client = ApiConfig.getApiService().postUid(uid, user)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    Log.d("Hello", "Response successful")
                } else {
                    Log.d("Hello", "Response unsuccessful: Error ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Hello", "Request failed: ${t.message}")
            }
        })
    }

}