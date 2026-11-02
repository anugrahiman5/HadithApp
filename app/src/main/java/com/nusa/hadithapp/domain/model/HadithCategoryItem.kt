package com.nusa.hadithapp.domain.model

data class HadithCategoryItem(
    val bookSlug: String,
    val number: Int,
    val arabicText: String,
    val translation: String,
    val bookName: String,
    val chapterName: String,
    val totalCount: Int,
    val vMember: Int
)