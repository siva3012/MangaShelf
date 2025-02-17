package com.example.managshelf.ui.uiScreens.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.managshelf.data.database.MangaDatabase
import com.example.managshelf.data.database.MangaEntity
import com.example.managshelf.data.network.MangaApi
import com.example.managshelf.data.network.RetrofitInstance
import com.example.managshelf.data.repository.MangaRepositoryImpl
import com.example.managshelf.domain.FetchMangaDataUseCase
import com.example.managshelf.domain.LoadData
import com.example.managshelf.domain.NetworkStateUseCase
import com.example.managshelf.domain.UpdateManga
import com.example.managshelf.ui.common.SortOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MangaViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val mDataBase = MangaDatabase.getDatabase(context)
    private val apiService : MangaApi by lazy { RetrofitInstance.api }
    private val mMangaRepositoryImpl : MangaRepositoryImpl = MangaRepositoryImpl(apiService,mDataBase,context)
    private var _dataList = MutableStateFlow<List<MangaEntity>>(emptyList())
    var dataList: StateFlow<List<MangaEntity>> =_dataList
    var offset = mutableStateOf(IntOffset(0,0))
    private val _selectedManga = MutableStateFlow<MangaEntity?>(null)
    val selectedManga : StateFlow<MangaEntity?> = _selectedManga
     var selectOption = SortOption.YEAR_ASC
    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading : StateFlow<Boolean> = _isLoading
    var internetState = mutableStateOf(true)
    val showDialog = MutableStateFlow<Boolean>(false)

    init {
        loadData()
        getData()
        networkUpdated()
    }

   private  fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            LoadData(mMangaRepositoryImpl).execute()
        }
    }

    private fun getData(){
        viewModelScope.launch(Dispatchers.IO) {
           FetchMangaDataUseCase(mMangaRepositoryImpl).execute().collect{
               Log.d("data from gatData",it.toString())
               if (it.isEmpty()){
                   val dummyList = List(2) {
                       MangaEntity(
                           id = it.toString(), category = "", title = "Loading...", score = 0.0,
                           popularity = 0, year = 0, imageUrl = "", fav = false, read = false
                       )
                   }
                   _isLoading.value = true
                   _dataList.value = dummyList
                   setSelectedOption(SortOption.YEAR_ASC)
               }else {
                   _isLoading.value = false
                   _dataList.value = it
                   setSelectedOption(SortOption.YEAR_ASC)
               }
           }
        }
    }

    fun updateManga(mangaEntity: MangaEntity){
        Log.d("Fav in view","Click")
        _selectedManga.value=mangaEntity
        viewModelScope.launch(Dispatchers.IO) {
            UpdateManga(mMangaRepositoryImpl).execute(mangaEntity)
        }
    }

    fun selectManga(manga: MangaEntity) {
        _selectedManga.value = manga
    }
    fun setSelectedOption(option: SortOption) {
        selectOption = option
       _dataList.value = when (option) {
            SortOption.SCORE_ASC -> _dataList.value.sortedBy { it.score }
            SortOption.SCORE_DESC -> _dataList.value.sortedByDescending { it.score }
            SortOption.POPULARITY_ASC -> _dataList.value.sortedBy { it.popularity }
            SortOption.POPULARITY_DESC -> _dataList.value.sortedByDescending { it.popularity }
            SortOption.YEAR_ASC -> _dataList.value.sortedBy { it.year }
        }
    }

    fun updateMangaDetails(manga: MangaEntity) {
        val currentList = dataList.value

        val updatedList = currentList.toMutableList()

        val indexToUpdate =
            updatedList.indexOfFirst { it.id == manga.id }

        if (indexToUpdate != -1) {
            updatedList[indexToUpdate] = manga
            _dataList.value=updatedList
        }
    }

    private fun networkUpdated(){
        viewModelScope.launch(Dispatchers.IO) {
            NetworkStateUseCase(mMangaRepositoryImpl).execute().collect{
                Log.d("Network","$it ${isLoading.value}")
                if (it==true){
                    showDialog.value=false
                    internetState.value=true
                }
                if (isLoading.value && it==true){
                    loadData()
                }else if(isLoading.value && it == false){
                    val list = mDataBase.mangaDao().getAllData()
                    if (list.isEmpty()){
                        showDialog.value=true
                    }
                }else if (it == false){
                    internetState.value = false
                }
            }
        }
    }

    fun unregisterReceiver(){
        mMangaRepositoryImpl.unregisterReceiver()
    }

    override fun onCleared() {
        super.onCleared()
      unregisterReceiver()
    }
}