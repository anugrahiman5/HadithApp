package com.nusa.hadithapp.domain.model.compare

import com.nusa.hadithapp.domain.model.SanadPerawi

data class ReferenceHadith(
    val bookSlug: String,
    val bookName: String,
    val number: Int,
    val arabicText: String,
    val translation: String,
    val kitabName: String = "",
    val babName: String = "",
    val grade: String = "Shahih",
    val sahabatName: String = "",
    val sanadChain: List<SanadPerawi> = emptyList(),
    val totalJalurDitemukan: Int = 0
)

data class ComparedHadith(
    val id: String,
    val bookSlug: String,
    val bookName: String,
    val number: Int,
    val arabicText: String,
    val translation: String,
    val kitabName: String = "",
    val babName: String = "",
    val grade: String = "Shahih",
    val sanadChain: List<SanadPerawi> = emptyList(),
    val similarityPercent: Int = 100,
    val differingWords: List<String> = emptyList(),
    val variantNote: String? = null
)

data class MatanMatrixRow(
    val bookLabel: String,
    val similarityStatus: String,
    val matanSnippet: String,
    val contextBab: String
)
