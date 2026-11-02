package com.nusa.hadithapp.data.mapper

import com.nusa.hadithapp.data.data_source.remote.strategy.HadithStrategyFactory
import com.nusa.hadithapp.data.model.HadithDto
import com.nusa.hadithapp.data.model.HaditsSoftRawItem
import com.nusa.hadithapp.data.model.PerawiProfileDto
import com.nusa.hadithapp.data.model.ResearcherGradeDto
import com.nusa.hadithapp.data.model.SimilarHadithDto
import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.model.ResearcherGrade
import com.nusa.hadithapp.domain.model.SanadPerawi
import com.nusa.hadithapp.domain.model.SimilarHadith
import com.nusa.hadithapp.util.BookSlugUtil

/**
 * Mapper murni untuk merakit dan mengubah data mentah API menjadi model aplikasi.
 * Seluruh logika Strategy ulama dan pembersihan string dipusatkan di sini.
 */
object HadithMapper {

    fun toDto(
        cleanSlug: String,
        remoteData: HaditsSoftRawItem,
        sanadJalur: List<List<PerawiProfileDto>>
    ): HadithDto {
        // 1. Logika Penilaian Peneliti / Ulama Hadits (Strategy Pattern)
        val listPenelitiDinamis = mutableListOf<ResearcherGradeDto>()
        val kitabStrategy = HadithStrategyFactory.getStrategy(cleanSlug.lowercase())

        if (cleanSlug.contains("ShahihBukhari", ignoreCase = true) || cleanSlug.contains("ShahihMuslim", ignoreCase = true)) {
            listPenelitiDinamis.add(ResearcherGradeDto("Ijma' Ulama", "Shahih"))
        } else {
            val namaTokoh = kitabStrategy.getResearchers()

            if (namaTokoh.first != "-" && !remoteData.Albani.isNullOrEmpty() && remoteData.Albani != "null") {
                listPenelitiDinamis.add(
                    ResearcherGradeDto(
                        researcher = namaTokoh.first,
                        grade = kitabStrategy.replaceDerajat(remoteData.Albani)
                    )
                )
            }
            if (namaTokoh.second != "-" && !remoteData.Darussalam.isNullOrEmpty() && remoteData.Darussalam != "null") {
                listPenelitiDinamis.add(
                    ResearcherGradeDto(
                        researcher = namaTokoh.second,
                        grade = kitabStrategy.replaceDerajat(remoteData.Darussalam)
                    )
                )
            }
        }

        // 2. Pembersihan & Fallback Teks
        val derajatUtama = listPenelitiDinamis.firstOrNull()?.grade ?: "Belum Ada Penilaian"
        val officialBookName = BookSlugUtil.getOfficialBookName(cleanSlug)
        val namaKitab = remoteData.kitabTitle?.firstOrNull()?.nKitab ?: "Informasi Kitab"
        val namaBab = remoteData.babTitle?.firstOrNull()?.nBab ?: "Informasi Bab"
        val totalCount = remoteData.hadithCount?.firstOrNull()?.count ?: 0
        val sahabatNarrator = sanadJalur.firstOrNull()?.firstOrNull()?.name ?: "Sahabat Nabi"

        return HadithDto(
            id = "${cleanSlug}_${remoteData.Nomer}",
            bookName = officialBookName,
            number = remoteData.Nomer,
            kitab = namaKitab,
            chapter = namaBab,
            arabicText = remoteData.Arabic ?: "",
            translation = remoteData.Indonesia ?: "Terjemahan belum tersedia.",
            grade = derajatUtama,
            narrator = sahabatNarrator,
            sanadJalur = sanadJalur,
            syarah = "Hadits dengan sanad tersambung (muttashil).",
            researcherList = listPenelitiDinamis,
            otherNumber = remoteData.otherNumber ?: "",
            totalHadith = totalCount
        )
    }

    fun toDomain(dto: HadithDto): Hadith {
        return Hadith(
            id = dto.id,
            bookName = dto.bookName,
            number = dto.number,
            kitab = dto.kitab,
            chapter = dto.chapter,
            arabicText = dto.arabicText,
            translation = dto.translation,
            grade = dto.grade,
            narrator = dto.narrator,
            syarah = dto.syarah,
            sanadJalurList = dto.sanadJalur.map { jalur ->
                jalur.map {
                    SanadPerawi(
                        it.kodeRawi, it.name, it.quality, it.kalangan, it.nasab,
                        it.kuniyah, it.laqob, it.negeriHidup, it.negeriWafat,
                        it.tahunWafat, it.rBukhari, it.rMuslim, it.rAbuDaud,
                        it.rTirmidzi, it.rNasai, it.rIbnuMajah, it.rAhmad,
                        it.rMalik, it.rDarimi
                    )
                }
            },
            researcherList = dto.researcherList.map {
                ResearcherGrade(it.researcher, it.grade)
            },
            otherNumber = dto.otherNumber,
            totalHadith = dto.totalHadith
        )
    }

    fun toDomain(dto: SimilarHadithDto): SimilarHadith {
        val canonicalSlug = BookSlugUtil.normalize(dto.name)
        val officialName = BookSlugUtil.getOfficialBookName(canonicalSlug)
        return SimilarHadith(
            bookName = officialName,
            bookSlug = canonicalSlug,
            number = dto.number.toIntOrNull() ?: 1
        )
    }
}
