package com.nusa.hadithapp.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.preferences.FontPreferencesRepository
import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.domain.model.HadithNote
import com.nusa.hadithapp.domain.repository.BookmarkRepository
import com.nusa.hadithapp.domain.use_case.DeleteNoteUseCase
import com.nusa.hadithapp.domain.use_case.GetAllNotesUseCase
import com.nusa.hadithapp.domain.use_case.GetBookmarksUseCase
import com.nusa.hadithapp.util.FontSettingsState
import com.nusa.hadithapp.util.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class FilterParams(
    val tab: Int = 0,
    val bookmarkTag: String = "Semua Bookmark",
    val noteTag: String = "Semua Catatan",
    val query: String = "",
    val sortOrder: SortOrder = SortOrder.NEWEST,
    val isSearchBarVisible: Boolean = false,
    val customFolders: List<String> = listOf("Materi Khutbah Jumat", "Hafalan Arbain", "Muamalah & Bisnis")
)

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val bookmarkRepository: BookmarkRepository,
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val fontPreferencesRepository: FontPreferencesRepository
) : ViewModel() {

    private val _filterParams = MutableStateFlow(FilterParams())
    private val _bookmarksFlow = getBookmarksUseCase()
    private val _notesFlow = getAllNotesUseCase()

    val fontSettings: StateFlow<FontSettingsState> = fontPreferencesRepository.fontSettingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FontSettingsState()
    )

    val uiState: StateFlow<SavedUiState> = combine(
        _filterParams,
        _bookmarksFlow,
        _notesFlow
    ) { params, bookmarks, domainNotes ->

        val notesMap = domainNotes.associateBy {
            "${it.bookSlug.lowercase()}_${it.number}"
        }

        val allFolders =
            listOf("Semua Bookmark") + (params.customFolders + bookmarks.flatMap { it.folderTags }).distinct().filter { it != "Semua Bookmark" }

        val allCategories = listOf("Semua Catatan") + domainNotes.map { it.categoryTag }.distinct().filter { it != "Semua Catatan" }

        val enrichedBookmarks = bookmarks.map { b ->
            val noteForThisHadith = notesMap[b.id]
            if (noteForThisHadith != null && noteForThisHadith.content.isNotBlank()) {
                b.copy(userNote = noteForThisHadith.content)
            } else b
        }

        val filteredBookmarks = if (params.bookmarkTag == "Semua Bookmark") {
            enrichedBookmarks
        } else {
            enrichedBookmarks.filter { bookmark ->
                bookmark.folderTags.any { it.equals(params.bookmarkTag, ignoreCase = true) }
            }
        }

        val filteredNotes = if (params.noteTag == "Semua Catatan") {
            domainNotes
        } else {
            domainNotes.filter { it.categoryTag.equals(params.noteTag, ignoreCase = true) }
        }

        // Filter Berdasarkan Kata Kunci Pencarian Lokal
        val finalBookmarks = if (params.query.isNotBlank()) {
            val q = params.query.lowercase().trim()
            filteredBookmarks.filter {
                it.translation.lowercase().contains(q) ||
                        it.bookName.lowercase().contains(q) ||
                        it.arabicText.contains(q) ||
                        it.number.toString() == q ||
                        it.sanadSnippet.lowercase().contains(q) ||
                        it.userNote.lowercase().contains(q)
            }
        } else {
            filteredBookmarks
        }

        val finalNotes = if (params.query.isNotBlank()) {
            val q = params.query.lowercase().trim()
            filteredNotes.filter {
                it.title.lowercase().contains(q) ||
                        it.content.lowercase().contains(q) ||
                        it.tags.any { tag -> tag.lowercase().contains(q) } ||
                        it.categoryTag.lowercase().contains(q) ||
                        it.arabicSnippet.contains(q)
            }
        } else {
            filteredNotes
        }

        // Pengurutan
        val sortedBookmarks = when (params.sortOrder) {
            SortOrder.NEWEST -> finalBookmarks.sortedByDescending { it.createdAt }
            SortOrder.OLDEST -> finalBookmarks.sortedBy { it.createdAt }
            SortOrder.NAME_TITLE -> finalBookmarks.sortedWith(
                compareBy<Bookmark> { it.bookName }.thenBy { it.number }
            )
            SortOrder.HADITH_NUMBER -> finalBookmarks.sortedBy { it.number }
        }

        val sortedNotes = when (params.sortOrder) {
            SortOrder.NEWEST -> finalNotes.sortedWith(
                compareByDescending<HadithNote> { it.isPinned }.thenByDescending { it.updatedAt }
            )
            SortOrder.OLDEST -> finalNotes.sortedWith(
                compareByDescending<HadithNote> { it.isPinned }.thenBy { it.updatedAt }
            )
            SortOrder.NAME_TITLE -> finalNotes.sortedWith(
                compareByDescending<HadithNote> { it.isPinned }.thenBy { it.title.lowercase() }
            )
            SortOrder.HADITH_NUMBER -> finalNotes.sortedWith(
                compareByDescending<HadithNote> { it.isPinned }.thenBy { it.number }
            )
        }

        SavedUiState(
            bookmarks = sortedBookmarks,
            notes = sortedNotes,
            selectedTab = params.tab,
            bookmarkFilterTag = params.bookmarkTag,
            noteFilterTag = params.noteTag,
            availableFolderTags = allFolders,
            availableNoteCategories = allCategories,
            searchQuery = params.query,
            isSearchBarVisible = params.isSearchBarVisible,
            sortOrder = params.sortOrder,
            totalBookmarksCount = bookmarks.size,
            totalNotesCount = domainNotes.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SavedUiState()
    )

    fun selectTab(index: Int) {
        _filterParams.update { it.copy(tab = index) }
    }

    fun selectFilterTag(tag: String) {
        _filterParams.update {
            if (it.tab == 0) it.copy(bookmarkTag = tag)
            else it.copy(noteTag = tag)
        }
    }

    fun selectTag(tag: String) = selectFilterTag(tag)

    fun updateSearchQuery(query: String) {
        _filterParams.update { it.copy(query = query) }
    }

    fun toggleSearchBar() {
        _filterParams.update {
            val nextVisible = !it.isSearchBarVisible
            it.copy(
                isSearchBarVisible = nextVisible,
                query = if (!nextVisible) "" else it.query
            )
        }
    }

    fun setSortOrder(order: SortOrder) {
        _filterParams.update { it.copy(sortOrder = order) }
    }

    fun createNewFolder(folderName: String) {
        val clean = folderName.trim()
        if (clean.isNotBlank()) {
            _filterParams.update {
                if (!it.customFolders.contains(clean)) {
                    it.copy(customFolders = it.customFolders + clean, bookmarkTag = clean)
                } else {
                    it.copy(bookmarkTag = clean)
                }
            }
        }
    }

    fun moveBookmarkToFolder(bookmark: Bookmark, newFolder: String) {
        viewModelScope.launch {
            bookmarkRepository.updateBookmarkFolders(bookmark.bookSlug, bookmark.number, listOf(newFolder))
        }
    }

    fun removeBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            bookmarkRepository.removeBookmark(bookmark.bookSlug, bookmark.number)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId)
        }
    }

    fun updateFontSettings(newSettings: FontSettingsState) {
        viewModelScope.launch {
            fontPreferencesRepository.saveFontSettings(newSettings)
        }
    }
}
