package com.nusa.hadithapp.domain.repository

import com.nusa.hadithapp.domain.model.BabItem
import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.model.HadithCategoryItem
import com.nusa.hadithapp.domain.model.HadithSearchItem
import com.nusa.hadithapp.domain.model.HadithThemeItem
import com.nusa.hadithapp.domain.model.KitabItem
import com.nusa.hadithapp.domain.model.PerawiProfile
import com.nusa.hadithapp.domain.model.RawiSearchItem
import com.nusa.hadithapp.domain.model.ScholarComment
import com.nusa.hadithapp.domain.model.SearchResult
import com.nusa.hadithapp.domain.model.SimilarHadith
import kotlinx.coroutines.flow.Flow

interface HadithRepository {

    suspend fun getHadithByBook(bookSlug: String, number: Int): Hadith

    suspend fun getPerawiProfile(kodeRawi: Int): PerawiProfile?

    suspend fun getScholarComments(kodeRawi: Int): List<ScholarComment>

    suspend fun getAllBooks(bookSlug: String): List<KitabItem>

    suspend fun getAllChapters(bookSlug: String, start: Int, end: Int): List<BabItem>

    suspend fun getHadithThemeBatch(
        themeName: String,
        startVMember: Int,
        endVMember: Int
    ): List<HadithThemeItem>

    suspend fun getHadithByThemeVMember(themeName: String, vMember: Int): Hadith

    suspend fun getHadithCategoryBatch(
        categoryName: String,
        startVMember: Int,
        endVMember: Int
    ): List<HadithCategoryItem>

    suspend fun getHadithByCategoryVMember(categoryName: String, vMember: Int): Hadith

    fun searchHadits(keyword: String, books: List<String>): Flow<SearchResult>

    suspend fun getHadithBatchDetails(bookSlug: String, numbers: List<Int>): List<HadithSearchItem>

    suspend fun searchRawi(
        name: String = "",
        kuniyah: String = "",
        classify: String = "",
        level: String = ""
    ): List<RawiSearchItem>

    suspend fun getSimilarHadith(bookSlug: String, number: Int): List<SimilarHadith>

    suspend fun getPerawiTotalHadith(bookSlug: String, kodeRawi: Int): List<Int>
}
