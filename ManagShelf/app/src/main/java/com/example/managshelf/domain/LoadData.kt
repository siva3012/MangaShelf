package com.example.managshelf.domain

import com.example.managshelf.data.repository.MangaRepository

class LoadData(private val mangaRepository: MangaRepository) {
    suspend fun execute(){
        mangaRepository.loadMangaData()
    }
}