package com.example.fishsnap.adapter.historyAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.fishsnap.HistoryFragmentDirections
import com.example.fishsnap.data.dummy.DummyItemsHistory
import com.example.fishsnap.databinding.ListHistoryBinding

class HistoryAdapter(private val context: Context, private val newList: List<DummyItemsHistory>) :
RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){

    inner class HistoryViewHolder(private val binding: ListHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(newsItem: DummyItemsHistory){
            binding.tvTitleHistory.text = newsItem.title
            binding.tvDescriptionHistory.text = newsItem.description

            Glide.with(context)
                .load(newsItem.image)
                .apply(RequestOptions().transform(RoundedCorners(16)))
                .into(binding.ivImageHistory)

            binding.root.setOnClickListener {
                val action = HistoryFragmentDirections.actionHistoryFragmentToDetailHistoryFragment()
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ListHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
      return newList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
      val newsItem = newList[position]
        holder.bind(newsItem)
    }
}