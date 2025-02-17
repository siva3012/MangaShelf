package com.example.managshelf.data.network

import com.google.gson.annotations.SerializedName

data class MangaDataNet(
    @SerializedName("id") val id: String,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("score") val score: Double,
    @SerializedName("popularity") val popularity: Int,
    @SerializedName("title") val title: String,
    @SerializedName("publishedChapterDate") val publishedChapterDate: Long,
    @SerializedName("category") val category: String
)
