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

class HistoryAdapter(private val histories: List<FishScanResponse>, private val onClick: (FishScanResponse) -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(histories[position])
    }

    override fun getItemCount(): Int = histories.size

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val historyFishImageView: ImageView = itemView.findViewById(R.id.iv_imageHistory)
        private val historyFishNameTextView: TextView = itemView.findViewById(R.id.tv_titleHistory)
        private val historyFishDescriptionTextView: TextView = itemView.findViewById(R.id.tv_descriptionHistory)

        fun bind(history: FishScanResponse) {
            historyFishNameTextView.text = history.name
            historyFishDescriptionTextView.text = history.description.joinToString(". ")
            Glide.with(itemView.context).load(history.urlImg).into(historyFishImageView)

            itemView.setOnClickListener {
                onClick(history)
            }
        }
    }
}
