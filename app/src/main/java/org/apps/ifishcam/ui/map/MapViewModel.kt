package org.apps.ifishcam.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.apps.ifishcam.model.story.StoriesItem
import org.apps.ifishcam.model.story.StoryResponse
import org.apps.ifishcam.network.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel : ViewModel() {

    private val _mapsStories = MutableLiveData<List<StoriesItem>>()
    val mapStories: LiveData<List<StoriesItem>> = _mapsStories

    fun getMapsStories(){

        val client = ApiConfig.getApiService().getLocation()
        client?.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    _mapsStories.value = response.body()?.stories as List<StoriesItem>
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
            }
        })
    }

}