package com.nusa.hadithapp.data.model

import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.model.ResearcherGrade
import com.nusa.hadithapp.domain.model.SanadPerawi
import kotlinx.serialization.Serializable

//Data Transfer Object

@Serializable
data class HadithDto(
    val id: String,
    val bookName: String,
    val number: Int,
    val kitab: String,
    val chapter: String,
    val arabicText: String,
    val translation: String,
    val grade: String,
    val narrator: String,
    val sanadJalur: List<List<PerawiProfileDto>>,
    val syarah: String,
    val researcherList: List<ResearcherGradeDto>,
    val otherNumber: String,
    val totalHadith: Int
)

// Fungsi untuk mengubah data mentah API menjadi data bersih Domain
//fun HadithDto.toDomain(): Hadith {
//    return Hadith(
//        id = this.id,
//        bookName = this.bookName,
//        number = this.number,
//        kitab = this.kitab,
//        chapter = this.chapter,
//        arabicText = this.arabicText,
//        translation = this.translation,
//        grade = this.grade,
//        narrator = this.narrator,
//        syarah = this.syarah,
//        sanadJalurList = this.sanadJalur.map { jalur ->
//            jalur.map {
//                SanadPerawi(
//                    it.kodeRawi,
//                    it.name,
//                    it.quality,
//                    it.kalangan,
//                    it.nasab,
//                    it.kuniyah,
//                    it.laqob,
//                    it.negeriHidup,
//                    it.negeriWafat,
//                    it.tahunWafat,
//                    it.rBukhari,
//                    it.rMuslim,
//                    it.rAbuDaud,
//                    it.rTirmidzi,
//                    it.rNasai,
//                    it.rIbnuMajah,
//                    it.rAhmad,
//                    it.rMalik,
//                    it.rDarimi
//                )
//            }
//        },
//        researcherList = this.researcherList.map {
//            ResearcherGrade(it.researcher, it.grade)
//        },
//        otherNumber = this.otherNumber,
//        totalHadith = totalHadith
//    )
//}
