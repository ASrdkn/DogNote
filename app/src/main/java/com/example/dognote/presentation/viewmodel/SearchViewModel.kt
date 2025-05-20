package com.example.dognote.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dognote.data.models.DogBreed
import com.example.dognote.data.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _dogBreeds = MutableLiveData<List<DogBreed>>()
    val dogBreeds: LiveData<List<DogBreed>> get() = _dogBreeds

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun searchDogs(query: String) {
        val apiKey = "live_ejKtuMsWVvvZRY9iJMKAGMnDQUFEvUR78gsJe1TnZGPtd2UzY60eHgDo2Co5HxDO"
        RetrofitClient.dogApiService.searchDogs(apiKey, query).enqueue(object :
            Callback<List<DogBreed>> {
            override fun onResponse(call: Call<List<DogBreed>>, response: Response<List<DogBreed>>) {
                if (response.isSuccessful) {
                    _dogBreeds.postValue(response.body())
                } else {
                    _error.postValue("Error when receiving data")
                }
            }

            override fun onFailure(call: Call<List<DogBreed>>, t: Throwable) {
                _error.postValue("Error: ${t.message}")
            }
        })
    }
}