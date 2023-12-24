package com.bignerdranch.android.animalgallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bignerdranch.android.animalgallery.api.Animal
import com.bignerdranch.android.animalgallery.databinding.ListItemAnimalBinding

class AnimalViewHolder(
    private val binding: ListItemAnimalBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(galleryItem: Animal, onItemClicked: (Uri) -> Unit) {
        //fill in animal info in list item
        val photo = galleryItem.photos?.firstOrNull { it.medium != null}?.medium
        binding.animalPhoto.load(photo) {
            placeholder(R.drawable.dog_holding_heart)
            fallback(R.drawable.dog_holding_heart)
        }

        binding.animalName.text = galleryItem.name

        val age = galleryItem.age.orEmpty()
        val gender = galleryItem.gender.orEmpty()
        val breed = galleryItem.breeds?.primary.orEmpty()

        val animalInfo = "$age • $gender • $breed"

        binding.animalInfo.text = animalInfo
        binding.root.setOnClickListener { onItemClicked(galleryItem.animalPageUri) }
    }
}

class AnimalAdapter(
    private val galleryItems: List<Animal>,
    private val onItemClicked: (Uri) -> Unit
) : RecyclerView.Adapter<AnimalViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemAnimalBinding.inflate(inflater, parent, false)
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val item = galleryItems[position]
        holder.bind(item, onItemClicked)
    }

    override fun getItemCount() = galleryItems.size
}
