package com.nusa.hadithapp.ui.compare

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.data_source.remote.HadithApiService
import com.nusa.hadithapp.data.model.SimilarHadithDto
import com.nusa.hadithapp.domain.model.HadithBook
import com.nusa.hadithapp.domain.model.PerawiProfile
import com.nusa.hadithapp.domain.model.SanadPerawi
import com.nusa.hadithapp.domain.model.ScholarComment
import com.nusa.hadithapp.domain.model.compare.ComparedHadith
import com.nusa.hadithapp.domain.model.compare.MatanMatrixRow
import com.nusa.hadithapp.domain.model.compare.ReferenceHadith
import com.nusa.hadithapp.ui.detail.PerawiUiState
import com.nusa.hadithapp.ui.detail.ScholarCommentUiState
import com.nusa.hadithapp.util.ArabicDiffUtil
import com.nusa.hadithapp.util.BookSlugUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HadithCompareViewModel @Inject constructor(
    private val apiService: HadithApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HadithCompareUiState())
    val uiState: StateFlow<HadithCompareUiState> = _uiState.asStateFlow()

    private var currentBaseSlug: String = ""
    private var currentBaseNumber: Int = 1
    private var allSimilarList: List<SimilarHadithDto> = emptyList()

    // State dialog profil perawi ketika nama rawi di sanad diklik
    private val _selectedPerawi = MutableStateFlow<SanadPerawi?>(null)
    val selectedPerawi: StateFlow<SanadPerawi?> = _selectedPerawi.asStateFlow()

    private val _perawiProfileState = MutableStateFlow<PerawiUiState>(PerawiUiState.Empty)
    val perawiProfileState: StateFlow<PerawiUiState> = _perawiProfileState.asStateFlow()

    private val _scholarCommentState = MutableStateFlow<ScholarCommentUiState>(ScholarCommentUiState.Empty)
    val scholarCommentState: StateFlow<ScholarCommentUiState> = _scholarCommentState.asStateFlow()

    fun loadComparison(bookSlug: String, number: Int) {
        if (currentBaseSlug == bookSlug && currentBaseNumber == number && !_uiState.value.isLoading) {
            return
        }
        currentBaseSlug = bookSlug
        currentBaseNumber = number

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val baseCleanSlug = BookSlugUtil.normalize(bookSlug)
                val baseBook = HadithBook.fromSlug(baseCleanSlug)

                // 1. Muat data acuan, sanad acuan, dan daftar similar secara paralel bersamaan
                val baseHadithDeferred = async { apiService.getHadithDetail(bookSlug, number) }
                val baseSanadDeferred = async { apiService.getSanadChain(baseCleanSlug, number) }
                val similarListDeferred = async { apiService.getSimilarHadith(baseCleanSlug, number) }

                val baseHadithDto = baseHadithDeferred.await()
                val baseSanadChain = baseSanadDeferred.await()
                val similarList = similarListDeferred.await()
                allSimilarList = similarList

                val sahabatRaw = baseSanadChain.lastOrNull()?.name ?: "Sahabat Nabi"
                val cleanSahabat = ArabicDiffUtil.formatRawiShortName(sahabatRaw)

                val refHadith = ReferenceHadith(
                    bookSlug = baseCleanSlug,
                    bookName = baseBook.fullName,
                    number = number,
                    arabicText = baseHadithDto.arabicText,
                    translation = baseHadithDto.translation,
                    kitabName = baseHadithDto.kitab.ifBlank { "Kitab Induk" },
                    babName = baseHadithDto.chapter,
                    grade = baseHadithDto.grade,
                    sahabatName = cleanSahabat,
                    sanadChain = baseSanadChain,
                    totalJalurDitemukan = similarList.size
                )

                // Hitung jumlah riwayat per kitab
                val counts = mutableMapOf<String, Int>()
                counts["Semua"] = similarList.size
                similarList.forEach { item ->
                    val canonicalBook = HadithBook.fromSlug(item.name)
                    val bookKey = canonicalBook.cleanName
                    counts[bookKey] = (counts[bookKey] ?: 0) + 1
                }

                // Tambahkan Hadits Acuan ke baris pertama matriks
                val baseMatan = ArabicDiffUtil.extractMatan(baseHadithDto.arabicText)
                val baseSnippet = if (baseMatan.length > 55) baseMatan.take(55).trim() + "..." else baseMatan
                val baseMatrixRow = MatanMatrixRow(
                    bookLabel = "${baseBook.shortName} #$number (Acuan)",
                    similarityStatus = "Acuan (100%)",
                    matanSnippet = baseSnippet,
                    contextBab = baseHadithDto.kitab.ifBlank { baseHadithDto.chapter.ifBlank { "Bab Awal" } }
                )

                val notes = "Hadis ini berakar pada tingkatan sahabat melalui $cleanSahabat. Ditemukan ${similarList.size} jalur riwayat paralel di berbagai kitab mu'tabar. Teks berwarna menandakan variasi kata antar-riwayat."

                // 2. Muat data hadits pembanding batch pertama (6 riwayat teratas)
                val targetToLoad = similarList.take(6)
                val remainingCount = maxOf(0, similarList.size - targetToLoad.size)

                // Langsung render data acuan ke UI agar tidak menunggu (sangat responsif!)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        referenceHadith = refHadith,
                        bookCounts = counts,
                        takhrijNotes = notes,
                        matanMatrix = listOf(baseMatrixRow),
                        totalJalurCount = similarList.size,
                        remainingToLoadCount = remainingCount
                    )
                }

                val comparedDeferred = targetToLoad.map { item ->
                    async {
                        val targetNum = item.number.toIntOrNull() ?: return@async null
                        val targetBook = HadithBook.fromSlug(item.name)

                        val customDataDeferred = async { apiService.getSimilarCustomData(targetBook.slug, targetNum) }
                        val sanadDeferred = async { apiService.getSanadChain(targetBook.slug, targetNum) }

                        val customData = customDataDeferred.await() ?: return@async null
                        if (customData.arabic.isBlank()) return@async null
                        val sanadChain = sanadDeferred.await()

                        val (similarity, differingWords) = ArabicDiffUtil.analyzeSimilarity(
                            baseArabic = baseHadithDto.arabicText,
                            targetArabic = customData.arabic
                        )

                        // Catatan variasi lafadz
                        val cleanTarget = ArabicDiffUtil.normalizeArabic(customData.arabic)
                        val cleanBase = ArabicDiffUtil.normalizeArabic(baseHadithDto.arabicText)
                        val variantNote = when {
                            cleanTarget.contains("بالنيه") && !cleanBase.contains("بالنيه") ->
                                "Variasi lafaz: Menggunakan bentuk tunggal بِالنِّيَّةِ"
                            cleanTarget.contains("بالنيات") && !cleanBase.contains("بالنيات") ->
                                "Variasi lafaz: Menggunakan bentuk jamak بِالنِّيَّاتِ"
                            cleanTarget.contains("الكنيف") -> "Variasi istilah: Menggunakan lafaz الْكَنِيف (jamban/WC)"
                            cleanTarget.contains("الخلاء") && !cleanBase.contains("الخلاء") ->
                                "Variasi istilah: Menggunakan lafaz الْخَلَاء"
                            else -> null
                        }

                        val comparedItem = ComparedHadith(
                            id = "${targetBook.slug}_$targetNum",
                            bookSlug = targetBook.slug,
                            bookName = targetBook.fullName,
                            number = targetNum,
                            arabicText = customData.arabic,
                            translation = customData.indonesia,
                            kitabName = customData.kitabName.ifBlank { targetBook.cleanName },
                            babName = customData.babName,
                            grade = customData.grade,
                            sanadChain = sanadChain,
                            similarityPercent = similarity,
                            differingWords = differingWords,
                            variantNote = variantNote
                        )

                        // Baris matriks
                        val targetMatan = ArabicDiffUtil.extractMatan(customData.arabic)
                        val targetSnippet = if (targetMatan.length > 55) targetMatan.take(55).trim() + "..." else targetMatan
                        val statusText = if (similarity >= 99) "Identik (100%)" else "$similarity% Mirip"
                        val matrixRow = MatanMatrixRow(
                            bookLabel = "${targetBook.shortName} #$targetNum",
                            similarityStatus = statusText,
                            matanSnippet = targetSnippet,
                            contextBab = customData.kitabName.ifBlank { customData.babName }
                        )

                        Pair(comparedItem, matrixRow)
                    }
                }

                val results = comparedDeferred.awaitAll().filterNotNull()
                val loadedComparedList = results.map { it.first }
                val allMatrixRows = listOf(baseMatrixRow) + results.map { it.second }

                _uiState.update {
                    it.copy(
                        allComparedHadiths = loadedComparedList,
                        displayedHadiths = loadedComparedList,
                        matanMatrix = allMatrixRows
                    )
                }
            } catch (e: Exception) {
                Log.e("COMPARE_VM", "Error memuat komparasi hadits: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Gagal memuat data komparasi hadits."
                    )
                }
            }
        }
    }

    /**
     * Memuat semua riwayat paralel yang tersisa (jika ditemukan > 6 riwayat).
     */
    fun loadRemainingHadiths() {
        if (_uiState.value.isLoadingRemaining || allSimilarList.size <= _uiState.value.allComparedHadiths.size) return
        val currentLoadedIds = _uiState.value.allComparedHadiths.map { "${it.bookSlug}_${it.number}" }.toSet()
        val remainingList = allSimilarList.filter { "${BookSlugUtil.normalize(it.name)}_${it.number}" !in currentLoadedIds }
        if (remainingList.isEmpty()) {
            _uiState.update { it.copy(remainingToLoadCount = 0) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoadingRemaining = true) }
            try {
                val baseHadith = _uiState.value.referenceHadith ?: return@launch
                val remainingDeferred = remainingList.map { item ->
                    async {
                        val targetNum = item.number.toIntOrNull() ?: return@async null
                        val targetBook = HadithBook.fromSlug(item.name)

                        val customDataDeferred = async { apiService.getSimilarCustomData(targetBook.slug, targetNum) }
                        val sanadDeferred = async { apiService.getSanadChain(targetBook.slug, targetNum) }

                        val customData = customDataDeferred.await() ?: return@async null
                        if (customData.arabic.isBlank()) return@async null
                        val sanadChain = sanadDeferred.await()

                        val (similarity, differingWords) = ArabicDiffUtil.analyzeSimilarity(
                            baseArabic = baseHadith.arabicText,
                            targetArabic = customData.arabic
                        )

                        val comparedItem = ComparedHadith(
                            id = "${targetBook.slug}_$targetNum",
                            bookSlug = targetBook.slug,
                            bookName = targetBook.fullName,
                            number = targetNum,
                            arabicText = customData.arabic,
                            translation = customData.indonesia,
                            kitabName = customData.kitabName.ifBlank { targetBook.cleanName },
                            babName = customData.babName,
                            grade = customData.grade,
                            sanadChain = sanadChain,
                            similarityPercent = similarity,
                            differingWords = differingWords
                        )

                        val targetMatan = ArabicDiffUtil.extractMatan(customData.arabic)
                        val targetSnippet = if (targetMatan.length > 55) targetMatan.take(55).trim() + "..." else targetMatan
                        val statusText = if (similarity >= 99) "Identik (100%)" else "$similarity% Mirip"
                        val matrixRow = MatanMatrixRow(
                            bookLabel = "${targetBook.shortName} #$targetNum",
                            similarityStatus = statusText,
                            matanSnippet = targetSnippet,
                            contextBab = customData.kitabName.ifBlank { customData.babName }
                        )

                        Pair(comparedItem, matrixRow)
                    }
                }

                val results = remainingDeferred.awaitAll().filterNotNull()
                val newlyLoaded = results.map { it.first }
                val newMatrixRows = results.map { it.second }

                _uiState.update { current ->
                    val updatedAll = current.allComparedHadiths + newlyLoaded
                    val updatedMatrix = current.matanMatrix + newMatrixRows
                    val filtered = if (current.selectedBookFilter == null || current.selectedBookFilter == "Semua") {
                        updatedAll
                    } else {
                        updatedAll.filter {
                            val book = HadithBook.fromSlug(it.bookSlug)
                            book.cleanName.equals(current.selectedBookFilter, true) || it.bookName.contains(current.selectedBookFilter, true)
                        }
                    }
                    current.copy(
                        isLoadingRemaining = false,
                        allComparedHadiths = updatedAll,
                        displayedHadiths = filtered,
                        matanMatrix = updatedMatrix,
                        remainingToLoadCount = 0
                    )
                }
            } catch (e: Exception) {
                Log.e("COMPARE_VM", "Error loading remaining hadiths: ${e.message}", e)
                _uiState.update { it.copy(isLoadingRemaining = false) }
            }
        }
    }

    fun filterByBook(bookName: String?) {
        _uiState.update { current ->
            val filtered = if (bookName == null || bookName == "Semua") {
                current.allComparedHadiths
            } else {
                current.allComparedHadiths.filter {
                    val book = HadithBook.fromSlug(it.bookSlug)
                    book.cleanName.equals(bookName, true) || it.bookName.contains(bookName, true)
                }
            }
            current.copy(
                selectedBookFilter = if (bookName == "Semua") null else bookName,
                displayedHadiths = filtered
            )
        }

        // Jika buku yang difilter masih memiliki riwayat yang belum dimuat, pemicu muat otomatis
        if (bookName != null && bookName != "Semua" && _uiState.value.remainingToLoadCount > 0) {
            val hasUnloadedForBook = allSimilarList.any { item ->
                val book = HadithBook.fromSlug(item.name)
                book.cleanName.equals(bookName, true) &&
                        _uiState.value.allComparedHadiths.none { it.number == item.number.toIntOrNull() && it.bookSlug == book.slug }
            }
            if (hasUnloadedForBook) {
                loadRemainingHadiths()
            }
        }
    }

    fun toggleHighlight(enabled: Boolean) {
        _uiState.update { it.copy(isHighlightEnabled = enabled) }
    }

    // ==========================================
    // DETAIL PROFIL RAWI / PERAWI INTERACTION
    // ==========================================

    fun selectPerawi(perawi: SanadPerawi) {
        _selectedPerawi.value = perawi
        loadPerawiProfile(perawi.kodeRawi)
        loadScholarComments(perawi.kodeRawi)
    }

    fun dismissPerawi() {
        _selectedPerawi.value = null
        _perawiProfileState.value = PerawiUiState.Empty
        _scholarCommentState.value = ScholarCommentUiState.Empty
    }

    suspend fun loadPerawiHadithNumbers(slug: String, kodeRawi: Int): List<Int> {
        return apiService.getPerawiTotalHadith(slug, kodeRawi)
    }

    private fun loadPerawiProfile(kodeRawi: Int) {
        if (kodeRawi <= 0) return
        _perawiProfileState.value = PerawiUiState.Loading
        viewModelScope.launch {
            try {
                val dto = apiService.getPerawiProfile(kodeRawi)
                if (dto != null) {
                    _perawiProfileState.value = PerawiUiState.Success(
                        PerawiProfile(
                            kodeRawi = dto.kodeRawi,
                            nama = dto.name,
                            quality = dto.quality,
                            kalangan = dto.kalangan,
                            kuniyah = dto.kuniyah ?: "",
                            laqob = dto.laqob ?: "",
                            nasab = dto.nasab ?: "",
                            negeriHidup = dto.negeriHidup ?: "",
                            negeriWafat = dto.negeriWafat ?: "",
                            tahunWafat = dto.tahunWafat ?: "",
                            rBukhari = dto.rBukhari ?: 0,
                            rMuslim = dto.rMuslim ?: 0,
                            rAbuDaud = dto.rAbuDaud ?: 0,
                            rTirmidzi = dto.rTirmidzi ?: 0,
                            rNasai = dto.rNasai ?: 0,
                            rIbnuMajah = dto.rIbnuMajah ?: 0,
                            rAhmad = dto.rAhmad ?: 0,
                            rMalik = dto.rMalik ?: 0,
                            rDarimi = dto.rDarimi ?: 0
                        )
                    )
                } else {
                    _perawiProfileState.value = PerawiUiState.Error("Data perawi tidak ditemukan")
                }
            } catch (e: Exception) {
                _perawiProfileState.value = PerawiUiState.Error(e.localizedMessage ?: "Gagal memuat profil perawi")
            }
        }
    }

    private fun loadScholarComments(kodeRawi: Int) {
        if (kodeRawi <= 0) return
        _scholarCommentState.value = ScholarCommentUiState.Loading
        viewModelScope.launch {
            try {
                val commentsDto = apiService.getScholarComments(kodeRawi)
                val comments = commentsDto.map {
                    ScholarComment(
                        ulama = it.ulama ?: "",
                        komentar = it.komentar ?: "-"
                    )
                }
                _scholarCommentState.value = ScholarCommentUiState.Success(comments)
            } catch (e: Exception) {
                _scholarCommentState.value = ScholarCommentUiState.Error(e.localizedMessage ?: "Gagal memuat komentar ulama")
            }
        }
    }
}
