package com.nusa.hadithapp.data.repository

import com.nusa.hadithapp.data.data_source.local.dao.ReadingHistoryDao
import com.nusa.hadithapp.data.data_source.local.entity.ReadingHistoryEntity
import com.nusa.hadithapp.domain.model.BookReadingProgress
import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.model.ReadingHistoryItem
import com.nusa.hadithapp.domain.repository.ReadingHistoryRepository
import com.nusa.hadithapp.util.BookSlugUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReadingHistoryRepositoryImpl @Inject constructor(
    private val readingHistoryDao: ReadingHistoryDao
) : ReadingHistoryRepository {

    private val bookTotalHadithMap = mapOf(
        "ShahihBukhari" to 7008,
        "ShahihMuslim" to 5362,
        "SunanAbuDaud" to 4590,
        "SunanTirmidzi" to 3891,
        "SunanNasai" to 5662,
        "SunanIbnuMajah" to 4341,
        "MusnadAhmad" to 26363,
        "MuwathaMalik" to 1841,
        "SunanDarimi" to 3367,
        "SunanDaruquthni" to 4898,
        "ShahihIbnuKhuzaimah" to 1808,
        "ShahihIbnuHibban" to 2769,
        "AlMustadrak" to 673,
        "MusnadSyafii" to 1800
    )

    override fun getAllHistory(): Flow<List<ReadingHistoryItem>> {
        return readingHistoryDao.getAllHistory().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getHistoryByBook(bookSlug: String): Flow<List<ReadingHistoryItem>> {
        val canonicalSlug = BookSlugUtil.normalize(bookSlug)
        return readingHistoryDao.getHistoryByBook(canonicalSlug).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getDistinctBookCount(): Flow<Int> {
        return readingHistoryDao.getDistinctBookCount()
    }

    override fun getTotalReadCount(): Flow<Int> {
        return readingHistoryDao.getTotalReadCount()
    }

    override fun getReadCountByBook(bookSlug: String): Flow<Int> {
        val canonicalSlug = BookSlugUtil.normalize(bookSlug)
        return readingHistoryDao.getReadCountByBook(canonicalSlug)
    }

    override fun isHadithRead(bookSlug: String, number: Int): Flow<Boolean> {
        val canonicalSlug = BookSlugUtil.normalize(bookSlug)
        return readingHistoryDao.isHadithRead(canonicalSlug, number)
    }

    override fun getBooksReadingProgress(): Flow<List<BookReadingProgress>> {
        return readingHistoryDao.getAllHistory().map { list ->
            val grouped = list.groupBy { it.bookSlug }
            grouped.map { (slug, entities) ->
                val canonicalSlug = BookSlugUtil.normalize(slug)
                val bookName = entities.firstOrNull()?.bookName
                    ?: BookSlugUtil.getOfficialBookName(canonicalSlug)
                val total = bookTotalHadithMap[canonicalSlug] ?: 1000
                BookReadingProgress(
                    bookSlug = canonicalSlug,
                    bookName = bookName,
                    readCount = entities.size,
                    totalHadith = total,
                    latestHadiths = entities.take(5).map { it.toDomain() }
                )
            }.sortedByDescending { it.readCount }
        }
    }

    override suspend fun recordReading(hadith: Hadith, bookSlug: String) {
        val canonicalSlug = BookSlugUtil.normalize(bookSlug)
        val entity = ReadingHistoryEntity(
            id = "${canonicalSlug.lowercase()}_${hadith.number}",
            bookSlug = canonicalSlug,
            number = hadith.number,
            bookName = BookSlugUtil.getOfficialBookName(canonicalSlug),
            kitab = hadith.kitab,
            chapter = hadith.chapter,
            arabicSnippet = hadith.arabicText.take(120),
            translationSnippet = hadith.translation.take(150),
            readAt = System.currentTimeMillis()
        )
        readingHistoryDao.insertOrUpdate(entity)
    }

    override suspend fun deleteHistoryItem(bookSlug: String, number: Int) {
        val canonicalSlug = BookSlugUtil.normalize(bookSlug)
        readingHistoryDao.deleteHistoryItem(canonicalSlug, number)
    }

    override suspend fun clearAllHistory() {
        readingHistoryDao.clearAllHistory()
    }

    private fun ReadingHistoryEntity.toDomain(): ReadingHistoryItem {
        return ReadingHistoryItem(
            bookSlug = this.bookSlug,
            number = this.number,
            bookName = this.bookName,
            kitab = this.kitab,
            chapter = this.chapter,
            arabicSnippet = this.arabicSnippet,
            translationSnippet = this.translationSnippet,
            readAt = this.readAt
        )
    }
}
