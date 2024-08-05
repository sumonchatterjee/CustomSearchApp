package com.livelike.trialapps.search.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livelike.trialapps.search.SearchRepository
import com.livelike.trialapps.search.data.SearchResult
import com.livelike.trialapps.search.db.SearchHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    @Named("cx")  private val cx: String,
    @Named("key") private val key: String
): ViewModel() {


    //using flow
     private val _searchResults = MutableStateFlow<SearchResult?>(null)
     val searchResults: StateFlow<SearchResult?> get() = _searchResults

    //state in to convert the cold flow into a hot one.repository.getSearchHistory(), this gives cold flow
    val searchHistory = repository.getSearchHistory().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun search(query: String) {
        viewModelScope.launch {
            try {
                val result = repository.search(query,cx,key)
                if (result.isSuccessful) {
                    result.body()?.let {
                        _searchResults.value = it

                        //check if the query already exists in db.if yes dont insert.we will convert this latter to LRU
                        if (!repository.isQueryExisting(query)){
                            repository.insertSearchHistory(SearchHistory(query = query))
                        }

                    }

                }else {
                    _searchResults.value = null
                }
            } catch (e: Exception) {
                // Handle error
                _searchResults.value = null
            }
        }
    }

}