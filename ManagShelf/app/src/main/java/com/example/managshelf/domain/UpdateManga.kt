package com.example.managshelf.domain

import com.example.managshelf.data.database.MangaEntity
import com.example.managshelf.data.repository.MangaRepository

class UpdateManga(private val mangaRepository: MangaRepository) {
    suspend fun execute(manga:MangaEntity){
        mangaRepository.updateManga(manga)
    }
}