package com.livelike.trialapps.search.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchHistory: SearchHistory)

    @Query("SELECT * FROM search_history ORDER BY id DESC")
    fun getAllSearchHistory(): Flow<List<SearchHistory>>

    @Query("DELETE FROM search_history WHERE id NOT IN (SELECT id FROM search_history ORDER BY id DESC LIMIT 10)")
    suspend fun deleteOldest(): Unit

    @Query("SELECT * FROM search_history WHERE query = :query LIMIT 1")
    suspend fun getSearchHistoryByQuery(query: String): SearchHistory?

}