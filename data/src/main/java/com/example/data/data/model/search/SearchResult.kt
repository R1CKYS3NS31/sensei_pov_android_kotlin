package com.example.data.data.model.search

import com.example.data.data.model.pov.PoV

data class SearchResult(
    val povs: List<PoV> = emptyList()
)