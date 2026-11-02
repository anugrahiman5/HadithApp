package com.nusa.hadithapp.domain.model

data class Hadith(
    val id: String,
    val bookName: String,
    val number: Int,
    val kitab: String,
    val chapter: String,
    val arabicText: String,
    val translation: String,
    val grade: String,
    val narrator: String,
    val syarah: String,
    val sanadJalurList: List<List<SanadPerawi>>,
    val researcherList: List<ResearcherGrade>,
    val otherNumber: String,
    val totalHadith: Int
)

