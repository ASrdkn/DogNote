package com.example.dognote.presentation.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dognote.R
import com.example.dognote.data.models.DogBreed
import com.example.dognote.databinding.ItemDogBreedBinding
import com.example.dognote.presentation.ui.SecondActivity

// Адаптер для RecyclerView, который отображает элементы списка пород собак
class DogBreedAdapter(
    private val context: Context // Контекст для запуска активностей
) : RecyclerView.Adapter<DogBreedAdapter.DogBreedViewHolder>() {

    private var dogItems: List<DogBreed> = listOf()

    // Внутренний класс для привязки одного элемента (ViewHolder)
    inner class DogBreedViewHolder(private val binding: ItemDogBreedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dogItem: DogBreed) {
            with(binding) {
                dogName.text = dogItem.name
                dogNote.visibility = View.GONE
                Glide.with(root)
                    .load(dogItem.image.url)
                    .placeholder(R.drawable.placeholder_dog_image)
                    .into(dogImage)
            }

            // Обработчик клика по элементу списка
            binding.root.setOnClickListener {
                val intent = Intent(context, SecondActivity::class.java).apply {
                    putExtra("DOG_BREED_NAME", dogItem.name) // Передаем имя породы
                    putExtra("DOG_IMAGE_URL", dogItem.image.url) // Передаем URL изображения породы
                }
                context.startActivity(intent) // Запускаем новую активность с переданными данными
            }
        }
    }

    // Метод для создания нового ViewHolder (элемента списка)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogBreedViewHolder {
        // Создаем привязку для элемента списка (элемент разметки)
        val binding = ItemDogBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogBreedViewHolder(binding) // Возвращаем новый ViewHolder
    }

    // Метод для привязки данных к ViewHolder
    override fun onBindViewHolder(holder: DogBreedViewHolder, position: Int) {
        holder.bind(dogItems[position]) // Привязываем данные из списка к элементу
    }

    // Метод для получения количества элементов в списке
    override fun getItemCount(): Int = dogItems.size

    // Метод для обновления данных адаптера
    fun submitList(list: List<DogBreed>) {
        dogItems = list
        notifyDataSetChanged() // Уведомляем адаптер, что данные изменились
    }
}