package com.example.fishsnap.adapter.carouselAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.fishsnap.databinding.ListFishBinding

class CarouselAdapter(private val images: List<String>,  private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(private val binding: ListFishBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            with(binding) {
                Glide.with(carouselImageView.context)
                    .load(imageUrl)
                    .transform(RoundedCorners(8)) // Apply rounded corners transformation
                    .into(carouselImageView)
                carouselTextView.text = "Dummy Ikan" // Update text based on position
                root.setOnClickListener { onItemClick(imageUrl) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ListFishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(images[position])
    }

}