package com.example.dognote.presentation.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dognote.R
import com.example.dognote.databinding.ActivityMainBinding
import com.example.dognote.presentation.adapters.DogBreedAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding //Инициализация ViewBinding
    private lateinit var dogBreedAdapter: DogBreedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) //Инициализация binding
        setContentView(binding.root) //Установка корневого представления

        //Настройка toolbar
        setSupportActionBar(binding.toolbar)

        //Боковое меню управление
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //Элементы бокового меню
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_my_list -> {
                    startActivity(Intent(this, MyListActivity::class.java))
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_exit -> {
                    finish()
                    System.exit(0)
                    true
                }
                else -> false
            }
        }
        setupRecyclerView()
        fetchDogBreeds()
    }

    //LayoutManager в зависимости от ориентации
    private fun setupRecyclerView() {
        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Горизонтальная ориентация: 4 колонки
            GridLayoutManager(this, 4)
        } else {
            // Вертикальная ориентация: 1 колонка
            LinearLayoutManager(this)
        }
        binding.recyclerView.layoutManager = layoutManager
    }

    // Загрузка данных из API
    private fun fetchDogBreeds() {
        val apiKey = "live_ejKtuMsWVvvZRY9iJMKAGMnDQUFEvUR78gsJe1TnZGPtd2UzY60eHgDo2Co5HxDO"
        RetrofitClient.dogApiService.getDogBreeds(apiKey).enqueue(object :
            Callback<List<DogBreed>> {
            override fun onResponse(call: Call<List<DogBreed>>, response: Response<List<DogBreed>>) {
                if (response.isSuccessful) {
                    response.body()?.let { dogBreeds ->
                        dogBreedAdapter = DogBreedAdapter(this@MainActivity, dogBreeds)
                        binding.recyclerView.adapter = dogBreedAdapter // Используем binding для доступа к RecyclerView
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Ошибка получения данных: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<DogBreed>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(binding.navigationView) //Открытие бокового меню
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    //Перенастройка layoutmanager при изменении ориентации
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupRecyclerView()
    }
}
