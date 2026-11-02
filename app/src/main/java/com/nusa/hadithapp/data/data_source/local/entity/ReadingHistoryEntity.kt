package com.nusa.hadithapp.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_history")
data class ReadingHistoryEntity(
    @PrimaryKey
    val id: String, // format: "${bookSlug.lowercase()}_$number"
    val bookSlug: String,
    val number: Int,
    val bookName: String,
    val kitab: String,
    val chapter: String,
    val arabicSnippet: String,
    val translationSnippet: String,
    val readAt: Long = System.currentTimeMillis()
)
