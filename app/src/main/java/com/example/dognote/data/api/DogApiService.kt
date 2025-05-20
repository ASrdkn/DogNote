package com.example.dognote.data.api

import com.example.dognote.data.models.DogBreed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DogApiService {
    @GET("breeds")
    fun getDogBreeds(@Header("x-api-key") apiKey: String): Call<List<DogBreed>>
    @GET("breeds/search")
    fun searchDogs(@Header("x-api-key") apiKey: String, @Query("q") query: String): Call<List<DogBreed>>
}
