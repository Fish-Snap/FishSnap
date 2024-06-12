package com.example.fishsnap.adapter.foodAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishsnap.R
import com.example.fishsnap.auth.Location

class LocationAdapter(private val locations: List<Location>) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(locations[position])
    }

    override fun getItemCount(): Int = locations.size

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val locationNameTextView: TextView = itemView.findViewById(R.id.fishLocationTextView)
        private val locationDescriptionTextView: TextView = itemView.findViewById(R.id.fishLocationDescriptionTextView)

        fun bind(location: Location) {
            locationNameTextView.text = location.name
            locationDescriptionTextView.text = location.description
        }
    }
}
