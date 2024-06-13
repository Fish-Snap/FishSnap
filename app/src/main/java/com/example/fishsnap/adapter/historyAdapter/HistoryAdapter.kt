package com.example.fishsnap.adapter.historyAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

import com.example.fishsnap.R
import com.example.fishsnap.auth.FishScanResponse
import com.example.fishsnap.data.dummy.DummyItemsHistory
import com.example.fishsnap.databinding.ListHistoryBinding

class HistoryAdapter(
    private val historyList: List<FishScanResponse>,
    private val onItemClick: (FishScanResponse) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ListHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fishScanResponse: FishScanResponse) {
            binding.tvTitleHistory.text = fishScanResponse.name
            binding.tvDescriptionHistory.text = fishScanResponse.description.joinToString("\n")
            Glide.with(binding.ivImageHistory.context)
                .load(fishScanResponse.annotatedImagePath ?: fishScanResponse.urlImg)
                .into(binding.ivImageHistory)
            binding.root.setOnClickListener {
                onItemClick(fishScanResponse)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}

