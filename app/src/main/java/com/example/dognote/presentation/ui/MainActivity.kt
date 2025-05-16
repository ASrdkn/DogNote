package com.example.dognote.presentation.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dognote.presentation.adapters.DogBreedAdapter
import com.example.dognote.R
import com.example.dognote.databinding.ActivityMainBinding
import com.example.dognote.presentation.viewmodel.DogNoteViewModel
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Инициализация ViewBinding
    private lateinit var dogBreedAdapter: DogBreedAdapter
    private val dogNoteViewModel: DogNoteViewModel by viewModels() // Используем ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Инициализация binding
        setContentView(binding.root) // Установка корневого представления

        // Настройка toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Main"
        // Боковое меню управление
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Элементы бокового меню
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_my_list -> {
                    startActivity(Intent(this, MyListActivity::class.java))
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_exit -> {
                    finish()
                    exitProcess(0)
                }
                else -> false
            }
        }
        binding.navigationView.menu.findItem(R.id.nav_main).isVisible = false
        setupRecyclerView()
        setupObserver()

        // Загружаем данные о породах собак
        dogNoteViewModel.fetchDogBreeds() // Загружаем данные с API
    }

    // Настройка RecyclerView в зависимости от ориентации
    private fun setupRecyclerView() {
        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Горизонтальная ориентация: 4 колонки
            GridLayoutManager(this, 4)
        } else {
            // Вертикальная ориентация: 1 колонка
            LinearLayoutManager(this)
        }
        binding.recyclerView.layoutManager = layoutManager
        dogBreedAdapter = DogBreedAdapter(this)
        binding.recyclerView.adapter = dogBreedAdapter
    }

    // Подписка на изменения в LiveData
    private fun setupObserver() {
        // Получаем данные через ViewModel и обновляем адаптер
        dogNoteViewModel.dogBreeds.observe(this, Observer { dogBreeds ->
            dogBreedAdapter.submitList(dogBreeds) // Обновление адаптера с новыми данными
        })
        // Подписка на ошибки из ViewModel
        dogNoteViewModel.error.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show() // Показываем ошибку
        })
    }

    // Перенастройка layoutmanager при изменении ориентации
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupRecyclerView()
    }
    // Открытие бокового меню при нажатии
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(binding.navigationView) // Открытие бокового меню
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}