package com.example.dognote.presentation.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.dognote.data.models.DogNote
import com.example.dognote.databinding.ActivitySecondBinding
import com.example.dognote.presentation.viewmodel.SecondActivityViewModel

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private val secondActivityViewModel: SecondActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация ViewBinding
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка тулбара
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "Creating a note"

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
                val dogNote = DogNote(
                    breedName = dogBreedName,
                    note = noteText,
                    sawDog = sawDog,
                    nickname = dogNickname,
                    imageUrl = dogImageUrl
                )
                secondActivityViewModel.saveDogNote(dogNote)
            }
        }

        // Подписка на статус сохранения
        secondActivityViewModel.saveStatus.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (message == "The note is saved") {
                finish()
            }
        })
    }

    // Обработка нажатия на стрелку "назад"
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}