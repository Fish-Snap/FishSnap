package com.example.fishsnap.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FishScanResponse(
    val id: String,
    val idUser: String,
    val codeFishModel: String,
    val name: String,
    val scientificName: String,
    var urlImg: String,
    val otherNames: List<String>,
    val description: List<String>,
    val productRecipe: List<ProductRecipe>,
    val location: List<Location>,
    var annotatedImagePath: String?,
    val createdAt: String
) : Parcelable

@Parcelize
data class ProductRecipe(
    val name: String,
    val steps: List<String>,
    val urlImg: String,
    val material: List<String>,
    val description: String
) : Parcelable

@Parcelize
data class Location(
    val name: String,
    val description: String
) : Parcelable