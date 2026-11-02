package com.nusa.hadithapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.preferences.FontPreferencesRepository
import com.nusa.hadithapp.data.preferences.LastReadPreferencesRepository
import com.nusa.hadithapp.data.preferences.SettingsPreferencesRepository
import com.nusa.hadithapp.data.preferences.SettingsState
import com.nusa.hadithapp.data.session.PerawiSession
import com.nusa.hadithapp.data.session.PerawiSessionManager
import com.nusa.hadithapp.data.session.SearchSessionManager
import com.nusa.hadithapp.domain.model.AppError
import com.nusa.hadithapp.domain.model.BabItem
import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.model.KitabItem
import com.nusa.hadithapp.domain.repository.BookmarkRepository
import com.nusa.hadithapp.domain.repository.ReadingHistoryRepository
import com.nusa.hadithapp.domain.use_case.AskHadithAiUseCase
import com.nusa.hadithapp.domain.use_case.GetBabListUseCase
import com.nusa.hadithapp.domain.use_case.GetBookmarkUseCase
import com.nusa.hadithapp.domain.use_case.GetHadithDetailByCategoryUseCase
import com.nusa.hadithapp.domain.use_case.GetHadithDetailByThemeUseCase
import com.nusa.hadithapp.domain.use_case.GetHadithDetailUseCase
import com.nusa.hadithapp.domain.use_case.GetKitabListUseCase
import com.nusa.hadithapp.domain.use_case.GetNoteUseCase
import com.nusa.hadithapp.domain.use_case.GetPerawiProfileUseCase
import com.nusa.hadithapp.domain.use_case.GetScholarCommentUseCase
import com.nusa.hadithapp.domain.use_case.GetSimilarHadithUseCase
import com.nusa.hadithapp.domain.use_case.GetPerawiTotalHadithUseCase
import com.nusa.hadithapp.domain.use_case.IsBookmarkedUseCase
import com.nusa.hadithapp.domain.use_case.SaveNoteUseCase
import com.nusa.hadithapp.domain.use_case.ToggleBookmarkUseCase
import com.nusa.hadithapp.ui.home.LastReadHadith
import com.nusa.hadithapp.util.BookSlugUtil
import com.nusa.hadithapp.util.FontSettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HadithViewModel @Inject constructor(
    private val getHadithDetailUseCase: GetHadithDetailUseCase,
    private val getHadithDetailByThemeUseCase: GetHadithDetailByThemeUseCase,
    private val getHadithDetailByCategoryUseCase: GetHadithDetailByCategoryUseCase,
    private val getPerawiProfileUseCase: GetPerawiProfileUseCase,
    private val getScholarCommentUseCase: GetScholarCommentUseCase,
    private val getKitabListUseCase: GetKitabListUseCase,
    private val getBabListUseCase: GetBabListUseCase,
    private val getBookmarkUseCase: GetBookmarkUseCase,
    private val lastReadRepository: LastReadPreferencesRepository,
    private val readingHistoryRepository: ReadingHistoryRepository,
    private val fontRepository: FontPreferencesRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val searchSessionManager: SearchSessionManager,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val isBookmarkedUseCase: IsBookmarkedUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getSimilarHadithUseCase: GetSimilarHadithUseCase,
    private val getPerawiTotalHadithUseCase: GetPerawiTotalHadithUseCase,
    private val perawiSessionManager: PerawiSessionManager,
    private val askHadithAiUseCase: AskHadithAiUseCase,
    private val settingsPreferencesRepository: SettingsPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HadithUiState>(HadithUiState.Empty)
    private val _perawiProfileState = MutableStateFlow<PerawiUiState>(PerawiUiState.Empty)
    private val _scholarCommentState = MutableStateFlow<ScholarCommentUiState>(ScholarCommentUiState.Empty)
    private val _similarHadithState = MutableStateFlow<SimilarHadithUiState>(SimilarHadithUiState.Empty)
    private val _kitabListState = MutableStateFlow<List<KitabItem>>(emptyList())
    private val _babListState = MutableStateFlow<List<BabItem>>(emptyList())
    private val _searchCurrentIndex = MutableStateFlow(-1)
    private val _isFromSearch = MutableStateFlow(false)
    private val _isBookmarked = MutableStateFlow(false)
    private val _currentNote = MutableStateFlow("")
    private val _currentBookmarkFolders = MutableStateFlow<List<String>>(emptyList())
    private val _isPerawiMode = MutableStateFlow(false)
    private val _perawiSession = MutableStateFlow<PerawiSession?>(null)
    private val _aiResponseState = MutableStateFlow<AiResponseState>(AiResponseState.Idle)

    private var bookmarkObserveJob: kotlinx.coroutines.Job? = null

    val settingsState: StateFlow<SettingsState> = settingsPreferencesRepository.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsState()
        )

    val aiResponseState: StateFlow<AiResponseState> = _aiResponseState.asStateFlow()

    val fontSettings: StateFlow<FontSettingsState> = fontRepository.fontSettingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FontSettingsState()
        )

    val uiState: StateFlow<HadithUiState> = _uiState.asStateFlow()
    val perawiProfileState: StateFlow<PerawiUiState> = _perawiProfileState.asStateFlow()
    val scholarCommentState: StateFlow<ScholarCommentUiState> = _scholarCommentState.asStateFlow()
    val similarHadithState: StateFlow<SimilarHadithUiState> = _similarHadithState.asStateFlow()
    val kitabListState: StateFlow<List<KitabItem>> = _kitabListState.asStateFlow()
    val babListState: StateFlow<List<BabItem>> = _babListState.asStateFlow()
    val searchCurrentIndex: StateFlow<Int> = _searchCurrentIndex.asStateFlow()
    val isFromSearch: StateFlow<Boolean> = _isFromSearch.asStateFlow()
    val isBookmarked: StateFlow<Boolean> = _isBookmarked.asStateFlow()
    val currentNote: StateFlow<String> = _currentNote.asStateFlow()
    val currentBookmarkFolders: StateFlow<List<String>> = _currentBookmarkFolders.asStateFlow()
    val isPerawiMode: StateFlow<Boolean> = _isPerawiMode.asStateFlow()
    val perawiSession: StateFlow<PerawiSession?> = _perawiSession.asStateFlow()

    val availableFolders: StateFlow<List<String>> = bookmarkRepository.getAllFolders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf("Semua Bookmark")
        )

    fun loadHadith(bookSlug: String, number: Int) {
        viewModelScope.launch {
            _uiState.value = HadithUiState.Loading
            _similarHadithState.value = SimilarHadithUiState.Empty
            try {
                val result = getHadithDetailUseCase(bookSlug, number)
                _uiState.value = HadithUiState.Success(result)
                observeBookmarkFolders(bookSlug, result.number)

                if (_isPerawiMode.value) {
                    val session = _perawiSession.value
                    if (session != null) {
                        val idx = session.hadithNumbers.indexOf(result.number)
                        if (idx != -1 && idx != session.currentIndex) {
                            session.currentIndex = idx
                            _perawiSession.value = session.copy(currentIndex = idx)
                        }
                    }
                }

                lastReadRepository.saveLastRead(
                    LastReadHadith(
                        bookSlug = bookSlug,
                        bookName = BookSlugUtil.getOfficialBookName(bookSlug),
                        number = result.number,
                        title = result.chapter,
                        progressPercent = if (result.totalHadith > 0) result.number.toFloat() / result.totalHadith.toFloat() else 0f,
                        kitab = result.kitab
                    )
                )
                readingHistoryRepository.recordReading(result, bookSlug)
            } catch (e: Exception) {
                Log.e("HADITH_CRASH_LOG", "Deteksi Crash: ", e)
                val appError = AppError.fromThrowable(e)
                _uiState.value = HadithUiState.Error(appError.message)
            }
        }
    }

    fun loadHadithWithSearchIndex(bookSlug: String, number: Int, searchIndex: Int) {
        viewModelScope.launch {
            _isPerawiMode.value = false
            _perawiSession.value = null
            _uiState.value = HadithUiState.Loading
            _similarHadithState.value = SimilarHadithUiState.Empty
            try {
                val resolvedIndex = if (searchIndex != -1) {
                    searchIndex
                } else {
                    searchSessionManager.getIndex(bookSlug, number)
                }

                if (resolvedIndex != -1) {
                    _searchCurrentIndex.value = resolvedIndex
                    _isFromSearch.value = true
                } else {
                    _isFromSearch.value = false
                    _searchCurrentIndex.value = -1
                }

                val result = getHadithDetailUseCase(bookSlug, number)
                _uiState.value = HadithUiState.Success(result)
                observeBookmarkFolders(bookSlug, result.number)

                if (_isPerawiMode.value) {
                    val session = _perawiSession.value
                    if (session != null) {
                        val idx = session.hadithNumbers.indexOf(result.number)
                        if (idx != -1 && idx != session.currentIndex) {
                            session.currentIndex = idx
                            _perawiSession.value = session.copy(currentIndex = idx)
                        }
                    }
                }

                lastReadRepository.saveLastRead(
                    LastReadHadith(
                        bookSlug = bookSlug,
                        bookName = BookSlugUtil.getOfficialBookName(bookSlug),
                        number = result.number,
                        title = result.chapter,
                        progressPercent = if (result.totalHadith > 0) result.number.toFloat() / result.totalHadith.toFloat() else 0f,
                        kitab = result.kitab
                    )
                )
                readingHistoryRepository.recordReading(result, bookSlug)
            } catch (e: Exception) {
                Log.e("HADITH_CRASH_LOG", "Deteksi Crash: ", e)
                val appError = AppError.fromThrowable(e)
                _uiState.value = HadithUiState.Error(appError.message)
            }
        }
    }

    fun loadNextSearchItem() {
        if (!_isFromSearch.value) return
        val currentIndex = _searchCurrentIndex.value
        if (currentIndex < searchSessionManager.activeSearchItems.size - 1) {
            val nextIndex = currentIndex + 1
            val nextItem = searchSessionManager.getItemByIndex(nextIndex)
            if (nextItem != null) {
                loadHadithWithSearchIndex(nextItem.bookSlug, nextItem.number, nextIndex)
            }
        }
    }

    fun loadPreviousSearchItem() {
        if (!_isFromSearch.value) return
        val currentIndex = _searchCurrentIndex.value
        if (currentIndex > 0) {
            val prevIndex = currentIndex - 1
            val prevItem = searchSessionManager.getItemByIndex(prevIndex)
            if (prevItem != null) {
                loadHadithWithSearchIndex(prevItem.bookSlug, prevItem.number, prevIndex)
            }
        }
    }

    fun getSearchPositionText(): String {
        val currentIndex = _searchCurrentIndex.value
        val total = searchSessionManager.activeSearchItems.size
        return if (_isFromSearch.value && total > 0 && currentIndex != -1) {
            "${currentIndex + 1} dari $total hasil pencarian"
        } else {
            ""
        }
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

    fun loadHadithWithPerawiMode(
        bookSlug: String,
        number: Int,
        kodeRawi: Int,
        rawiName: String
    ) {
        viewModelScope.launch {
            _isPerawiMode.value = true
            _isFromSearch.value = false
            _searchCurrentIndex.value = -1

            var session = perawiSessionManager.getSession(bookSlug, kodeRawi)
            if (session == null) {
                try {
                    val numbers = getPerawiTotalHadithUseCase(bookSlug, kodeRawi)
                    perawiSessionManager.setSession(bookSlug, kodeRawi, rawiName, numbers, number)
                    session = perawiSessionManager.getSession(bookSlug, kodeRawi)
                } catch (e: Exception) {
                    Log.e("HadithViewModel", "Gagal memuat daftar hadits perawi", e)
                }
            } else {
                val idx = session.hadithNumbers.indexOf(number)
                if (idx != -1) {
                    session.currentIndex = idx
                }
            }
            _perawiSession.value = session?.copy()
            loadHadith(bookSlug, number)
        }
    }

    fun loadNextPerawiItem() {
        val session = _perawiSession.value ?: return
        val nextNumber = perawiSessionManager.getNextHadithNumber(session.bookSlug, session.kodeRawi)
        if (nextNumber != null) {
            _perawiSession.value = perawiSessionManager.getSession(session.bookSlug, session.kodeRawi)?.copy()
            loadHadith(session.bookSlug, nextNumber)
        }
    }

    fun loadPreviousPerawiItem() {
        val session = _perawiSession.value ?: return
        val prevNumber = perawiSessionManager.getPreviousHadithNumber(session.bookSlug, session.kodeRawi)
        if (prevNumber != null) {
            _perawiSession.value = perawiSessionManager.getSession(session.bookSlug, session.kodeRawi)?.copy()
            loadHadith(session.bookSlug, prevNumber)
        }
    }

    fun loadPerawiItemAt(index: Int) {
        val session = _perawiSession.value ?: return
        val targetNumber = perawiSessionManager.getHadithNumberAt(session.bookSlug, session.kodeRawi, index)
        if (targetNumber != null) {
            _perawiSession.value = perawiSessionManager.getSession(session.bookSlug, session.kodeRawi)?.copy()
            loadHadith(session.bookSlug, targetNumber)
        }
    }

    fun getPerawiPositionText(): String {
        val session = _perawiSession.value ?: return ""
        val currentPos = session.currentIndex + 1
        val total = session.hadithNumbers.size
        return "Hadits $currentPos dari $total riwayat ${session.rawiName}"
    }

    fun loadHadithByTheme(themeName: String, vMember: Int) {
        viewModelScope.launch {
            _isPerawiMode.value = false
            _perawiSession.value = null
            _uiState.value = HadithUiState.Loading
            _similarHadithState.value = SimilarHadithUiState.Empty
            try {
                val result = getHadithDetailByThemeUseCase(themeName, vMember)
                val bookSlug = result.bookName.lowercase().replace(" ", "")
                _uiState.value = HadithUiState.Success(result)
                observeBookmarkFolders(bookSlug, result.number)

                lastReadRepository.saveLastRead(
                    LastReadHadith(
                        bookSlug = bookSlug,
                        bookName = BookSlugUtil.getOfficialBookName(result.bookName),
                        number = result.number,
                        title = result.chapter,
                        progressPercent = if (result.totalHadith > 0) {
                            result.number.toFloat() / result.totalHadith.toFloat()
                        } else 0f,
                        kitab = result.kitab
                    )
                )
                readingHistoryRepository.recordReading(result, bookSlug)
            } catch (e: Exception) {
                Log.e("HADITH_CRASH_LOG", "Deteksi Crash: ", e)
                val appError = AppError.fromThrowable(e)
                _uiState.value = HadithUiState.Error(appError.message)
            }
        }
    }

    fun loadHadithByCategory(categoryName: String, vMember: Int) {
        viewModelScope.launch {
            _isPerawiMode.value = false
            _perawiSession.value = null
            _uiState.value = HadithUiState.Loading
            _similarHadithState.value = SimilarHadithUiState.Empty
            try {
                val result = getHadithDetailByCategoryUseCase(categoryName, vMember)
                val bookSlug = result.bookName.lowercase().replace(" ", "")
                _uiState.value = HadithUiState.Success(result)

                observeBookmarkFolders(bookSlug, result.number)

                lastReadRepository.saveLastRead(
                    LastReadHadith(
                        bookSlug = bookSlug,
                        bookName = BookSlugUtil.getOfficialBookName(result.bookName),
                        number = result.number,
                        title = result.chapter,
                        progressPercent = if (result.totalHadith > 0) {
                            result.number.toFloat() / result.totalHadith.toFloat()
                        } else 0f,
                        kitab = result.kitab
                    )
                )
                readingHistoryRepository.recordReading(result, bookSlug)
            } catch (e: Exception) {
                Log.e("HADITH_CRASH_LOG", "Deteksi Crash Kategori: ", e)
                val appError = AppError.fromThrowable(e)
                _uiState.value = HadithUiState.Error(appError.message)
            }
        }
    }

    fun loadSimilarHadith(bookSlug: String, number: Int) {
        viewModelScope.launch {
            _similarHadithState.value = SimilarHadithUiState.Loading
            try {
                val result = getSimilarHadithUseCase(bookSlug, number)
                _similarHadithState.value = SimilarHadithUiState.Success(result)
            } catch (e: Exception) {
                Log.e("SIMILAR_HADITH_LOG", "Gagal memuat hadits serupa: ", e)
                _similarHadithState.value = SimilarHadithUiState.Error(
                    e.localizedMessage ?: "Gagal memuat hadits serupa."
                )
            }
        }
    }

    fun loadPerawiProfile(kodeRawi: Int) {
        viewModelScope.launch {
            _perawiProfileState.value = PerawiUiState.Loading
            try {
                val profile = getPerawiProfileUseCase(kodeRawi)
                if (profile != null) {
                    _perawiProfileState.value = PerawiUiState.Success(profile)
                } else {
                    _perawiProfileState.value = PerawiUiState.Error("Data Profil tidak tersedia.")
                }
            } catch (e: Exception) {
                _perawiProfileState.value = PerawiUiState.Error(e.localizedMessage ?: "Gagal memuat profil.")
            }
        }
    }

    fun resetPerawiProfile() {
        _perawiProfileState.value = PerawiUiState.Empty
    }

    fun loadScholarComments(kodeRawi: Int) {
        viewModelScope.launch {
            _scholarCommentState.value = ScholarCommentUiState.Loading
            try {
                val comments = getScholarCommentUseCase(kodeRawi)
                _scholarCommentState.value = ScholarCommentUiState.Success(comments)
            } catch (e: Exception) {
                _scholarCommentState.value = ScholarCommentUiState.Error(
                    e.localizedMessage ?: "Gagal memuat komentar ulama"
                )
            }
        }
    }

    suspend fun loadPerawiHadithNumbers(bookSlug: String, kodeRawi: Int): List<Int> {
        return getPerawiTotalHadithUseCase(bookSlug, kodeRawi)
    }

    fun resetScholarCommentState() {
        _scholarCommentState.value = ScholarCommentUiState.Empty
    }

    fun loadAllBooks(bookSlug: String) {
        viewModelScope.launch {
            try {
                val result = getKitabListUseCase(bookSlug)
                _kitabListState.value = result
            } catch (e: Exception) {
                Log.e("HADITH_VIEW_MODEL", "Gagal memuat daftar kitab: ", e)
            }
        }
    }

    fun loadAllChapters(bookSlug: String, start: Int, end: Int) {
        viewModelScope.launch {
            try {
                val result = getBabListUseCase(bookSlug, start, end)
                _babListState.value = result
            } catch (e: Exception) {
                Log.e("HADITH_VIEW_MODEL", "Gagal memuat daftar bab: ", e)
            }
        }
    }

    fun updateFontSettings(newSettings: FontSettingsState) {
        viewModelScope.launch {
            fontRepository.saveFontSettings(newSettings)
        }
    }

    fun observeBookmarkAndNote(bookSlug: String, number: Int) {
        viewModelScope.launch {
            isBookmarkedUseCase(bookSlug, number).collect { bookmarked ->
                _isBookmarked.value = bookmarked
            }
        }
        viewModelScope.launch {
            getNoteUseCase(bookSlug, number).collect { note ->
                _currentNote.value = note?.content ?: ""
            }
        }
    }

    fun toggleBookmark(hadith: Hadith, bookSlug: String) {
        viewModelScope.launch {
            toggleBookmarkUseCase(hadith, bookSlug, _isBookmarked.value)
        }
    }

    @Deprecated("Gunakan toggleBookmark", ReplaceWith("toggleBookmark(hadith, bookSlug)"))
    fun toogleBookmark(hadith: Hadith, bookSlug: String) {
        toggleBookmark(hadith, bookSlug)
    }

    fun saveNote(bookSlug: String, number: Int, content: String) {
        viewModelScope.launch {
            saveNoteUseCase(bookSlug, number, content)
        }
    }

    fun observeBookmarkFolders(bookSlug: String, number: Int) {
        bookmarkObserveJob?.cancel()
        _isBookmarked.value = false
        _currentBookmarkFolders.value = emptyList()
        bookmarkObserveJob = viewModelScope.launch {
            getBookmarkUseCase(bookSlug, number).collect { bookmark ->
                _isBookmarked.value = bookmark != null
                _currentBookmarkFolders.value = bookmark?.folderTags ?: emptyList()
            }
        }
    }

    fun saveBookmarkToFolder(hadith: Hadith, bookSlug: String, folderTags: List<String>) {
        viewModelScope.launch {
            bookmarkRepository.addBookmark(hadith, bookSlug, folderTags)
            _isBookmarked.value = true
            _currentBookmarkFolders.value = folderTags
        }
    }

    fun removeBookmark(bookSlug: String, number: Int) {
        viewModelScope.launch {
            bookmarkRepository.removeBookmark(bookSlug, number)
            _isBookmarked.value = false
        }
    }

    fun askAi(hadith: Hadith, prompt: String) {
        val currentSettings = settingsState.value
        if (currentSettings.geminiApiKey.isBlank()) {
            _aiResponseState.value = AiResponseState.Error(
                message = "Kunci API Gemini belum diatur. Silakan atur di menu Pengaturan.",
                isApiKeyMissing = true
            )
            return
        }

        _aiResponseState.value = AiResponseState.Loading
        viewModelScope.launch {
            val result = askHadithAiUseCase(
                hadith = hadith,
                userQuery = prompt,
                apiKeyOverride = currentSettings.geminiApiKey,
                modelOverride = currentSettings.geminiModel
            )
            result.onSuccess { answer ->
                _aiResponseState.value = AiResponseState.Success(answer = answer, prompt = prompt)
            }.onFailure { error ->
                val isKeyIssue = error.message?.contains("Kunci API", ignoreCase = true) == true ||
                        error.message?.contains("API Key", ignoreCase = true) == true
                _aiResponseState.value = AiResponseState.Error(
                    message = error.localizedMessage ?: "Terjadi kesalahan saat memproses jawaban AI.",
                    isApiKeyMissing = isKeyIssue
                )
            }
        }
    }

    fun resetAiState() {
        _aiResponseState.value = AiResponseState.Idle
    }
}
