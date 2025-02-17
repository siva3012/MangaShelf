package com.example.managshelf.domain

import com.example.managshelf.data.repository.MangaRepository
import kotlinx.coroutines.flow.Flow


class NetworkStateUseCase(private val mangaRepository: MangaRepository) {

   suspend fun execute() : Flow<Boolean?> {
        return mangaRepository.networkState()
    }
}