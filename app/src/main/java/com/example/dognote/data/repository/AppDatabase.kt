package com.example.dognote.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dognote.data.models.DogNote

@Database(entities = [DogNote::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dogNoteDao(): DogNoteDao

    companion object {
        //Видимость изменений между потоками
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //Проверка наличия БД
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dog_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}