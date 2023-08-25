package org.apps.ifishcam.ui.detect_fish


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.apps.ifishcam.network.api.ApiConfig
import org.apps.ifishcam.network.response.PredictResponse
import org.apps.ifishcam.network.response.RecipesItem
import org.apps.ifishcam.utils.reduceFileImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetectFishViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _predictResponse = MutableLiveData<PredictResponse>()
    val predictResponse: LiveData<PredictResponse> = _predictResponse

    fun predictImage(uid: String, imageFile: File) {
        val file = reduceFileImage(imageFile as File)

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().predictImage(uid, imageMultipart)
        _isLoading.value = true
        _isError.value = false
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>,
            ) {
                if(response.isSuccessful){
                    Log.d("IKAN", "Berhasil ${response.code()}")
                    Thread.sleep(3000)
                    _predictResponse.value = response.body()
                    _isLoading.value = false
                    _isError.value = false
                } else {
                    Log.d("IKAN", "GAGAL ${response.code()}")
                    _isLoading.value = false
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                Log.d("IKAN", "GAGAL ${t.message}")
                _isError.value = true
            }

        })

    }

    private val _listRecipes = MutableLiveData<List<RecipesItem>?>()
    val listRecipes: LiveData<List<RecipesItem>?> = _listRecipes

    fun getRecipes(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRecipes()

        client.enqueue(object : Callback<PredictResponse>{
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>,
            ) {
                if (response.isSuccessful){
                    _listRecipes.value = response.body()?.recipes as List<RecipesItem>
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
            }

        })
    }
}