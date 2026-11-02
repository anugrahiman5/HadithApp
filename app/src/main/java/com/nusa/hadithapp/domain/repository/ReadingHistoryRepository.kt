package com.nusa.hadithapp.domain.repository

import com.nusa.hadithapp.domain.model.BookReadingProgress
import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.model.ReadingHistoryItem
import kotlinx.coroutines.flow.Flow

interface ReadingHistoryRepository {
    fun getAllHistory(): Flow<List<ReadingHistoryItem>>
    fun getHistoryByBook(bookSlug: String): Flow<List<ReadingHistoryItem>>
    fun getDistinctBookCount(): Flow<Int>
    fun getTotalReadCount(): Flow<Int>
    fun getReadCountByBook(bookSlug: String): Flow<Int>
    fun isHadithRead(bookSlug: String, number: Int): Flow<Boolean>
    fun getBooksReadingProgress(): Flow<List<BookReadingProgress>>
    suspend fun recordReading(hadith: Hadith, bookSlug: String)
    suspend fun deleteHistoryItem(bookSlug: String, number: Int)
    suspend fun clearAllHistory()
}
