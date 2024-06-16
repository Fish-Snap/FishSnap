package com.example.fishsnap.adapter.carouselAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.fishsnap.data.dummy.CarouselItem
import com.example.fishsnap.databinding.ListFishBinding

class CarouselAdapter(private val items: List<CarouselItem>,  private val onItemClick: (CarouselItem) -> Unit) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(private val binding: ListFishBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CarouselItem) {
            with(binding) {
                Glide.with(carouselImageView.context)
                    .load(item.imageUrl)
                    .transform(RoundedCorners(8)) // Apply rounded corners transformation
                    .into(carouselImageView)
                carouselTextView.text = item.title// Update text based on position
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ListFishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(items[position])
    }

}