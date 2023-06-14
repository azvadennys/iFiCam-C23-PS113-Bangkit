package org.apps.ifishcam.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.apps.ifishcam.model.HistoryReq
import org.apps.ifishcam.network.api.ApiConfig
import org.apps.ifishcam.network.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {

    private val _listHistory = MutableLiveData<List<HistoriesItem>?>()
    val listHistory: LiveData<List<HistoriesItem>?> = _listHistory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    fun getHistories(uid: String){
        _isLoading.value = true
        _isEmpty.value = false
        val client = ApiConfig.getApiService().getHistoriesByUid(uid)
        client.enqueue(object: Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>,
            ) {
                if (response.isSuccessful) {
                    val histories = response.body()?.histories
                    if (histories != null && histories.isNotEmpty()) {
                        _listHistory.value = histories as List<HistoriesItem>?
                        _isLoading.value = false
                        _isEmpty.value = false
                        Log.d("Riwayat", response.code().toString())
                    } else {
                        _isEmpty.value = true
                    }
                } else {
                    _isEmpty.value = true
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
            }
        })
    }

    fun deleteHistoryUid(uid: String, historyReq: HistoryReq){
        _isLoading.value = true
        val client = ApiConfig.getApiService().deleteHistory(uid, historyReq)
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
            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {}
        })
    }

    private val _detailHistory = MutableLiveData<DetailedHistory>()
    val detailHistory: LiveData<DetailedHistory> = _detailHistory

    fun getDetailHistory(uid: String, hid: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailHistories(uid,hid)
        client.enqueue(object: Callback<DetailHistoryResponse>{
            override fun onResponse(
                call: Call<DetailHistoryResponse>,
                response: Response<DetailHistoryResponse>,
            ) {
                if (response.isSuccessful){
                    _detailHistory.value = response.body()?.detailedHistory as DetailedHistory
                    _isLoading.value = false
                    Log.d("DETAIL", "Berhasil ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DetailHistoryResponse>, t: Throwable) {}
        })
    }
}