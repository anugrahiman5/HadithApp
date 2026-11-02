package com.nusa.hadithapp.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.preferences.LastReadPreferencesRepository
import com.nusa.hadithapp.domain.model.HadithBook
import com.nusa.hadithapp.domain.use_case.GetKitabListUseCase
import com.nusa.hadithapp.ui.home.LastReadHadith
import com.nusa.hadithapp.util.ArabicChapterHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChapterItemUiModel(
    val chapterNumber: Int,
    val indonesianTitle: String,
    val arabicTitle: String,
    val startHadithNumber: Int,
    val endHadithNumber: Int,
    val totalHadithInChapter: Int,
    val isCurrentlyReading: Boolean = false,
    val currentReadingNumber: Int = 0,
    val progressPercent: Float = 0f
)

data class ChapterBrowserUiState(
    val selectedBook: HadithBook = HadithBook.BUKHARI,
    val chapters: List<ChapterItemUiModel> = emptyList(),
    val filteredChapters: List<ChapterItemUiModel> = emptyList(),
    val searchQuery: String = "",
    val activeFilterChip: String = "Semua Bab",
    val lastRead: LastReadHadith? = null,
    val isLoading: Boolean = false
)

// 🟢 Satukan parameter filter & loading agar combine hanya menerima 3 Flow
private data class BrowserParams(
    val selectedBook: HadithBook = HadithBook.BUKHARI,
    val query: String = "",
    val activeFilterChip: String = "Semua Bab",
    val isLoading: Boolean = false
)

@HiltViewModel
class ChapterBrowserViewModel @Inject constructor(
    private val getKitabListUseCase: GetKitabListUseCase,
    private val lastReadRepository: LastReadPreferencesRepository
) : ViewModel() {

    private val _params = MutableStateFlow(BrowserParams())
    private val _rawChapters = MutableStateFlow<List<ChapterItemUiModel>>(emptyList())

    val lastReadFlow: StateFlow<LastReadHadith?> = lastReadRepository.lastReadFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // 🟢 Sekarang hanya ada 3 Flow! 100% aman dan bebas error
    val uiState: StateFlow<ChapterBrowserUiState> = combine(
        _params,
        _rawChapters,
        lastReadFlow
    ) { params, chapters, lastRead ->

        val syncedChapters = chapters.map { item ->
            val isCurrentBook = lastRead != null && lastRead.bookSlug.equals(params.selectedBook.slug, ignoreCase = true)
            val isMatchingChapter = isCurrentBook && lastRead!!.number in item.startHadithNumber..item.endHadithNumber

            if (isMatchingChapter) {
                val readOffset = lastRead!!.number - item.startHadithNumber + 1
                val percent = if (item.totalHadithInChapter > 0) readOffset.toFloat() / item.totalHadithInChapter.toFloat() else 0f
                item.copy(
                    isCurrentlyReading = true,
                    currentReadingNumber = lastRead.number,
                    progressPercent = percent.coerceIn(0f, 1f)
                )
            } else {
                item.copy(isCurrentlyReading = false)
            }
        }

        val filtered = if (params.query.isNotBlank()) {
            val q = params.query.lowercase().trim()
            syncedChapters.filter {
                it.indonesianTitle.lowercase().contains(q) ||
                        it.arabicTitle.contains(q) ||
                        it.chapterNumber.toString() == q ||
                        q.toIntOrNull()?.let { num -> num in it.startHadithNumber..it.endHadithNumber } == true
            }
        } else {
            syncedChapters
        }

        ChapterBrowserUiState(
            selectedBook = params.selectedBook,
            chapters = syncedChapters,
            filteredChapters = filtered,
            searchQuery = params.query,
            activeFilterChip = params.activeFilterChip,
            lastRead = lastRead,
            isLoading = params.isLoading
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChapterBrowserUiState())

    init {
        loadChaptersForBook(HadithBook.BUKHARI)
    }

    fun selectBook(book: HadithBook) {
        if (_params.value.selectedBook != book) {
            _params.update { it.copy(selectedBook = book, query = "") }
            loadChaptersForBook(book)
        }
    }

    fun updateSearchQuery(query: String) {
        _params.update { it.copy(query = query) }
    }

    fun selectFilterChip(chip: String) {
        _params.update { it.copy(activeFilterChip = chip) }
    }

    private fun loadChaptersForBook(book: HadithBook) {
        viewModelScope.launch {
            _params.update { it.copy(isLoading = true) }
            try {
                val apiKitabList = getKitabListUseCase(book.slug)

                val processedItems = apiKitabList.mapIndexed { index, current ->
                    val startNum = current.awalan?.toIntOrNull() ?: 1
                    val nextItem = apiKitabList.getOrNull(index + 1)
                    val nextStart = nextItem?.awalan?.toIntOrNull()
                    val endNum = if (nextStart != null && nextStart > startNum) nextStart - 1 else book.totalHadith
                    val count = (endNum - startNum + 1).coerceAtLeast(1)

                    val indoName = current.nKitab?.ifBlank { "Bab ${index + 1}" } ?: "Bab ${index + 1}"
                    val arabicName = ArabicChapterHelper.resolveArabicTitle(
                        bookSlug = book.slug,
                        indonesianTitle = indoName,
                        chapterIndex = index + 1
                    )

                    ChapterItemUiModel(
                        chapterNumber = index + 1,
                        indonesianTitle = indoName,
                        arabicTitle = arabicName,
                        startHadithNumber = startNum,
                        endHadithNumber = endNum,
                        totalHadithInChapter = count
                    )
                }

                _rawChapters.value = processedItems
            } catch (e: Exception) {
                _rawChapters.value = emptyList()
            } finally {
                _params.update { it.copy(isLoading = false) }
            }
        }
    }
}