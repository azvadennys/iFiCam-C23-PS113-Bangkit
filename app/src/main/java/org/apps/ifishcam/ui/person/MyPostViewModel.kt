package org.apps.ifishcam.ui.person

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.apps.ifishcam.model.StoryReq
import org.apps.ifishcam.model.story.StoriesItem
import org.apps.ifishcam.model.story.StoryResponse
import org.apps.ifishcam.network.api.ApiConfig
import org.apps.ifishcam.network.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPostViewModel: ViewModel() {

    private val _listStory = MutableLiveData<List<StoriesItem>?>()
    val listStory: LiveData<List<StoriesItem>?> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    fun getStoriesUid(uid: String){
        _isLoading.value = true
        _isEmpty.value = false
        val client = ApiConfig.getApiService().getStoriesUid(uid)
        client.enqueue(object: Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>,
            ) {
                if (response.isSuccessful) {
                    val storiesUid = response.body()?.stories
                    if (storiesUid != null && storiesUid.isNotEmpty()) {
                        _listStory.value = storiesUid as List<StoriesItem>?
                        _isLoading.value = false
                        _isEmpty.value = false
                    } else {
                        _isEmpty.value = true
                        _isLoading.value = false
                    }
                } else {
                    _isEmpty.value = true
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
            }
        })
    }

    fun deleteStoriesUid(uid: String, storyReq: StoryReq){
        _isLoading.value = true
        val client = ApiConfig.getApiService().deleteStory(uid, storyReq)
        client.enqueue(object: Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>,
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    Log.d("Hapus", "Berhasil ${response.code()}")


                } else {
                    Log.d("Hapus", "Gagal ${response.code()}")
                }
            }
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Log.d("Hapus", "KONTOL ${t.message}")
            }
        })
    }

}