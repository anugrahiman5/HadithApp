package com.nusa.hadithapp.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_hadiths")
data class CachedHadithEntity(
    @PrimaryKey
    val id: String, // format: "${cleanSlug.lowercase()}_${number}"
    val bookSlug: String,
    val number: Int,
    val bookName: String,
    val kitab: String,
    val chapter: String,
    val arabicText: String,
    val translation: String,
    val grade: String,
    val narrator: String,
    val syarah: String = "",
    val otherNumber: String = "",
    val totalHadith: Int = 0,
    val cachedAt: Long = System.currentTimeMillis()
)
