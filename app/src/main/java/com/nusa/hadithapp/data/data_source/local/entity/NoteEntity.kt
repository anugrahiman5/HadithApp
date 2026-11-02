package com.nusa.hadithapp.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val bookSlug: String,
    val number: Int,
    val bookName: String = "",
    val title: String = "",
    val content: String,
    val categoryTag: String = "Kajian Umum",
    val tags: String = "",
    val arabicSnippet: String = "",
    val translationSnippet: String = "",
    val firstSanad: String = "",
    val referencesJson: String = "",
    val isPinned: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)