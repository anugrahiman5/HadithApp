package com.nusa.hadithapp.ui.saved

import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.domain.model.HadithNote
import com.nusa.hadithapp.util.SortOrder

data class SavedUiState(
    val selectedTab: Int = 0, // 0 = Bookmark, 1 = Catatan
    val bookmarkFilterTag: String = "Semua Bookmark",
    val noteFilterTag: String = "Semua Catatan",
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.NEWEST,
    val bookmarks: List<Bookmark> = emptyList(),
    val notes: List<HadithNote> = emptyList(),
    val totalBookmarksCount: Int = 0,
    val totalNotesCount: Int = 0,
    val availableFolderTags: List<String> = emptyList(),
    val availableNoteCategories: List<String> = emptyList(),
    val isSearchBarVisible: Boolean = false
)