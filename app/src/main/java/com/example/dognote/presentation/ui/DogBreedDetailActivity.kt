package com.example.dognote.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dognote.databinding.ActivityDogBreedDetailBinding

class DogBreedDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDogBreedDetailBinding // Инициализация ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Настраиваем ViewBinding
        binding = ActivityDogBreedDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получение данных из интента
        val dogBreedName = intent.getStringExtra("DOG_BREED_NAME")
        val dogBreedDescription = intent.getStringExtra("DOG_BREED_DESCRIPTION")
        val dogImageUrl = intent.getStringExtra("DOG_IMAGE_URL")

        // Настройка UI с использованием ViewBinding
        binding.dogBreedName.text = dogBreedName
        binding.dogBreedDescription.text = dogBreedDescription

        // Загрузка изображения с помощью Glide
        Glide.with(this)
            .load(dogImageUrl)
            .into(binding.dogImage) // Используем binding для ImageView
    }
}