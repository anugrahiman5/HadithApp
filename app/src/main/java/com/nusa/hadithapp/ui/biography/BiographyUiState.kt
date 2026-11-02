package com.nusa.hadithapp.ui.biography

import com.nusa.hadithapp.domain.model.biography.BiographyTab
import com.nusa.hadithapp.domain.model.biography.ImamBiography
import com.nusa.hadithapp.domain.model.biography.ImamSummary

data class BiographyUiState(
    val selectedImamId: String = "Bukhari",
    val allImams: List<ImamSummary> = emptyList(),
    val currentBiography: ImamBiography? = null,
    val selectedTab: BiographyTab = BiographyTab.SUMMARY,
    val isLoading: Boolean = false,
    val isApiEnriched: Boolean = false
)
