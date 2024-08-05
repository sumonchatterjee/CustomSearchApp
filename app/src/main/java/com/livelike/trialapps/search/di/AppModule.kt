package com.livelike.trialapps.search.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.livelike.trialapps.search.SearchRepository
import com.livelike.trialapps.search.data.remote.ApiService
import com.livelike.trialapps.search.db.AppDatabse
import com.livelike.trialapps.search.db.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://customsearch.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }



    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabse {
        return AppDatabse.getDatabase(context)
    }



    @Provides
    fun provideSearchHistoryDao(database: AppDatabse): SearchHistoryDao {
        return database.searchHistoryDao()
    }


    @Provides
    @Singleton
    fun provideRepository(apiService: ApiService,searchHistoryDAO: SearchHistoryDao): SearchRepository {
        return SearchRepository(apiService,searchHistoryDAO)
    }

    @Provides
    @Singleton
    @Named("cx")
    fun provideCX() = "020a39a0d94d24cb8"

    @Provides
    @Singleton
    @Named("key")
    fun provideKey() = "AIzaSyDuzH8CiNZ4R5XuEeZRjWZay4I84A1wvG0"
}