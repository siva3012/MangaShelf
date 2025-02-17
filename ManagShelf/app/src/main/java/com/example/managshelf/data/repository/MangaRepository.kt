package com.example.managshelf.data.repository

import com.example.managshelf.data.database.MangaEntity
import kotlinx.coroutines.flow.Flow

interface MangaRepository {
    fun getMangaData() : Flow<List<MangaEntity>>
    suspend fun loadMangaData()
    suspend fun updateManga(manga : MangaEntity)
    suspend fun networkState() : Flow<Boolean?>
}