package com.nusa.hadithapp.domain.model

data class HadithSearchItem(
    val bookSlug: String,
    val bookName: String,
    val number: Int,
    val arabicText: String,
    val translation: String,
    val chapterName: String,
    val grade: String,
    val narrator: String
)

data class SearchResult(
    val total: Int,
    val resultsPerBook: Map<String, List<HadithSearchItem>>,
    val allNumbersPerBook: Map<String, List<Int>> = emptyMap()
)