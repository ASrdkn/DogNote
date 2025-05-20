package com.example.dognote.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dognote.data.models.DogNote
import com.example.dognote.data.repository.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SecondActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)

    // LiveData для отслеживания успеха или ошибки при сохранении
    private val _saveStatus = MutableLiveData<String>()
    val saveStatus: LiveData<String> get() = _saveStatus

    // Функция для сохранения заметки в базе данных
    fun saveDogNote(dogNote: DogNote) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.dogNoteDao().insertDogNote(dogNote)
                _saveStatus.postValue("The note is saved")
            } catch (e: Exception) {
                _saveStatus.postValue("Error when saving")
            }
        }
    }
}