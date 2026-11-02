package com.nusa.hadithapp.data.repository

import com.nusa.hadithapp.data.data_source.local.dao.CachedHadithDao
import com.nusa.hadithapp.data.data_source.local.entity.CachedHadithEntity
import com.nusa.hadithapp.data.data_source.remote.HadithApiService
import com.nusa.hadithapp.data.data_source.remote.config.HadithClassificationMetadata
import com.nusa.hadithapp.data.mapper.HadithMapper
import com.nusa.hadithapp.data.model.RawiSearchDto
import com.nusa.hadithapp.data.model.SearchRequestDto
import com.nusa.hadithapp.data.session.SearchSessionManager
import com.nusa.hadithapp.domain.model.BabItem
import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.model.HadithCategoryItem
import com.nusa.hadithapp.domain.model.HadithSearchItem
import com.nusa.hadithapp.domain.model.HadithThemeItem
import com.nusa.hadithapp.domain.model.KitabItem
import com.nusa.hadithapp.domain.model.PerawiProfile
import com.nusa.hadithapp.domain.model.RawiSearchItem
import com.nusa.hadithapp.domain.model.ScholarComment
import com.nusa.hadithapp.domain.model.SearchResult
import com.nusa.hadithapp.domain.model.SimilarHadith
import com.nusa.hadithapp.domain.repository.HadithRepository
import com.nusa.hadithapp.util.BookSlugUtil
import com.nusa.hadithapp.util.SearchSnippetHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import javax.inject.Inject
import kotlin.collections.flatten

class HadithRepositoryImpl @Inject constructor(
    private val apiService: HadithApiService,
    private val searchSessionManager: SearchSessionManager,
    private val cachedHadithDao: CachedHadithDao
) : HadithRepository {

    private val networkSemaphore = Semaphore(permits = 8)

    override suspend fun getHadithByBook(bookSlug: String, number: Int): Hadith {
        val cleanSlug = BookSlugUtil.normalize(bookSlug)
        return try {
            val dto = apiService.getHadithDetail(cleanSlug, number)
            val domain = HadithMapper.toDomain(dto)

            // Simpan ke cache lokal otomatis (On-Demand Offline Caching)
            try {
                cachedHadithDao.insertHadith(
                    CachedHadithEntity(
                        id = "${cleanSlug.lowercase()}_$number",
                        bookSlug = cleanSlug,
                        number = number,
                        bookName = domain.bookName,
                        kitab = domain.kitab,
                        chapter = domain.chapter,
                        arabicText = domain.arabicText,
                        translation = domain.translation,
                        grade = domain.grade,
                        narrator = domain.narrator,
                        syarah = domain.syarah,
                        otherNumber = domain.otherNumber,
                        totalHadith = domain.totalHadith
                    )
                )
            } catch (e: Exception) {
                // Jangan batalkan proses bila penulisan cache gagal
            }

            domain
        } catch (networkError: Exception) {
            // Jika koneksi gagal / offline, cari di memori cache lokal
            val cached = cachedHadithDao.getHadith(cleanSlug, number)
                ?: cachedHadithDao.getHadithById("${cleanSlug.lowercase()}_$number")

            if (cached != null) {
                Hadith(
                    id = cached.id,
                    bookName = cached.bookName,
                    number = cached.number,
                    kitab = cached.kitab,
                    chapter = cached.chapter,
                    arabicText = cached.arabicText,
                    translation = cached.translation,
                    grade = cached.grade,
                    narrator = cached.narrator,
                    syarah = "${cached.syarah}\n(Dimuat dari memori offline)",
                    sanadJalurList = emptyList(),
                    researcherList = emptyList(),
                    otherNumber = cached.otherNumber,
                    totalHadith = cached.totalHadith
                )
            } else {
                throw networkError
            }
        }
    }

    override suspend fun getPerawiProfile(kodeRawi: Int): PerawiProfile? {
        val dto = apiService.getPerawiProfile(kodeRawi) ?: return null
        return PerawiProfile(
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
    }

    override suspend fun getScholarComments(kodeRawi: Int): List<ScholarComment> {
        val dtoList = apiService.getScholarComments(kodeRawi)
        return dtoList.map { dto ->
            ScholarComment(
                ulama = dto.ulama ?: "",
                komentar = dto.komentar ?: "-"
            )
        }
    }

    override suspend fun getAllBooks(bookSlug: String): List<KitabItem> {
        return apiService.getAllBooks(bookSlug).map { dto ->
            KitabItem(
                nKitab = dto.nKitab ?: "",
                vMember = dto.vMember ?: "",
                awalan = dto.awalan ?: "1"
            )
        }
    }

    override suspend fun getAllChapters(
        bookSlug: String,
        start: Int,
        end: Int
    ): List<BabItem> {
        return apiService.getAllChapters(bookSlug, start, end).map { dto ->
            BabItem(
                nBab = dto.nBab ?: "",
                vMemberBab = dto.vMemberBab ?: "",
                awalanBab = dto.awalanBab ?: "1"
            )
        }
    }

    override suspend fun getHadithThemeBatch(
        themeName: String,
        startVMember: Int,
        endVMember: Int
    ): List<HadithThemeItem> = coroutineScope {
        val vMembers = (startVMember..endVMember).toList()

        val deferredResults = vMembers.map { vMember ->
            async(Dispatchers.IO) {
                try {
                    val correctSlug =
                        HadithClassificationMetadata.resolveBookSlug(themeName, vMember)

                    val rawItems = apiService.getHadithByTheme(correctSlug, vMember, themeName)
                    val officialBookName = correctSlug.replace(Regex("(?<!^)(?=[A-Z])"), " ")

                    rawItems.map { dto ->
                        val babName = dto.babTitle?.firstOrNull()?.nBab ?: "Bab"
                        HadithThemeItem(
                            bookSlug = correctSlug,
                            number = dto.Nomer,
                            arabicText = dto.Arabic ?: "",
                            translation = dto.Indonesia ?: "",
                            bookName = officialBookName,
                            chapterName = babName,
                            totalCount = HadithClassificationMetadata.getTotalCount(themeName),
                            vMember = vMember
                        )
                    }
                } catch (e: Exception) {
                    emptyList<HadithThemeItem>()
                }
            }
        }

        deferredResults.awaitAll().flatten()
    }

    override suspend fun getHadithByThemeVMember(themeName: String, vMember: Int): Hadith {
        val correctSlug = HadithClassificationMetadata.resolveBookSlug(themeName, vMember)
        val rawResponse = apiService.getHadithByTheme(correctSlug, vMember, themeName)

        val itemDto = rawResponse.getOrNull(0)
            ?: throw Exception("Data hadits tema tidak ditemukan untuk vMember: $vMember")

        val targetNumber = itemDto.Nomer
        val fullHadith = getHadithByBook(correctSlug, targetNumber)

        return fullHadith.copy(
            totalHadith = HadithClassificationMetadata.getTotalCount(themeName)
        )
    }

    override suspend fun getHadithCategoryBatch(
        categoryName: String,
        startVMember: Int,
        endVMember: Int
    ): List<HadithCategoryItem> = coroutineScope {
        val vMembers = (startVMember..endVMember).toList()

        val deferredResults = vMembers.map { vMember ->
            async(Dispatchers.IO) {
                try {
                    val correctSlug =
                        HadithClassificationMetadata.resolveBookSlug(categoryName, vMember)
                    val rawItems = apiService.getHadithByTheme(correctSlug, vMember, categoryName)
                    val officialBookName = correctSlug.replace(Regex("(?<!^)(?=[A-Z])"), " ")

                    rawItems.map { dto ->
                        val babName = dto.babTitle?.firstOrNull()?.nBab ?: "Bab"
                        HadithCategoryItem(
                            bookSlug = correctSlug,
                            number = dto.Nomer,
                            arabicText = dto.Arabic ?: "",
                            translation = dto.Indonesia ?: "",
                            bookName = officialBookName,
                            chapterName = babName,
                            totalCount = HadithClassificationMetadata.getTotalCount(categoryName),
                            vMember = vMember
                        )
                    }
                } catch (e: Exception) {
                    emptyList<HadithCategoryItem>()
                }
            }
        }
        deferredResults.awaitAll().flatten()
    }

    override suspend fun getHadithByCategoryVMember(categoryName: String, vMember: Int): Hadith {
        val correctSlug = HadithClassificationMetadata.resolveBookSlug(categoryName, vMember)
        val rawResponse = apiService.getHadithByTheme(correctSlug, vMember, categoryName)

        val itemDto = rawResponse.getOrNull(0)
            ?: throw Exception("Data hadits kategori tidak ditemukan untuk vMember: $vMember")

        val targetNumber = itemDto.Nomer
        val fullHadith = getHadithByBook(correctSlug, targetNumber)

        return fullHadith.copy(
            totalHadith = HadithClassificationMetadata.getTotalCount(categoryName)
        )
    }

    override fun searchHadits(keyword: String, books: List<String>): Flow<SearchResult> =
        channelFlow {
            val trimmed = keyword.trim()
            val keywordList = trimmed.split("\\s+".toRegex()).filter { it.isNotBlank() }

            val request = SearchRequestDto(
                keyword = keywordList,
                books = books
            )
            // Deteksi otomatis bahasa: jika input mengandung huruf hijaiyah, cari di kolom "Arabic"
            val isArabic = trimmed.any { it in '\u0600'..'\u06FF' }
            val searchColumn = if (isArabic) "Arabic" else "Indonesia"

            val response = apiService.searchHadith(searchColumn, request)
            val totalAll = response.total
            val resultsMap = mutableMapOf<String, List<HadithSearchItem>>()
            val allNumbersMap = mutableMapOf<String, List<Int>>()

            val sortedEntries = response.results?.entries?.sortedBy { (bookSlug, _) ->
                val index = BookSlugUtil.ALL_BOOK_SLUGS.indexOf(bookSlug)
                if (index != -1) index else Int.MAX_VALUE
            } ?: emptyList()

            val globalAllSearchItems = mutableListOf<HadithSearchItem>()
            sortedEntries.forEach { (bookSlug, bookResult) ->
                if (bookResult.count > 0 && !bookResult.rows.isNullOrEmpty()) {
                    val officialBookName = bookSlug.replace(Regex("(?<!^)(?=[A-Z])"), " ")
                    val allNumbers = bookResult.rows.mapNotNull { jsonObj ->
                        jsonObj.values.firstOrNull()?.toString()?.toIntOrNull()
                    }.distinct()

                    allNumbersMap[bookSlug] = allNumbers

                    allNumbers.forEach { number ->
                        globalAllSearchItems.add(
                            HadithSearchItem(
                                bookSlug = bookSlug,
                                bookName = officialBookName,
                                number = number,
                                arabicText = "",
                                translation = "",
                                chapterName = "",
                                grade = "",
                                narrator = ""
                            )
                        )
                    }
                }
            }

            searchSessionManager.updateSearchItems(globalAllSearchItems)

            val fetchedResults = sortedEntries.map { (bookSlug, bookResult) ->
                async(Dispatchers.IO) {
                    if (bookResult.count > 0 && !bookResult.rows.isNullOrEmpty()) {
                        val officialBookName = bookSlug.replace(Regex("(?<!^)(?=[A-Z])"), " ")
                        val rowList = bookResult.rows
                        val allNumbers = rowList.mapNotNull { jsonObj ->
                            jsonObj.values.firstOrNull()?.toString()?.toIntOrNull()
                        }.distinct()

                        val firstBatchNumbers = allNumbers.take(3)

                        val searchItems = firstBatchNumbers.map { number ->
                            async {
                                networkSemaphore.withPermit {
                                    try {
                                        val fullHadith =
                                            apiService.getHadithDetail(bookSlug, number)
                                        val summarizedArabic = SearchSnippetHelper.summarizeArabic(
                                            fullHadith.arabicText, maxWords = 10
                                        )
                                        val snippetTranslation =
                                            SearchSnippetHelper.getSnippetTranslation(
                                                fullText = fullHadith.translation,
                                                keyword = keyword,
                                                radius = 60
                                            )
                                        HadithSearchItem(
                                            bookSlug = bookSlug,
                                            bookName = officialBookName,
                                            number = number,
                                            arabicText = summarizedArabic,
                                            translation = snippetTranslation,
                                            chapterName = fullHadith.chapter,
                                            grade = fullHadith.grade,
                                            narrator = fullHadith.narrator
                                        )
                                    } catch (e: Exception) {
                                        null
                                    }
                                }
                            }
                        }.awaitAll().filterNotNull()

                        if (searchItems.isNotEmpty()) {
                            bookSlug to searchItems
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                }
            }.awaitAll().filterNotNull()

            BookSlugUtil.ALL_BOOK_SLUGS.forEach { slug ->
                val found = fetchedResults.find { it.first == slug }
                if (found != null) {
                    resultsMap[found.first] = found.second
                }
            }

            if (resultsMap.isNotEmpty()) {
                send(
                    SearchResult(
                        total = totalAll,
                        resultsPerBook = resultsMap.toMap(),
                        allNumbersPerBook = allNumbersMap.toMap()
                    )
                )
            }
        }

    override suspend fun getHadithBatchDetails(
        bookSlug: String,
        numbers: List<Int>
    ): List<HadithSearchItem> = coroutineScope {
        val officialBookName = bookSlug.replace(Regex("(?<!^)(?=[A-Z])"), " ")

        numbers.map { number ->
            async(Dispatchers.IO) {
                networkSemaphore.withPermit {
                    try {
                        val fullHadith = apiService.getHadithDetail(bookSlug, number)
                        val summarizedArabic = SearchSnippetHelper.summarizeArabic(
                            fullHadith.arabicText,
                            maxWords = 10
                        )
                        val snippetTranslation = SearchSnippetHelper.getSnippetTranslation(
                            fullText = fullHadith.translation,
                            keyword = "",
                            radius = 60
                        )
                        HadithSearchItem(
                            bookSlug = bookSlug,
                            bookName = officialBookName,
                            number = number,
                            arabicText = summarizedArabic,
                            translation = snippetTranslation,
                            chapterName = fullHadith.chapter,
                            grade = fullHadith.grade,
                            narrator = fullHadith.narrator
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
            }
        }.awaitAll().filterNotNull()
    }

    override suspend fun searchRawi(
        name: String,
        kuniyah: String,
        classify: String,
        level: String
    ): List<RawiSearchItem> {
        val request = RawiSearchDto(
            name = name,
            kuniyah = kuniyah,
            classify = classify,
            level = level
        )
        val dtoList = apiService.searchRawi(request)

        return dtoList.map { dto ->
            val rawiCode = dto.kodeRawi?.toIntOrNull() ?: 0
            val qualityLevel = dto.quality?.toIntOrNull() ?: 0

            RawiSearchItem(
                kodeRawi = rawiCode,
                name = dto.nama ?: "",
                quality = qualityLevel,
                kalangan = classify.ifBlank { null }
            )
        }
    }

    override suspend fun getSimilarHadith(bookSlug: String, number: Int): List<SimilarHadith> {
        val dtoList = apiService.getSimilarHadith(bookSlug, number)
        return dtoList.map { HadithMapper.toDomain(it) }
    }

    override suspend fun getPerawiTotalHadith(bookSlug: String, kodeRawi: Int): List<Int> {
        val cleanSlug = BookSlugUtil.normalize(bookSlug)
        return apiService.getPerawiTotalHadith(cleanSlug, kodeRawi)
    }
}
