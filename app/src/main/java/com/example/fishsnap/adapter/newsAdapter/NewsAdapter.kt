package com.example.fishsnap.adapter.newsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.fishsnap.HomeFragmentDirections
import com.example.fishsnap.data.dummy.DummyItemsNews
import com.example.fishsnap.databinding.ListNewsBinding

class NewsAdapter(private val context: Context, private val newsList: List<DummyItemsNews>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ListNewsBinding.inflate(LayoutInflater.from(context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class NewsViewHolder(private val binding: ListNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(newsItem: DummyItemsNews) {
            binding.tvTitleNews.text = newsItem.title
            binding.tvDescriptionNews.text = newsItem.description
            binding.tvAuthorNews.text = newsItem.author
            binding.tvDateNews.text = newsItem.date

            // Use Glide to load images
            Glide.with(context)
                .load(newsItem.imageUrl)
                .apply(RequestOptions().transform(RoundedCorners(60)))
                .into(binding.ivItemNews)

            // Set click listener
            binding.root.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailNewsFragment()
                it.findNavController().navigate(action)
            }
        }
    }
}