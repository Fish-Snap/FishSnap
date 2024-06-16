package com.example.fishsnap.data.dummy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarouselItem(
    val imageUrl: String,
    val title: String,
    val description: String,
    val type: String,
    val manfaat: List<ItemDetail>? = null,
    val fakta: List<ItemDetail>? = null,
    val itemType: String,
    val sumber: String
) : Parcelable

@Parcelize
data class ItemDetail(
    val judul: String,
    val deskripsi: String
) : Parcelable
