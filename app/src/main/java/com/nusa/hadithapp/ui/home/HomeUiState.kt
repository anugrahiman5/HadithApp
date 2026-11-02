package com.nusa.hadithapp.ui.home

import com.nusa.hadithapp.domain.model.HadithBook

data class LastReadHadith(
    val bookSlug: String,
    val bookName: String,
    val number: Int,
    val title: String,
    val progressPercent: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val kitab: String = ""
)

data class HadithOfTheDay(
    val bookSlug: String,
    val bookName: String,
    val number: Int,
    val arabicText: String,
    val translation: String,
    val grade: String = "Shahih"
)

data class HomeUiState(
    val userName: String = "Thalibul Ilmi",
    val hijriDate: String = "29 Rabiul Awal 1448 H",
    val lastRead: LastReadHadith? = null,
    val hadithOfTheDay: HadithOfTheDay? = null,
    val books: List<HadithBook> = HadithBook.entries,
    val isLoading: Boolean = false,
    val isDailyHadithLoading: Boolean = false
)
