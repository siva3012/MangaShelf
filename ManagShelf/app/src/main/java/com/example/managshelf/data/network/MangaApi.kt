package com.example.managshelf.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MangaApi {
    @GET("KEJO")
   suspend fun getMangaListFromNet(): List<MangaDataNet>
}

object RetrofitInstance {
    val api: MangaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.jsonkeeper.com/b/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MangaApi::class.java)
    }
}