package com.example.dognote.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.dognote.data.repository.AppDatabase
import com.example.dognote.data.models.DogNote
import com.example.dognote.databinding.ActivitySecondBinding
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация ViewBinding
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация бд
        database = AppDatabase.getDatabase(this)

        // Получение данных
        val dogBreedName = intent.getStringExtra("DOG_BREED_NAME")
        val dogImageUrl = intent.getStringExtra("DOG_IMAGE_URL")

        // Установка текста и изображения
        binding.dogBreedName.text = dogBreedName
        Glide.with(this).load(dogImageUrl).into(binding.dogImage)

        // Обработка изменения состояния чекбокса
        binding.sawDogCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.dogNicknameInput.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // Обработка нажатия на кнопку "Сохранить"
        binding.saveButton.setOnClickListener {
            val noteText = binding.noteInput.text.toString()
            val sawDog = binding.sawDogCheckBox.isChecked
            val dogNickname = if (sawDog) binding.dogNicknameInput.text.toString() else null

            // Сохранение заметки в базу данных
            if (dogBreedName != null) {
                lifecycleScope.launch {
                    val dogNote = DogNote(
                        breedName = dogBreedName,
                        note = noteText,
                        sawDog = sawDog,
                        nickname = dogNickname,
                        imageUrl = dogImageUrl
                    )
                    database.dogNoteDao().insertDogNote(dogNote)

                    Toast.makeText(this@SecondActivity, "Заметка сохранена", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
