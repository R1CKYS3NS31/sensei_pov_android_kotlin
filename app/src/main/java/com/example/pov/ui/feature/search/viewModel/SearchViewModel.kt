package com.example.pov.ui.feature.search.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.data.data.model.pov.PoV
import com.example.data.data.repository.search.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

//    val searchResultUiState: StateFlow<SearchResultUiState> =
//        searchRepository.getSearchCount()
//            .flatMapLatest { totalCount ->
//                if (totalCount < SEARCH_MIN_FTS_ENTITY_COUNT) {
//                    flowOf(SearchResultUiState.EmptyQuery)
//                } else {
//                    searchQuery.flatMapLatest { query->
//                        if (query.length < SEARCH_QUERY_MIN_LENGTH){
//                            flowOf(SearchResultUiState.EmptyQuery)
//                        }else{
//
//                        }
//                    }
//                }
//            }

    companion object {
        private const val SEARCH_QUERY = "search_query"
        private const val SEARCH_MIN_FTS_ENTITY_COUNT = 1
        private const val SEARCH_QUERY_MIN_LENGTH = 2
    }
}

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState
    data object EmptyQuery : SearchResultUiState
    data object LoadFailed : SearchResultUiState
    data class Success(
        val povs: List<PoV> = emptyList()
    ) : SearchResultUiState

    data object SearchNotReady : SearchResultUiState
}