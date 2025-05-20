package com.example.dognote.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dognote.data.api.RetrofitClient
import com.example.dognote.data.models.DogBreed
import com.example.dognote.data.models.DogNote
import com.example.dognote.data.repository.AppDatabase
import com.example.dognote.data.repository.DogNoteDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DogNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val dogNoteDao: DogNoteDao = AppDatabase.getDatabase(application).dogNoteDao()

    // LiveData для данных из базы данных
    fun getAllDogNotes(): LiveData<List<DogNote>> {
        return dogNoteDao.getAllNotes()
    }

    // LiveData для данных о породах собак из API
    private val _dogBreeds = MutableLiveData<List<DogBreed>>()
    val dogBreeds: LiveData<List<DogBreed>> get() = _dogBreeds

    // LiveData для ошибок
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Функция для загрузки данных о породах собак из API
    fun fetchDogBreeds() {
        val apiKey = "live_ejKtuMsWVvvZRY9iJMKAGMnDQUFEvUR78gsJe1TnZGPtd2UzY60eHgDo2Co5HxDO"

        RetrofitClient.dogApiService.getDogBreeds(apiKey).enqueue(object : Callback<List<DogBreed>> {
            override fun onResponse(call: Call<List<DogBreed>>, response: Response<List<DogBreed>>) {
                if (response.isSuccessful) {
                    // Когда данные получены, обновляем LiveData
                    _dogBreeds.postValue(response.body())
                }  else {
                    _error.postValue("Error when receiving data") // Обновляем LiveData с ошибкой
                }
            }

            override fun onFailure(call: Call<List<DogBreed>>, t: Throwable) {
                _error.postValue("Error: ${t.message}")
            }
        })
    }

}