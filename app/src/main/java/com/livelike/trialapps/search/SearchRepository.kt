package com.livelike.trialapps.search

import com.livelike.trialapps.search.data.SearchResult
import com.livelike.trialapps.search.data.remote.ApiService
import com.livelike.trialapps.search.db.SearchHistory
import com.livelike.trialapps.search.db.SearchHistoryDao
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class SearchRepository(
    private val apiService: ApiService,
    private val searchHistoryDao: SearchHistoryDao
) {

    suspend fun search(query: String, cx: String, key: String): Response<SearchResult> = apiService.search(query,cx,key)


    fun getSearchHistory(): Flow<List<SearchHistory>> = searchHistoryDao.getAllSearchHistory()

    suspend fun insertSearchHistory(searchHistory: SearchHistory) {
        searchHistoryDao.insert(searchHistory)
    }

    suspend fun isQueryExisting(query: String): Boolean {
        return searchHistoryDao.getSearchHistoryByQuery(query) != null
    }

    suspend fun deleteOldSearches() {
        searchHistoryDao.deleteOldest()
    }

}