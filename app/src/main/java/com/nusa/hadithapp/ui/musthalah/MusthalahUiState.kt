package com.nusa.hadithapp.ui.musthalah

import com.nusa.hadithapp.domain.model.musthalah.MusthalahCategory
import com.nusa.hadithapp.domain.model.musthalah.MusthalahTerm

data class MusthalahUiState(
    val searchQuery: String = "",
    val selectedCategory: MusthalahCategory = MusthalahCategory.ALL,
    val terms: List<MusthalahTerm> = emptyList(),
    val totalCount: Int = 0,
    val selectedTermForDetail: MusthalahTerm? = null
)
