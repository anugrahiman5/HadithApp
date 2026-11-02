package com.nusa.hadithapp.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nusa.hadithapp.data.data_source.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT COUNT(*) FROM bookmarks")
    fun getBookmarkCount(): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id)")
    fun isBookmarked(id: String): Flow<Boolean>

    // Ambil data bookmark spesifik hadits ini
    @Query("SELECT * FROM bookmarks WHERE id = :id")
    fun getBookmarkById(id: String): Flow<BookmarkEntity?>

    // Ambil daftar seluruh nama folder yang sudah ada
    @Query("SELECT DISTINCT foldertags FROM bookmarks")
    fun getAllFolderTags(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: String)

    @Query("UPDATE bookmarks SET foldertags = :newFolder WHERE id = :id")
    suspend fun updateBookmarkFolders(id: String, newFolder: String)
}
