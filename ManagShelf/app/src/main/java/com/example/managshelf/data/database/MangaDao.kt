package com.example.managshelf.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(mangaList: List<MangaEntity>)

    @Query("SELECT * FROM manga_table")
    fun getAllData(): List<MangaEntity>

    @Update
    suspend fun updateAll(mangaList: List<MangaEntity>)

    @Query("SELECT * FROM manga_table WHERE id = :id LIMIT 1")
    suspend fun getMangaById(id: String): MangaEntity?

   @Update
   suspend fun updateManga(manga: MangaEntity)
}