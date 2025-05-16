package com.example.dognote.presentation.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dognote.R
import com.example.dognote.presentation.adapters.MyListAdapter
import com.example.dognote.presentation.viewmodel.DogNoteViewModel
import com.example.dognote.databinding.ActivityMyListBinding
import kotlin.system.exitProcess

class MyListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyListBinding
    private lateinit var myListAdapter: MyListAdapter
    private val dogNoteViewModel: DogNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация ViewBinding
        binding = ActivityMyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "My list"
        // Настройка DrawerLayout и бокового меню
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Обработка кликов по пунктам бокового меню
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_main -> { // Переход в MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_exit -> { // Завершить приложение
                    finish()
                    exitProcess(0)
                }
                else -> false
            }
        }
        binding.navigationView.menu.findItem(R.id.nav_my_list).isVisible = false
        setupRecyclerView()

        // Наблюдаем за данными
        dogNoteViewModel.getAllDogNotes().observe(this, Observer { dogNotes ->
            myListAdapter = MyListAdapter(this, dogNotes)
            binding.recyclerView.adapter = myListAdapter
            // Если список пуст, показываем сообщение
            if (dogNotes.isEmpty()) {
                binding.emptyListMessage.visibility = View.VISIBLE
            } else {
                binding.emptyListMessage.visibility = View.GONE
            }
        })
    }

    private fun setupRecyclerView() {
        // Настройка RecyclerView в зависимости от ориентации экрана
        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 4) // 4 колонки для горизонтальной ориентации
        } else {
            GridLayoutManager(this, 1) // 1 колонка для вертикальной ориентации
        }

        binding.recyclerView.layoutManager = layoutManager
    }

    // Переинициализация при изменении ориентации
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupRecyclerView()
    }
}
