package com.example.managshelf.domain

import com.example.managshelf.data.database.MangaEntity
import com.example.managshelf.data.repository.MangaRepository
import kotlinx.coroutines.flow.Flow

class FetchMangaDataUseCase(private val mangaRepository: MangaRepository) {
    fun execute() : Flow<List<MangaEntity>> {
        return mangaRepository.getMangaData()
    }
}