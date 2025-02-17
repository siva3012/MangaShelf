package com.example.managshelf.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga_table")
data class MangaEntity(
    @PrimaryKey val id: String,
    val title: String,
    val score: Double,
    val popularity: Int,
    val year: Long,
    val imageUrl: String,
    val category: String,
    var fav : Boolean,
    var read : Boolean
)
