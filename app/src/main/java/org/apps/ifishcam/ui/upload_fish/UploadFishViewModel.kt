package org.apps.ifishcam.ui.upload_fish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.apps.ifishcam.network.api.ApiConfig
import org.apps.ifishcam.network.response.UploadStoryResponse
import org.apps.ifishcam.utils.reduceFileImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadFishViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(uid: String, imageFile: File, name: String, desc: String, address: String, lat: String, long: String) {
        val file = reduceFileImage(imageFile as File)

        val name = name.toRequestBody("text/plain".toMediaType())
        val description = desc.toRequestBody("text/plain".toMediaType())
        val address = address.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().uploadImageStory(
                uid,
                imageMultipart,
                name,
                description,
                address,
                lat,
                long
            )

        _isLoading.value = true
        client?.enqueue(object : Callback<UploadStoryResponse> {
            override fun onResponse(
                call: Call<UploadStoryResponse>,
                response: Response<UploadStoryResponse>,
            ) {
                if (response.isSuccessful){
                    _isLoading.value = true
                    Log.d("WOI", "Response successful")

                } else {
                    Log.d("WOI", "Response unsuccessful: Error ${response.code()}")

                }
            }

            override fun onFailure(call: Call<UploadStoryResponse>, t: Throwable) {
                Log.d("WOI", "Response unsuccessful: Error ${t.message}")
            }

        })
    }

}