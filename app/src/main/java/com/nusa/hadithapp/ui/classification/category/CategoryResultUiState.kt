package com.nusa.hadithapp.ui.classification.category

import com.nusa.hadithapp.domain.model.HadithCategoryItem

sealed interface CategoryResultUiState {
    data object Loading: CategoryResultUiState
    data object Empty : CategoryResultUiState
    data class Success(
        val categoryName: String,
        val items: List<HadithCategoryItem>,
        val isLoadingMore: Boolean = false,
        val hasMore: Boolean = true
    ) : CategoryResultUiState
    data class Error(val message: String) : CategoryResultUiState
}