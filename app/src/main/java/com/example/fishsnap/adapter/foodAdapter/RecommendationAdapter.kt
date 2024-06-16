package com.example.fishsnap.adapter.foodAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fishsnap.R
import com.example.fishsnap.auth.ProductRecipe
import com.example.fishsnap.databinding.ItemRecommendationBinding

class RecommendationAdapter(
    private val onClick: (ProductRecipe) -> Unit
) : ListAdapter<ProductRecipe, RecommendationAdapter.RecommendationViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            onClick(recipe)
        }
    }

    inner class RecommendationViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: ProductRecipe) {
            binding.recipeNameTextView.text = recipe.name
            binding.recipeDescriptionTextView.text = recipe.description
            Glide.with(binding.recipeImageView.context)
                .load(recipe.urlImg)
                .into(binding.recipeImageView)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductRecipe>() {
        override fun areItemsTheSame(oldItem: ProductRecipe, newItem: ProductRecipe) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: ProductRecipe, newItem: ProductRecipe) = oldItem == newItem
    }
}
