package com.nusa.hadithapp.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.preferences.SearchHistoryPreferencesRepository
import com.nusa.hadithapp.data.session.PerawiSessionManager
import com.nusa.hadithapp.data.session.SearchSessionManager
import com.nusa.hadithapp.domain.model.HadithGradeFilter
import com.nusa.hadithapp.domain.model.RawiSearchItem
import com.nusa.hadithapp.domain.use_case.GetHadithBatchDetailsUseCase
import com.nusa.hadithapp.domain.use_case.GetPerawiProfileUseCase
import com.nusa.hadithapp.domain.use_case.GetScholarCommentUseCase
import com.nusa.hadithapp.domain.use_case.GetPerawiTotalHadithUseCase
import com.nusa.hadithapp.domain.use_case.SearchHadithUseCase
import com.nusa.hadithapp.domain.use_case.SearchRawiUseCase
import com.nusa.hadithapp.ui.detail.PerawiUiState
import com.nusa.hadithapp.ui.detail.ScholarCommentUiState
import com.nusa.hadithapp.util.BookSlugUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuickJumpSuggestion(
    val bookSlug: String,
    val bookName: String,
    val hadithNumber: Int
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchHadithUseCase: SearchHadithUseCase,
    private val getHadithBatchDetailsUseCase: GetHadithBatchDetailsUseCase,
    private val searchRawiUseCase: SearchRawiUseCase,
    private val getPerawiProfileUseCase: GetPerawiProfileUseCase,
    private val getScholarCommentUseCase: GetScholarCommentUseCase,
    private val searchSessionManager: SearchSessionManager,
    private val getPerawiTotalHadithUseCase: GetPerawiTotalHadithUseCase,
    private val perawiSessionManager: PerawiSessionManager,
    private val searchHistoryRepo: SearchHistoryPreferencesRepository
) : ViewModel() {

    val recentSearches: StateFlow<List<String>> = searchHistoryRepo.recentSearchesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _quickJumpSuggestion = MutableStateFlow<QuickJumpSuggestion?>(null)
    val quickJumpSuggestion: StateFlow<QuickJumpSuggestion?> = _quickJumpSuggestion.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _rawiUiState = MutableStateFlow<RawiSearchUiState>(RawiSearchUiState.Idle)
    val rawiUiState: StateFlow<RawiSearchUiState> = _rawiUiState.asStateFlow()

    private val _rawiKuniyahQuery = MutableStateFlow("")
    val rawiKuniyahQuery: StateFlow<String> = _rawiKuniyahQuery.asStateFlow()

    private val _rawiClassifyQuery = MutableStateFlow("")
    val rawiClassifyQuery: StateFlow<String> = _rawiClassifyQuery.asStateFlow()

    private val _rawiLevelQuery = MutableStateFlow("")
    val rawiLevelQuery: StateFlow<String> = _rawiLevelQuery.asStateFlow()

    private val _selectedRawiForDetail = MutableStateFlow<RawiSearchItem?>(null)
    val selectedRawiForDetail: StateFlow<RawiSearchItem?> = _selectedRawiForDetail.asStateFlow()

    private val _perawiProfileState = MutableStateFlow<PerawiUiState>(PerawiUiState.Empty)
    val perawiProfileState: StateFlow<PerawiUiState> = _perawiProfileState.asStateFlow()

    private val _scholarCommentState = MutableStateFlow<ScholarCommentUiState>(ScholarCommentUiState.Empty)
    val scholarCommentState: StateFlow<ScholarCommentUiState> = _scholarCommentState.asStateFlow()

    private val _currentQuery = MutableStateFlow("")
    val currentQuery: StateFlow<String> = _currentQuery.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _selectedFilterBook = MutableStateFlow<String?>(null)
    val selectedFilterBook: StateFlow<String?> = _selectedFilterBook.asStateFlow()

    private val _selectedGradeFilter = MutableStateFlow(HadithGradeFilter.ALL)
    val selectedGradeFilter: StateFlow<HadithGradeFilter> = _selectedGradeFilter.asStateFlow()

    fun setGradeFilter(filter: HadithGradeFilter) {
        _selectedGradeFilter.value = filter
    }

    val targetSearchBooks: StateFlow<Set<String>> = searchSessionManager.selectedTargetBooksFlow

    private val allFoundNumbers = mutableMapOf<String, List<Int>>()
    private val loadedIndices = mutableMapOf<String, Int>()

    fun setTargetSearchBooks(books: Set<String>) {
        if (books.isNotEmpty()) {
            searchSessionManager.selectedTargetBooks = books
            if (_currentQuery.value.isNotBlank()) {
                search(_currentQuery.value)
            }
        }
    }

    fun setTab(index: Int) {
        _selectedTab.value = index
    }

    fun updateQuery(query: String) {
        _currentQuery.value = query
        _quickJumpSuggestion.value = parseQuickJump(query)
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
        }
    }

    fun removeRecentSearch(query: String) {
        viewModelScope.launch {
            searchHistoryRepo.removeSearchQuery(query)
        }
    }

    fun clearAllRecentSearches() {
        viewModelScope.launch {
            searchHistoryRepo.clearAll()
        }
    }

    private fun parseQuickJump(input: String): QuickJumpSuggestion? {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return null

        val regex = Regex("""^(.+?)\s+(\d+)$""")
        val match = regex.find(trimmed)
        if (match != null) {
            val bookPart = match.groupValues[1].trim()
            val numPart = match.groupValues[2].toIntOrNull() ?: return null
            if (numPart > 0 && BookSlugUtil.isRecognizedBook(bookPart)) {
                val slug = BookSlugUtil.normalize(bookPart)
                return QuickJumpSuggestion(
                    bookSlug = slug,
                    bookName = BookSlugUtil.getOfficialBookName(slug),
                    hadithNumber = numPart
                )
            }
        }
        return null
    }

    fun search(keyword: String) {
        if (keyword.isBlank()) return
        _currentQuery.value = keyword
        _quickJumpSuggestion.value = parseQuickJump(keyword)
        _uiState.value = SearchUiState.Loading

        viewModelScope.launch {
            searchHistoryRepo.addSearchQuery(keyword)
        }

        allFoundNumbers.clear()
        loadedIndices.clear()

        viewModelScope.launch {
            try {
                val books = targetSearchBooks.value.toList().ifEmpty { BookSlugUtil.getAllSlugsList() }

                searchHadithUseCase(keyword, books).collect { result ->
                    if (result.total > 0 && result.resultsPerBook.isNotEmpty()) {
                        result.allNumbersPerBook.forEach { (slug, numbers) ->
                            registerFoundNumbers(slug, numbers)
                        }
                        _uiState.value = SearchUiState.Success(
                            total = result.total,
                            resultsPerBook = result.resultsPerBook,
                            selectedBookSlug = _selectedFilterBook.value
                        )
                    } else if (result.total == 0) {
                        _uiState.value = SearchUiState.Empty
                    }
                }
            } catch (e: Exception) {
                Log.e("SEARCH_VM", "Error saat pencarian: ${e.message}", e)
                _uiState.value = SearchUiState.Error(
                    e.localizedMessage ?: "Terjadi kesalahan saat memuat data."
                )
            }
        }
    }

    private fun registerFoundNumbers(bookSlug: String, numbers: List<Int>) {
        if (!allFoundNumbers.containsKey(bookSlug)) {
            allFoundNumbers[bookSlug] = numbers
            loadedIndices[bookSlug] = minOf(numbers.size, 5)
        }
    }

    fun loadMoreForBook(bookSlug: String) {
        val allNumbers = allFoundNumbers[bookSlug] ?: return
        val currentIndex = loadedIndices[bookSlug] ?: return

        if (currentIndex >= allNumbers.size) return

        val nextNumbers = allNumbers.subList(currentIndex, minOf(currentIndex + 10, allNumbers.size))

        viewModelScope.launch {
            try {
                val moreItems = getHadithBatchDetailsUseCase(bookSlug, nextNumbers)
                loadedIndices[bookSlug] = currentIndex + nextNumbers.size

                val currentState = _uiState.value
                if (currentState is SearchUiState.Success) {
                    val updatedMap = currentState.resultsPerBook.toMutableMap()
                    val existingList = updatedMap[bookSlug] ?: emptyList()
                    updatedMap[bookSlug] = existingList + moreItems

                    _uiState.value = currentState.copy(resultsPerBook = updatedMap)
                }
            } catch (e: Exception) {
                Log.e("SEARCH_VM", "Gagal load more: ${e.message}", e)
            }
        }
    }

    fun hasMoreItems(bookSlug: String): Boolean {
        val total = allFoundNumbers[bookSlug]?.size ?: 0
        val loaded = loadedIndices[bookSlug] ?: 0
        return loaded < total
    }

    fun getTotalCountForBook(bookSlug: String): Int {
        return allFoundNumbers[bookSlug]?.size ?: 0
    }

    fun filterByBook(bookSlug: String?) {
        _selectedFilterBook.value = bookSlug
        val currentState = _uiState.value
        if (currentState is SearchUiState.Success) {
            _uiState.value = currentState.copy(selectedBookSlug = bookSlug)
        }
    }

    fun getSearchItemIndex(bookSlug: String, number: Int): Int {
        return searchSessionManager.getIndex(bookSlug, number)
    }

    fun updateRawiFilters(kuniyah: String, classify: String, level: String) {
        _rawiKuniyahQuery.value = kuniyah
        _rawiClassifyQuery.value = classify
        _rawiLevelQuery.value = level
    }

    fun searchRawi(nameQuery: String = currentQuery.value) {
        val hasQuery = nameQuery.isNotBlank() ||
                _rawiKuniyahQuery.value.isNotBlank() ||
                _rawiClassifyQuery.value.isNotBlank() ||
                _rawiLevelQuery.value.isNotBlank()

        if (!hasQuery) return

        _rawiUiState.value = RawiSearchUiState.Loading

        viewModelScope.launch {
            try {
                val results = searchRawiUseCase(
                    name = nameQuery,
                    kuniyah = _rawiKuniyahQuery.value,
                    classify = _rawiClassifyQuery.value,
                    level = _rawiLevelQuery.value
                )

                if (results.isEmpty()) {
                    _rawiUiState.value = RawiSearchUiState.Empty
                } else {
                    _rawiUiState.value = RawiSearchUiState.Success(results)
                }
            } catch (e: Exception) {
                Log.e("SEARCH_RAWI_VM", "Gagal mencari perawi: ${e.message}", e)
                _rawiUiState.value = RawiSearchUiState.Error(
                    e.localizedMessage ?: "Terjadi kesalahan saat memuat data perawi."
                )
            }
        }
    }

    fun showPerawiDetail(rawi: RawiSearchItem) {
        _selectedRawiForDetail.value = rawi
        viewModelScope.launch {
            _perawiProfileState.value = PerawiUiState.Loading
            _scholarCommentState.value = ScholarCommentUiState.Loading

            try {
                val profile = getPerawiProfileUseCase(rawi.kodeRawi)
                if (profile != null) {
                    _perawiProfileState.value = PerawiUiState.Success(profile)
                } else {
                    _perawiProfileState.value = PerawiUiState.Error("Profil perawi tidak ditemukan")
                }

                val comments = getScholarCommentUseCase(rawi.kodeRawi)
                if (comments.isNotEmpty()) {
                    _scholarCommentState.value = ScholarCommentUiState.Success(comments)
                } else {
                    _scholarCommentState.value = ScholarCommentUiState.Empty
                }
            } catch (e: Exception) {
                _perawiProfileState.value = PerawiUiState.Error(e.message ?: "Gagal memuat profil")
                _scholarCommentState.value = ScholarCommentUiState.Error(e.message ?: "Gagal memuat komentar")
            }
        }
    }

    suspend fun loadPerawiHadithNumbers(bookSlug: String, kodeRawi: Int): List<Int> {
        return getPerawiTotalHadithUseCase(bookSlug, kodeRawi)
    }

    fun savePerawiSession(
        bookSlug: String,
        kodeRawi: Int,
        rawiName: String,
        hadithNumbers: List<Int>,
        currentHadithNumber: Int
    ) {
        perawiSessionManager.setSession(
            bookSlug = bookSlug,
            kodeRawi = kodeRawi,
            rawiName = rawiName,
            hadithNumbers = hadithNumbers,
            currentHadithNumber = currentHadithNumber
        )
    }

    fun dismissPerawiDetail() {
        _selectedRawiForDetail.value = null
        _perawiProfileState.value = PerawiUiState.Empty
        _scholarCommentState.value = ScholarCommentUiState.Empty
    }
}
