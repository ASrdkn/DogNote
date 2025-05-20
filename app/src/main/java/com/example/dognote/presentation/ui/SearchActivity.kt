package com.example.dognote.presentation.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dognote.R
import com.example.dognote.databinding.ActivitySearchBinding
import com.example.dognote.presentation.viewmodel.SearchViewModel
import com.example.dognote.presentation.adapters.DogBreedAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var dogBreedAdapter: DogBreedAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var noResultsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка тулбара
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Настройка RecyclerView
        dogBreedAdapter = DogBreedAdapter(this)
        binding.recyclerView.adapter = dogBreedAdapter
        setupRecyclerView()

        // Инициализация UI
        progressBar = findViewById(R.id.progressBar)
        noResultsText = findViewById(R.id.noResultsText)
        progressBar.visibility = ProgressBar.GONE
        noResultsText.visibility = TextView.VISIBLE

        // Подписка на LiveData
        setupObservers()

        // Настройка поля поиска
        val searchView = binding.toolbar.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)
        searchView.isIconified = false
        searchView.onActionViewExpanded()

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isEmpty()) {
                        noResultsText.text = "Please enter a search query."
                        noResultsText.visibility = TextView.VISIBLE
                        progressBar.visibility = ProgressBar.GONE
                    } else {
                        // Запуск поиска
                        noResultsText.visibility = TextView.GONE
                        progressBar.visibility = ProgressBar.VISIBLE
                        dogBreedAdapter.submitList(emptyList())
                        searchViewModel.searchDogs(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // Если запрос пустой, показываем сообщение и скрываем прогресс-бар
                    noResultsText.text = "No results found, check the correctness of the request."
                    noResultsText.visibility = TextView.VISIBLE
                    progressBar.visibility = ProgressBar.GONE
                    dogBreedAdapter.submitList(emptyList()) // Очистить список
                }
                return true
            }
        })
    }

    private fun setupObservers() {
        searchViewModel.dogBreeds.observe(this, Observer { dogBreeds ->
            progressBar.visibility = ProgressBar.GONE
            if (dogBreeds.isNullOrEmpty()) {
                noResultsText.visibility = TextView.VISIBLE
                dogBreedAdapter.submitList(emptyList())
            } else {
                noResultsText.visibility = TextView.GONE
                dogBreedAdapter.submitList(dogBreeds)
            }
        })

        searchViewModel.error.observe(this, Observer { errorMessage ->
            progressBar.visibility = ProgressBar.GONE
            noResultsText.text = errorMessage
            noResultsText.visibility = TextView.VISIBLE
            dogBreedAdapter.submitList(emptyList())
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 4)
        } else {
            LinearLayoutManager(this)
        }
        binding.recyclerView.layoutManager = layoutManager
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupRecyclerView()
    }
}