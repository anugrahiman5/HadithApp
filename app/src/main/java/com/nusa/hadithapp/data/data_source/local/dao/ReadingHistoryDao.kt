package com.nusa.hadithapp.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nusa.hadithapp.data.data_source.local.entity.ReadingHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: ReadingHistoryEntity)

    @Query("SELECT * FROM reading_history ORDER BY readAt DESC")
    fun getAllHistory(): Flow<List<ReadingHistoryEntity>>

    @Query("SELECT * FROM reading_history WHERE bookSlug = :bookSlug ORDER BY readAt DESC")
    fun getHistoryByBook(bookSlug: String): Flow<List<ReadingHistoryEntity>>

    @Query("SELECT COUNT(DISTINCT bookSlug) FROM reading_history")
    fun getDistinctBookCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM reading_history")
    fun getTotalReadCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM reading_history WHERE bookSlug = :bookSlug")
    fun getReadCountByBook(bookSlug: String): Flow<Int>

    @Query("SELECT DISTINCT bookSlug FROM reading_history")
    fun getDistinctBookSlugs(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM reading_history WHERE bookSlug = :bookSlug AND number = :number)")
    fun isHadithRead(bookSlug: String, number: Int): Flow<Boolean>

    @Query("DELETE FROM reading_history WHERE bookSlug = :bookSlug AND number = :number")
    suspend fun deleteHistoryItem(bookSlug: String, number: Int)

    @Query("DELETE FROM reading_history")
    suspend fun clearAllHistory()
}
