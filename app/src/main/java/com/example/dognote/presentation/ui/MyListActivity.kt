package com.example.dognote.presentation.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dognote.presentation.adapters.MyListAdapter
import com.example.dognote.presentation.viewmodel.DogNoteViewModel
import com.example.dognote.databinding.ActivityMyListBinding

class MyListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyListBinding
    private lateinit var myListAdapter: MyListAdapter
    private val dogNoteViewModel: DogNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Инициализация ViewBinding
        binding = ActivityMyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        //Наблюдаем за данными из ViewModel
        dogNoteViewModel.getAllDogNotes().observe(this, Observer { dogNotes ->
            myListAdapter = MyListAdapter(this, dogNotes)
            binding.recyclerView.adapter = myListAdapter
        })
    }

    private fun setupRecyclerView() {
        //Ориентация
        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Горизонтальная ориентация: 4 колонки
            GridLayoutManager(this, 4)
        } else {
            // Вертикальная ориентация: 1 колонка
            GridLayoutManager(this, 1)
        }

        binding.recyclerView.layoutManager = layoutManager
    }
    //Переинициализация при изменении ориентации
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupRecyclerView()
    }
}