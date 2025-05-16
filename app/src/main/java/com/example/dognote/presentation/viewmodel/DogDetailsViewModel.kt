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

class DogDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val dogNoteDao = AppDatabase.getDatabase(application).dogNoteDao()

    // LiveData для хранения информации о собаке
    private val _dogNote = MutableLiveData<DogNote?>() // Обновляем на Nullable
    val dogNote: LiveData<DogNote?> get() = _dogNote

    // LiveData для статуса операции (например, успешное сохранение)
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status

    // Загрузка данных о собаке по ID
    fun loadDogDetails(dogId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val dogNote = dogNoteDao.getDogNoteById(dogId)
            if (dogNote != null) {
                _dogNote.postValue(dogNote)
            } else {
                _status.postValue("Ошибка загрузки данных о собаке")
            }
        }
    }

    // Удаление заметки о собаке
    fun deleteDogNote(dogId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dogNoteDao.deleteDogNoteById(dogId)
            _status.postValue("Запись удалена")
        }
    }

    // Сохранение обновленных данных о собаке
    fun saveDogNote(updatedDogNote: DogNote) {
        viewModelScope.launch(Dispatchers.IO) {
            dogNoteDao.updateDogNote(updatedDogNote)
            _status.postValue("Заметка обновлена")
        }
    }
}

