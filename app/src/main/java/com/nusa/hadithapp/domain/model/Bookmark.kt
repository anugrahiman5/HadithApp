package com.nusa.hadithapp.domain.model

data class Bookmark(
    val id: String,
    val bookSlug: String,
    val number: Int,
    val bookName: String,
    val kitabTitle: String,
    val babTitle: String,
    val arabicText: String,
    val translation: String,
    val grade: String,
    val folderTags: List<String> = listOf("Semua Bookmark"),
    val sanadSnippet: String,
    val userNote: String,
    val createdAt: Long
)