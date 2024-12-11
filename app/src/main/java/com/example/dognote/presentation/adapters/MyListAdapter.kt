package com.example.dognote.presentation.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dognote.R
import com.example.dognote.presentation.ui.DogDetailsActivity
import com.example.dognote.data.models.DogNote
import com.example.dognote.databinding.ItemDogBreedBinding


class MyListAdapter(
    private val context: Context,
    private val dogNotes: List<DogNote>
) : RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {

    inner class MyListViewHolder(private val binding: ItemDogBreedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dogNote: DogNote) {
            // Проверяем наличие клички и устанавливаем текст в зависимости от её наличия
            binding.dogName.text = if (!dogNote.nickname.isNullOrBlank()) {
                "${dogNote.breedName}: ${dogNote.nickname}"
            } else {
                dogNote.breedName
            }

            // Загрузка изображения из URL, хранящегося в базе данных
            Glide.with(binding.root)
                .load(dogNote.imageUrl)
                .placeholder(R.drawable.placeholder_dog_image)
                .into(binding.dogImage)

            // Переход к DogDetailsActivity при клике на элемент
            binding.root.setOnClickListener {
                val intent = Intent(context, DogDetailsActivity::class.java).apply {
                    putExtra("DOG_ID", dogNote.id)  // Передаем ID собаки для поиска в базе
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        // Используем ViewBinding для создания ViewHolder
        val binding = ItemDogBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        holder.bind(dogNotes[position])
    }

    override fun getItemCount(): Int = dogNotes.size
}
