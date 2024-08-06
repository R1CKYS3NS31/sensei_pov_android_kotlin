package com.example.data.data.repository.search

import com.example.data.data.model.search.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun populateFtsData()
    fun search(searchQuery: String): Flow<SearchResult>
    fun getSearchCount(): Flow<Int>
}