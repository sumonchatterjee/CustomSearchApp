package com.livelike.trialapps.search.data.remote

import com.livelike.trialapps.search.data.SearchResult
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("customsearch/v1")
    suspend fun search(@Query("q") query: String,
                       @Query("cx") cx: String,
                       @Query("key") key: String): Response<SearchResult>
}


/*
object RetrofitInstance {
    private const val BASE_URL = "https://customsearch.googleapis.com"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}*/
