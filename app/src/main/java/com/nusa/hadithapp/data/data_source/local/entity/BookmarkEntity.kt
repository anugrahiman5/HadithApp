package com.nusa.hadithapp.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val id: String,
    val bookSlug: String,
    val number: Int,
    val bookName: String,
    val kitabTitle: String,
    val babTitle: String,
    val arabicText: String,
    val translation: String,
    val grade: String,
    val foldertags: String = "Semua Bookmark",
    val sanadSnippet: String = "",
    val userNote: String = "",
    val createdAt: Long = System.currentTimeMillis()
)