package com.example.dognote.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dognote.data.models.DogNote
import com.example.dognote.data.repository.AppDatabase
import com.example.dognote.data.repository.DogNoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DogNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val dogNoteDao: DogNoteDao = AppDatabase.getDatabase(application).dogNoteDao()

    // Функция для получения всех записей о собаках
    fun getAllDogNotes(): LiveData<List<DogNote>> {
        // Возвращаем LiveData, которая будет автоматически обновляться
        return dogNoteDao.getAllNotes()
    }

    fun insertNote(note: DogNote) {
        viewModelScope.launch(Dispatchers.IO) {
            dogNoteDao.insertDogNote(note)
        }
    }
}