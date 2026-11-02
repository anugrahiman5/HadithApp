package com.nusa.hadithapp.ui.profile

import com.nusa.hadithapp.data.preferences.UserProfile
import com.nusa.hadithapp.domain.model.BookReadingProgress
import com.nusa.hadithapp.domain.model.HeatmapWeek

data class ProfileUiState(
    val userProfile: UserProfile = UserProfile(),
    val bookmarkCount: Int = 0,
    val noteCount: Int = 0,
    val totalReadHadithCount: Int = 0,
    val distinctBooksCount: Int = 0,
    val booksProgress: List<BookReadingProgress> = emptyList(),
    val studyHeatmap: List<HeatmapWeek> = emptyList(),
    val currentStreakDays: Int = 0,
    val totalRecentReadCount: Int = 0
)
