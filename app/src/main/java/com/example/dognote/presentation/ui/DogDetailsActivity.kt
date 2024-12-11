package com.example.dognote.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.dognote.R
import com.example.dognote.data.repository.AppDatabase
import com.example.dognote.data.models.DogNote
import com.example.dognote.data.repository.DogNoteDao
import com.example.dognote.databinding.ActivityDogDetailsBinding
import kotlinx.coroutines.launch

class DogDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDogDetailsBinding
    private lateinit var dogNoteDao: DogNoteDao
    private var currentDogId: Int = -1
    private var isEditing: Boolean = false
    private var currentImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Инициализация ViewBinding
        binding = ActivityDogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dogNoteDao = AppDatabase.getDatabase(this).dogNoteDao()
        currentDogId = intent.getIntExtra("DOG_ID", -1)

        //Проверка ID на корректность
        if (currentDogId != -1) {
            loadDogDetails(currentDogId)
        } else {
            Toast.makeText(this, "Error loading dog details", Toast.LENGTH_SHORT).show()
            finish()
        }

        //Обработка редактирования
        binding.editIcon.setOnClickListener {
            if (isEditing) {
                saveDogNote()
            } else {
                enableEditing()
            }
        }

        //Удаление заметки
        binding.deleteIcon.setOnClickListener {
            deleteDogNote()
        }

        //Чекбокс
        binding.sawDogCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.dogNicknameInput.visibility = View.VISIBLE
            } else {
                binding.dogNicknameInput.visibility = View.GONE
            }
        }
    }

    //Загрузка данных о собаке
    private fun loadDogDetails(dogId: Int) {
        lifecycleScope.launch {
            val dogNote = dogNoteDao.getDogNoteById(dogId)
            if (dogNote != null) {
                currentImageUrl = dogNote.imageUrl
                displayDogDetails(dogNote)
            }
        }
    }

    //Отображение данных о собаке
    private fun displayDogDetails(dogNote: DogNote) {
        with(binding) {
            dogBreedName.text = dogNote.breedName
            noteInput.setText(dogNote.note)
            dogNicknameInput.setText(dogNote.nickname)
            sawDogCheckBox.isChecked = dogNote.sawDog

            //Видимость поля ввода клички
            if (dogNote.sawDog) {
                dogNicknameInput.visibility = View.VISIBLE
            } else {
                dogNicknameInput.visibility = View.GONE
            }

            Glide.with(this@DogDetailsActivity).load(dogNote.imageUrl).into(dogImage)

            //Отключение редактирования при просмотре
            noteInput.isEnabled = false
            dogNicknameInput.isEnabled = false
            sawDogCheckBox.isEnabled = false
        }
    }

    //Удаление заметки
    private fun deleteDogNote() {
        lifecycleScope.launch {
            dogNoteDao.deleteDogNoteById(currentDogId)
            Toast.makeText(this@DogDetailsActivity, "Запись удалена", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    //Режим редактирования
    private fun enableEditing() {
        isEditing = true
        with(binding) {
            noteInput.isEnabled = true
            dogNicknameInput.isEnabled = true
            sawDogCheckBox.isEnabled = true
            editIcon.setImageResource(R.drawable.ic_save)
        }
    }

    //Сохранение
    private fun saveDogNote() {
        val updatedNote = binding.noteInput.text.toString()
        val updatedNickname = binding.dogNicknameInput.text.toString()
        val sawDogChecked = binding.sawDogCheckBox.isChecked

        lifecycleScope.launch {
            val updatedDogNote = DogNote(
                id = currentDogId,
                breedName = binding.dogBreedName.text.toString(),
                note = updatedNote,
                sawDog = sawDogChecked,
                nickname = updatedNickname,
                imageUrl = currentImageUrl ?: ""
            )

            dogNoteDao.updateDogNote(updatedDogNote)
            Toast.makeText(this@DogDetailsActivity, "Заметка обновлена", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}