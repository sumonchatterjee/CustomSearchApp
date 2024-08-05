package com.livelike.trialapps.search.data

data class SearchResult(val items: List<ResultItem>,val searchInformation:SearchInformation)

data class ResultItem(val title: String, val link: String, val snippet: String, val displayLink:String)

data class SearchInformation(val totalResults:String,val formattedTotalResults:String,val formattedSearchTime:String)
