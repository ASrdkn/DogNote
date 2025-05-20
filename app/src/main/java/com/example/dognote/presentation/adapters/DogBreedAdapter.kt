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
    private val context: Context
) : RecyclerView.Adapter<DogBreedAdapter.DogBreedViewHolder>() {

    private var dogItems: List<DogBreed> = listOf()

    inner class DogBreedViewHolder(private val binding: ItemDogBreedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dogItem: DogBreed) {
            with(binding) {
                dogName.text = dogItem.name
                dogNote.visibility = View.GONE

                // Проверяем, что image не null, и url тоже
                val imageUrl = dogItem.image?.url
                if (imageUrl != null) {
                    Glide.with(root)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_dog_image)
                        .into(dogImage)
                } else {
                    // Если URL null, загружаем изображение-заглушку
                    Glide.with(root)
                        .load(R.drawable.placeholder_dog_image)
                        .into(dogImage)
                }
            }

            // Обработчик клика по элементу списка
            binding.root.setOnClickListener {
                val intent = Intent(context, SecondActivity::class.java).apply {
                    putExtra("DOG_BREED_NAME", dogItem.name)
                    val imageUrl = dogItem.image?.url // Проверка на null
                    if (imageUrl != null) {
                        putExtra("DOG_IMAGE_URL", imageUrl)
                    }
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogBreedViewHolder {
        val binding = ItemDogBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogBreedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogBreedViewHolder, position: Int) {
        holder.bind(dogItems[position])
    }

    override fun getItemCount(): Int = dogItems.size

    fun submitList(list: List<DogBreed>) {
        dogItems = list
        notifyDataSetChanged()
    }
}