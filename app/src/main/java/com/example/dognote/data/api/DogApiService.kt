package com.example.dogs.data.api

import com.example.dogs.data.models.DogBreed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface DogApiService {
    @GET("breeds")
    fun getDogBreeds(@Header("x-api-key") apiKey: String): Call<List<DogBreed>>
}
