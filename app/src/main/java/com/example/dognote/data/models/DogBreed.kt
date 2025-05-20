package com.example.dognote.data.models

data class DogBreed(
    val name: String,
    val image: DogImage,
)

data class DogImage(
    val url: String
)