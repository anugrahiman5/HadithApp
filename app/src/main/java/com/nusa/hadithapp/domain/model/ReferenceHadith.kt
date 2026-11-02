package com.nusa.hadithapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReferencedHadith(
    val bookSlug: String,
    val number: Int,
    val bookName: String,
    val grade: String = "Shahih",
    val arabicText: String = "",
    val translation: String = "",
    val firstSanad: String = ""
)