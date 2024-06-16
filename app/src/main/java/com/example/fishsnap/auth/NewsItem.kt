package com.example.fishsnap.auth

import com.google.gson.annotations.SerializedName

data class NewsItem(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("urlThumbImg") val urlThumbImg: String?,
    @SerializedName("urlHeaderImg") val urlHeaderImg: String?,
    @SerializedName("urlExternalNews") val urlExternalNews: String,
    @SerializedName("publicationAt") val publicationAt: String,
    @SerializedName("idAdmin") val idAdmin: String,
    @SerializedName("nameAuthor") val nameAuthor: String,
    @SerializedName("type") val type: String,
    @SerializedName("idCategoryNews") val idCategoryNews: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)