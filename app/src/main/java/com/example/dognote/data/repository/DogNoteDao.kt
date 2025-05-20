package com.example.dognote.data.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dognote.data.models.DogNote

@Dao
interface DogNoteDao {
    //Добавление новой заметки
    @Insert
    suspend fun insertDogNote(dogNote: DogNote)
    //Получение заметки по ID
    @Query("SELECT * FROM dog_notes WHERE id = :id")
    suspend fun getDogNoteById(id: Int): DogNote?
    //Получение всех заметок
    @Query("SELECT * FROM dog_notes")
    fun getAllNotes(): LiveData<List<DogNote>>
    //Обновление заметки
    @Update
    suspend fun updateDogNote(dogNote: DogNote)
    //Удаление заметки по ID
    @Query("DELETE FROM dog_notes WHERE id = :id")
    suspend fun deleteDogNoteById(id: Int)
}