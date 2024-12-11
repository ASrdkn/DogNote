package com.example.dogs.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.thedogapi.com/v1/"

    // Создаем экземпляр Retrofit с базовым URL и конвертером JSON
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Объявляем dogApiService как объект, чтобы можно было к нему обратиться из других классов
    val dogApiService: DogApiService = retrofit.create(DogApiService::class.java)
}
