package com.livelike.trialapps.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.livelike.trialapps.search.SearchRepository

class SearchViewModelFactory(private val repository: SearchRepository,
                             private val cx: String,
                             private val key: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository, cx, key) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}