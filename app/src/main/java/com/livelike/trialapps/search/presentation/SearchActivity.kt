package com.livelike.trialapps.search.presentation

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.livelike.trialapps.search.SearchRepository
import com.livelike.trialapps.search.data.SearchResult
import com.livelike.trialapps.search.db.SearchHistory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity:ComponentActivity() {

  /*  @Inject
    lateinit var viewModel: SearchViewModel  //field injection*/ //as per android documentation this does not work, hence you will have to create viewmodel
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchApp()
        }
    }

    @Composable
    fun getSearchViewModel(repository: SearchRepository, cx: String, key: String): SearchViewModel {
        val factory = SearchViewModelFactory(repository, cx, key)
        return ViewModelProvider(LocalContext.current as ViewModelStoreOwner, factory)[SearchViewModel::class.java]
    }



    @Composable
    fun SearchApp() {
        //all this are available via hilt
       /* val context = LocalContext.current
        val application = context.applicationContext as Application
        val repository = SearchRepository(
            RetrofitInstance.api,
            AppDatabse.getDatabase(application).searchHistoryDao()
        )
        val viewModel: SearchViewModel = getSearchViewModel(repository, cx, key)

*/
        val searchResults by viewModel.searchResults.collectAsStateWithLifecycle() //collects the value
        val searchHistory by viewModel.searchHistory.collectAsStateWithLifecycle()


        Column {
            SearchBar(
                onSearch = { query ->
                    viewModel.search(query)
                },
                searchHistory = searchHistory,
                onHistoryItemClicked = { query ->
                    viewModel.search(query)
                }
            )

            searchResults?.let {
                // Display search results
                SearchResultList(it)
            }
        }
    }


    @Composable
    fun SearchBar(
        onSearch: (String) -> Unit,
        searchHistory: List<SearchHistory>,
        onHistoryItemClicked: (String) -> Unit
    ) {
        var query by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        var showHistory by remember { mutableStateOf(false) }

        Column {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    showHistory = query.isEmpty() // Show history only when query is empty
                                },
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        showHistory = it.isFocused // Show history when search bar is focused
                    },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch(query)
                        focusRequester.freeFocus()
                        showHistory = false //dont show hisory when search is processed
                    }
                )
            )

            if(showHistory){
                LazyColumn {
                    items(searchHistory) { history ->
                        Text(
                            text = history.query,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onHistoryItemClicked(history.query)
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }

        }
    }


    @Composable
    fun SearchResultList(searchResults: SearchResult?) {
        if(searchResults?.searchInformation?.totalResults?.toLong() == 0L){
            Text("No results found.",
                modifier = Modifier.padding(10.dp),
                fontSize = 16.sp,
                textAlign = TextAlign.Center)
        }else{
            LazyColumn {
                items(searchResults?.items ?: emptyList()) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(text = item.title)
                        Text(text = item.snippet)
                        Text(text = item.link, color = Color.Blue)
                    }
                }
            }
        }
    }
}