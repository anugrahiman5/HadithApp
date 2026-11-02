package com.nusa.hadithapp.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nusa.hadithapp.data.data_source.local.entity.CachedHadithEntity

@Dao
interface CachedHadithDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHadith(hadith: CachedHadithEntity)

    @Query("SELECT * FROM cached_hadiths WHERE id = :id LIMIT 1")
    suspend fun getHadithById(id: String): CachedHadithEntity?

    @Query("SELECT * FROM cached_hadiths WHERE bookSlug = :bookSlug AND number = :number LIMIT 1")
    suspend fun getHadith(bookSlug: String, number: Int): CachedHadithEntity?

    @Query("SELECT COUNT(*) FROM cached_hadiths")
    suspend fun getCachedCount(): Int

    @Query("DELETE FROM cached_hadiths")
    suspend fun clearAllCache()
}
