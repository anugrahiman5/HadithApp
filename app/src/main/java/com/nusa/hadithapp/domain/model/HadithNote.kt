package com.nusa.hadithapp.domain.model

data class HadithNote(
    val id: String,
    val bookSlug: String,
    val number: Int,
    val bookName: String = "",
    val title: String = "",
    val content: String,
    val categoryTag: String = "Kajian Umum",
    val tags: List<String> = emptyList(),
    val arabicSnippet: String = "",
    val translationSnippet: String = "",
    val firstSanad: String = "",
    val references: List<ReferencedHadith> = emptyList(),
    val isPinned: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
