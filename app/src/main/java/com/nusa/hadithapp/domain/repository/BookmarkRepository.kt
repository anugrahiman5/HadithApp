package com.nusa.hadithapp.domain.repository

import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.domain.model.Hadith
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getAllBookmarks(): Flow<List<Bookmark>>
    fun getBookmark(bookSlug: String, number: Int) : Flow<Bookmark?>
    fun getAllFolders(): Flow<List<String>>
    fun isBookmarked(bookSlug: String, number: Int): Flow<Boolean>
    suspend fun addBookmark(hadith: Hadith, bookSlug: String, folderTags: List<String> = listOf("Semua Bookmark"))
    suspend fun removeBookmark(bookSlug: String, number: Int)
    suspend fun updateBookmarkFolders(bookSlug: String, number: Int, newFolders: List<String>)
}