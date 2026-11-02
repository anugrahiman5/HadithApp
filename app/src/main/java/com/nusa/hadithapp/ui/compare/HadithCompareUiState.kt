package com.nusa.hadithapp.ui.compare

import com.nusa.hadithapp.domain.model.compare.ComparedHadith
import com.nusa.hadithapp.domain.model.compare.MatanMatrixRow
import com.nusa.hadithapp.domain.model.compare.ReferenceHadith

data class HadithCompareUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val referenceHadith: ReferenceHadith? = null,
    val allComparedHadiths: List<ComparedHadith> = emptyList(),
    val displayedHadiths: List<ComparedHadith> = emptyList(),
    val selectedBookFilter: String? = null,
    val bookCounts: Map<String, Int> = emptyMap(),
    val isHighlightEnabled: Boolean = true,
    val takhrijNotes: String = "",
    val matanMatrix: List<MatanMatrixRow> = emptyList(),
    val totalJalurCount: Int = 0,
    val isLoadingRemaining: Boolean = false,
    val remainingToLoadCount: Int = 0
)
