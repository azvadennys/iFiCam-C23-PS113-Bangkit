package org.apps.ifishcam.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.apps.ifishcam.api.ApiConfig
import org.apps.ifishcam.response.ArticlesItem
import org.apps.ifishcam.response.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _listNews = MutableLiveData<List<ArticlesItem>?>()
    val listNews: MutableLiveData<List<ArticlesItem>?> = _listNews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    fun getNews(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getNews("d94640619385476aa70b61c86b4431e2")
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    _listNews.value = response.body()?.articles as List<ArticlesItem>
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
            }

        })
    }
}