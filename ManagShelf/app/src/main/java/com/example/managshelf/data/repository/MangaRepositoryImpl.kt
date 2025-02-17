package com.example.managshelf.data.repository

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import com.example.managshelf.data.database.MangaDatabase
import com.example.managshelf.data.database.MangaEntity
import com.example.managshelf.data.network.MangaApi
import com.example.managshelf.data.network.MangaDataNet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MangaRepositoryImpl(private val api: MangaApi, private val database : MangaDatabase,private val context: Context) :
    MangaRepository,NetworkDataSource {
    private val mangaEntity : MutableStateFlow<List<MangaEntity>> = MutableStateFlow(emptyList())
    private val networkState : MutableStateFlow<Boolean?> = MutableStateFlow(null)

    init {
        registerReceiver()
    }

    override fun getMangaData(): MutableStateFlow<List<MangaEntity>> {
        return mangaEntity
    }

    override suspend fun loadMangaData() {
        fetchApi()
        loadDataFromDb()
    }

    override suspend fun updateManga(manga: MangaEntity) {
        database.mangaDao().updateManga(manga)
    }

    override suspend fun networkState() : Flow<Boolean?> {
        return networkState
    }


    private fun loadDataFromDb(){
        mangaEntity.value = database.mangaDao().getAllData()
    }
    private suspend fun fetchApi() {
        try {
            val apiMangaList = api.getMangaListFromNet().map { it.toMangaData() }

            val newMangaList = mutableListOf<MangaEntity>()
            val updateMangaList = mutableListOf<MangaEntity>()

            for (apiManga in apiMangaList) {
                val existingManga = database.mangaDao().getMangaById(apiManga.id)

                if (existingManga == null) {
                    newMangaList.add(apiManga)
                } else {
                    val updatedManga = apiManga.copy(
                        fav = existingManga.fav,
                        read = existingManga.read
                    )
                    if (updatedManga != existingManga) {
                        updateMangaList.add(updatedManga)
                    }
                }
            }
            if (newMangaList.isNotEmpty()) database.mangaDao().insertAll(newMangaList)
            if (updateMangaList.isNotEmpty()) database.mangaDao().updateAll(updateMangaList)
            loadDataFromDb()

        } catch (e: Exception) {
            Log.d("Repository","NetworkCall Failed ${networkState.value}")
            if (networkState.value==true) {
                fetchApi()
            }
        }
    }


    private fun MangaDataNet.toMangaData(): MangaEntity {
        return MangaEntity(
            id = id,
            title = title,
            score = score,
            popularity = popularity,
            year = publishedChapterDate,
            imageUrl = imageUrl,
            category = category,
            fav = false,
            read = false
        )
    }

    private fun registerReceiver(){
        val networkReceiver = NetworkReceiver(this)
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
       context.registerReceiver(networkReceiver, intentFilter)
    }

    fun unregisterReceiver(){
        try {
            val networkReceiver = NetworkReceiver(this)
            context.unregisterReceiver(networkReceiver)
        }catch (e: Exception){
           Log.d("EXCEPTION","$e")
        }
    }

    override fun internetState(state: Boolean) {
        networkState.value = state
    }

}