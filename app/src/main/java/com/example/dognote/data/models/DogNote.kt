package com.example.dognote.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dog_notes")
data class DogNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val breedName: String,
    val note: String,
    val sawDog: Boolean,
    val nickname: String?,
    val imageUrl: String?
)