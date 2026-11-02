package com.nusa.hadithapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.preferences.LastReadPreferencesRepository
import com.nusa.hadithapp.data.preferences.UserProfilePreferencesRepository
import com.nusa.hadithapp.data.session.SearchSessionManager
import com.nusa.hadithapp.domain.model.HadithBook
import com.nusa.hadithapp.domain.repository.HadithRepository
import com.nusa.hadithapp.util.HijriDateUtil
import com.nusa.hadithapp.util.extractArabicMatan
import com.nusa.hadithapp.util.extractMatanOnly
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val lastReadRepository: LastReadPreferencesRepository,
    private val hadithRepository: HadithRepository,
    private val searchSessionManager: SearchSessionManager,
    private val userProfileRepo: UserProfilePreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val targetSearchBooks: StateFlow<Set<String>> = searchSessionManager.selectedTargetBooksFlow

    fun setTargetSearchBooks(books: Set<String>) {
        searchSessionManager.selectedTargetBooks = books
    }

    init {
        loadHomeData()
        observeLastRead()
        observeUserProfile()
    }

    /**
     * Inisialisasi data beranda: Tanggal Hijriah dan Hadits Hari Ini
     */
    private fun loadHomeData() {
        val realTimeHijri = HijriDateUtil.getCurrentHijriDate()
        val defaultDailyHadith = DailyHadithProvider.getDailyHadith()
        _uiState.update {
            it.copy(
                hijriDate = realTimeHijri,
                hadithOfTheDay = defaultDailyHadith
            )
        }
        refreshDailyHadith()
    }

    /**
     * Mengambil hadits Shahih secara dinamis dari SELURUH kitab yang tersedia (Kutubut Tis'ah & Muktabar).
     */
    fun refreshDailyHadith() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isDailyHadithLoading = true) }
            val dynamicHadith = fetchRandomShahihHadithFromAllBooks()
            _uiState.update {
                it.copy(
                    hadithOfTheDay = dynamicHadith,
                    isDailyHadithLoading = false
                )
            }
        }
    }

    private suspend fun fetchRandomShahihHadithFromAllBooks(): HadithOfTheDay {
        // Acak urutan seluruh kitab
        val candidateBooks = HadithBook.entries.shuffled()

        for (book in candidateBooks) {
            val maxNumber = maxOf(1, book.totalHadith)
            // Coba ambil nomor acak
            val randomNum = (1..maxNumber).random()
            try {
                val hadith = hadithRepository.getHadithByBook(book.slug, randomNum)
                val cleanGrade = hadith.grade.trim()

                // Verifikasi keshahihan hadis dari kitab tersebut
                val isShahih = cleanGrade.contains("Shahih", ignoreCase = true) ||
                        book == HadithBook.BUKHARI ||
                        book == HadithBook.MUSLIM ||
                        book == HadithBook.IBNU_KHUZAIMAH ||
                        book == HadithBook.IBNU_HIBBAN ||
                        book == HadithBook.MALIK ||
                        hadith.researcherList.any { it.grade.contains("Shahih", ignoreCase = true) }

                // Pastikan teks Arab dan terjemahan valid dan tidak kosong
                if (isShahih && hadith.arabicText.isNotBlank() &&
                    hadith.translation.isNotBlank() &&
                    !hadith.translation.contains("Terjemahan belum tersedia", ignoreCase = true)
                ) {
                    val displayGrade = if (cleanGrade.isNotBlank() && !cleanGrade.contains("Belum Ada", ignoreCase = true)) {
                        cleanGrade
                    } else {
                        "Shahih"
                    }

                    val pureArabicMatan = extractArabicMatan(hadith.arabicText).ifBlank { hadith.arabicText }
                    val pureIndonesianMatan = extractMatanOnly(hadith.translation).ifBlank { hadith.translation }

                    return HadithOfTheDay(
                        bookSlug = book.slug,
                        bookName = book.cleanName,
                        number = hadith.number,
                        arabicText = pureArabicMatan,
                        translation = pureIndonesianMatan,
                        grade = displayGrade
                    )
                }
            } catch (e: Exception) {
                Log.w("DAILY_HADITH", "Mencoba kitab lain setelah gagal di ${book.shortName} #$randomNum: ${e.message}")
            }
        }

        // Fallback jika koneksi internet terputus atau seluruh percobaan timeout
        return DailyHadithProvider.getRandomHadith(excludeCurrent = _uiState.value.hadithOfTheDay)
    }

    private fun observeLastRead() {
        viewModelScope.launch {
            lastReadRepository.lastReadFlow.collect { lastReadHadith ->
                if (lastReadHadith != null) {
                    _uiState.update { it.copy(lastRead = lastReadHadith) }
                }
            }
        }
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            userProfileRepo.profileFlow.collect { profile ->
                val greetingName = profile.name.trim().ifBlank { "Thalibul Ilmi" }
                _uiState.update { it.copy(userName = greetingName) }
            }
        }
    }
}
