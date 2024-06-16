package com.example.fishsnap.adapter.newsAdapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.fishsnap.ui.home.HomeFragmentDirections
import com.example.fishsnap.auth.NewsItem
import com.example.fishsnap.data.dummy.DummyItemsNews
import com.example.fishsnap.databinding.ListNewsBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class NewsAdapter(private val context: Context, private var newsList: List<NewsItem>) :
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

    fun updateNewsList(newsList: List<NewsItem>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(private val binding: ListNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(newsItem: NewsItem) {
            binding.tvTitleNews.text = newsItem.title
            binding.tvDescriptionNews.text = newsItem.content
            binding.tvAuthorNews.text = newsItem.nameAuthor
            binding.tvDateNews.text = formatDate(newsItem.publicationAt)

            // Use Glide to load images
            Glide.with(context)
                .load(newsItem.urlThumbImg)
                .apply(RequestOptions().transform(RoundedCorners(60)))
                .into(binding.ivItemNews)

            // Set click listener
            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(newsItem.urlExternalNews)
                context.startActivity(intent)
            }
        }
        private fun formatDate(dateString: String): String {
            val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            apiDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = apiDateFormat.parse(dateString)

            val indonesianDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            return date?.let { indonesianDateFormat.format(it) } ?: dateString
        }
    }
}
