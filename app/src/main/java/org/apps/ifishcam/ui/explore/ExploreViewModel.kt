package org.apps.ifishcam.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.apps.ifishcam.model.story.StoriesItem
import org.apps.ifishcam.model.story.StoryResponse
import org.apps.ifishcam.network.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreViewModel: ViewModel() {

    private val _listStories = MutableLiveData<List<StoriesItem>?>()
    val listStories: LiveData<List<StoriesItem>?> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _emptyData = MutableLiveData<Boolean>()
    val emptyData: MutableLiveData<Boolean> = _emptyData

    fun getStory(){
        _isLoading.value = true
        _emptyData.value = false
        val client = ApiConfig.getApiService().getStories()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful){
                    _listStories.value = response.body()?.stories as List<StoriesItem>
                    _isLoading.value = false
                    _emptyData.value = false
                } else {
                    _emptyData.value = true
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
            }

        })
    }


}