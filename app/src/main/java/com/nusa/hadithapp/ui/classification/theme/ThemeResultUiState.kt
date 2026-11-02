package com.nusa.hadithapp.ui.classification.theme

import com.nusa.hadithapp.domain.model.HadithThemeItem

sealed interface ThemeResultUiState {
    data object Loading : ThemeResultUiState
    data class Success(
        val themeName: String,
        val items: List<HadithThemeItem>,
        val isLoadingMore: Boolean = false,
        val hasMore: Boolean = true
    ): ThemeResultUiState
    data class Error(val message: String) : ThemeResultUiState
    data object Empty : ThemeResultUiState
}