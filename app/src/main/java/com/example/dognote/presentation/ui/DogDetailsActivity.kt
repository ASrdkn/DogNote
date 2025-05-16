package com.example.dognote.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.dognote.R
import com.example.dognote.data.models.DogNote
import com.example.dognote.databinding.ActivityDogDetailsBinding
import com.example.dognote.presentation.viewmodel.DogDetailsViewModel

class DogDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDogDetailsBinding
    private val dogDetailsViewModel: DogDetailsViewModel by viewModels()

    private var currentDogId: Int = -1
    private var isEditing: Boolean = false
    private var currentImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация ViewBinding
        binding = ActivityDogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получение данных о собаке
        currentDogId = intent.getIntExtra("DOG_ID", -1)

        if (currentDogId != -1) {
            dogDetailsViewModel.loadDogDetails(currentDogId)
        } else {
            Toast.makeText(this, "Error loading dog details", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Наблюдение за изменениями в данных
        dogDetailsViewModel.dogNote.observe(this, Observer { dogNote ->
            // Проверка на null перед вызовом метода displayDogDetails
            dogNote?.let {
                displayDogDetails(it)
            } ?: run {
                Toast.makeText(this, "Dog details not found", Toast.LENGTH_SHORT).show()
            }
        })

        // Наблюдение за статусом операции (сохранение или удаление)
        dogDetailsViewModel.status.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (message == "Заметка обновлена" || message == "Запись удалена") {
                finish()
            }
        })

        // Обработка редактирования
        binding.editIcon.setOnClickListener {
            if (isEditing) {
                saveDogNote()
            } else {
                enableEditing()
            }
        }

        // Удаление заметки
        binding.deleteIcon.setOnClickListener {
            dogDetailsViewModel.deleteDogNote(currentDogId)
        }

        // Чекбокс
        binding.sawDogCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.dogNicknameInput.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

    // Отображение данных о собаке
    private fun displayDogDetails(dogNote: DogNote) {
        currentImageUrl = dogNote.imageUrl
        with(binding) {
            dogBreedName.text = dogNote.breedName
            noteInput.setText(dogNote.note)
            dogNicknameInput.setText(dogNote.nickname)
            sawDogCheckBox.isChecked = dogNote.sawDog

            // Видимость поля ввода клички
            dogNicknameInput.visibility = if (dogNote.sawDog) View.VISIBLE else View.GONE

            Glide.with(this@DogDetailsActivity).load(dogNote.imageUrl).into(dogImage)

            // Отключение редактирования при просмотре
            noteInput.isEnabled = false
            dogNicknameInput.isEnabled = false
            sawDogCheckBox.isEnabled = false
        }
    }

    // Режим редактирования
    private fun enableEditing() {
        isEditing = true
        with(binding) {
            noteInput.isEnabled = true
            dogNicknameInput.isEnabled = true
            sawDogCheckBox.isEnabled = true
            editIcon.setImageResource(R.drawable.ic_save)
        }
    }

    // Сохранение изменений
    private fun saveDogNote() {
        val updatedNote = binding.noteInput.text.toString()
        val updatedNickname = binding.dogNicknameInput.text.toString()
        val sawDogChecked = binding.sawDogCheckBox.isChecked

        val updatedDogNote = DogNote(
            id = currentDogId,
            breedName = binding.dogBreedName.text.toString(),
            note = updatedNote,
            sawDog = sawDogChecked,
            nickname = updatedNickname,
            imageUrl = currentImageUrl
        )

        dogDetailsViewModel.saveDogNote(updatedDogNote)
    }
}
