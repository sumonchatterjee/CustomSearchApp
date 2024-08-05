package com.livelike.trialapps.search.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SearchHistory::class], version = 1)
abstract class AppDatabse: RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile private var instance: AppDatabse? = null

        fun getDatabase(context: Context): AppDatabse =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabse::class.java, "app_database"
                ).build().also { instance = it }
            }
    }
}