package com.nusa.hadithapp.ui.search

import com.nusa.hadithapp.domain.model.HadithSearchItem

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Success(
        val total: Int,
        val resultsPerBook: Map<String, List<HadithSearchItem>>,
        val selectedBookSlug: String? = null
    ) : SearchUiState
    data class Error(val message: String) : SearchUiState
    data object Empty : SearchUiState
}