package com.nusa.hadithapp.data.repository

import com.nusa.hadithapp.data.data_source.local.dao.BookmarkDao
import com.nusa.hadithapp.data.data_source.local.entity.BookmarkEntity
import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.repository.BookmarkRepository
import com.nusa.hadithapp.util.BookSlugUtil
import com.nusa.hadithapp.util.extractPerawiNames
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {

    override fun getAllBookmarks(): Flow<List<Bookmark>> {
        return bookmarkDao.getAllBookmarks().map { entities ->
            entities.map { entity ->
                val tagList = entity.foldertags.split(",").map { it.trim() }.filter { it.isNotBlank() }
                Bookmark(
                    id = entity.id,
                    bookSlug = entity.bookSlug,
                    number = entity.number,
                    bookName = entity.bookName,
                    kitabTitle = entity.kitabTitle,
                    babTitle = entity.babTitle,
                    arabicText = entity.arabicText,
                    translation = entity.translation,
                    grade = entity.grade,
                    folderTags = if (tagList.isEmpty()) listOf("Semua Bookmark") else tagList,
                    sanadSnippet = entity.sanadSnippet,
                    userNote = entity.userNote,
                    createdAt = entity.createdAt
                )
            }
        }
    }

    override fun getBookmark(bookSlug: String, number: Int): Flow<Bookmark?> {
        val id = "${bookSlug.lowercase()}_$number"
        return bookmarkDao.getBookmarkById(id).map { entity ->
            entity?.let {
                val tagList = entity.foldertags.split(",").map { it.trim() }.filter { it.isNotBlank() }
                Bookmark(
                    id = it.id,
                    bookSlug = it.bookSlug,
                    number = it.number,
                    bookName = it.bookName,
                    kitabTitle = it.kitabTitle,
                    babTitle = it.babTitle,
                    arabicText = it.arabicText,
                    translation = it.translation,
                    grade = it.grade,
                    folderTags = if (tagList.isEmpty()) listOf("Semua Bookmark") else tagList,
                    sanadSnippet = it.sanadSnippet,
                    userNote = it.userNote,
                    createdAt = it.createdAt
                )
            }
        }
    }

    override fun getAllFolders(): Flow<List<String>> {
        val defaultFolders =
            listOf("Materi Khutbah Jumat", "Hafalan Arbain", "Muamalah & Bisnis")
        return bookmarkDao.getAllFolderTags().map { tagsFromDb ->
            (defaultFolders + tagsFromDb).distinct().filter { it.isNotBlank() && it != "Semua Bookmark" }
        }
    }

    override fun isBookmarked(bookSlug: String, number: Int): Flow<Boolean> {
        val id = "${bookSlug.lowercase()}_$number"
        return bookmarkDao.isBookmarked(id)
    }

    override suspend fun addBookmark(hadith: Hadith, bookSlug: String, folderTags: List<String>) {
        val sanadFromList = hadith.sanadJalurList.firstOrNull()
            ?.mapNotNull { it.name.trim().takeIf { name -> name.isNotBlank() } }
            ?.joinToString(" ➔ ") ?: ""

        val finalSanadSnippet = if (sanadFromList.isBlank() || sanadFromList.contains("➔ , ➔")) {
            extractPerawiNames(hadith.translation).joinToString(" ➔ ")
        } else {
            sanadFromList
        }

        val officialName = BookSlugUtil.getOfficialBookName(bookSlug)
        val cleanTagsString = (listOf("Semua Bookmark") + folderTags).distinct().joinToString(",")
        val entity = BookmarkEntity(
            id = "${bookSlug.lowercase()}_${hadith.number}",
            bookSlug = bookSlug,
            number = hadith.number,
            bookName = officialName,
            kitabTitle = hadith.kitab,
            babTitle = hadith.chapter,
            arabicText = hadith.arabicText,
            translation = hadith.translation,
            grade = hadith.grade,
            sanadSnippet = finalSanadSnippet,
            foldertags = cleanTagsString
        )
        bookmarkDao.insertBookmark(entity)
    }

    override suspend fun removeBookmark(bookSlug: String, number: Int) {
        val id = "${bookSlug.lowercase()}_$number"
        bookmarkDao.deleteBookmarkById(id)
    }

    override suspend fun updateBookmarkFolders(
        bookSlug: String,
        number: Int,
        newFolders: List<String>
    ) {
        val id = "${bookSlug.lowercase()}_$number"
        val cleanTagsString = (listOf("Semua Bookmark") + newFolders).distinct().joinToString(",")
        bookmarkDao.updateBookmarkFolders(id, cleanTagsString)
    }
}