package com.nusa.hadithapp.data.repository

import com.nusa.hadithapp.data.data_source.local.catalog.ImamBioCatalog
import com.nusa.hadithapp.data.data_source.remote.HadithApiService
import com.nusa.hadithapp.data.mapper.BiographyTextParser
import com.nusa.hadithapp.domain.model.biography.ImamBiography
import com.nusa.hadithapp.domain.model.biography.ImamSummary
import com.nusa.hadithapp.domain.repository.BiographyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiographyRepositoryImpl @Inject constructor(
    private val apiService: HadithApiService
) : BiographyRepository {

    private val apiSupportedImams = setOf(
        "Bukhari", "Muslim", "Abu Daud", "Tirmidzi", "Nasai",
        "Ibnu Majah", "Ahmad", "Malik", "Darimi", "Daruquthni"
    )

    override fun getAllImams(): List<ImamSummary> {
        return ImamBioCatalog.ALL_IMAMS
    }

    override fun getBiography(imamKey: String): Flow<ImamBiography> = flow {
        // 1. Emit versi lokal catalog seketika (Zero latency, offline-ready)
        val baseBio = ImamBioCatalog.getImam(imamKey)
        emit(baseBio)

        // 2. Fetch narasi mendalam dari API secara asynchronous jika imam tersedia di API backend
        if (baseBio.id in apiSupportedImams) {
            try {
                val rawText = apiService.getBiographyRawText(baseBio.id)
                if (!rawText.isNullOrBlank()) {
                    val parsed = BiographyTextParser.parse(rawText)
                    val enrichedBio = baseBio.copy(
                        apiRawText = rawText,
                        parsedTeachers = parsed.teachers,
                        parsedStudents = parsed.students,
                        parsedSummaryParagraphs = if (parsed.summaryParagraphs.isNotEmpty()) {
                            parsed.summaryParagraphs
                        } else {
                            baseBio.parsedSummaryParagraphs
                        }
                    )
                    emit(enrichedBio)
                }
            } catch (_: Exception) {
                // Abaikan kegagalan jaringan, baseBio lokal sudah ter-emit dengan sempurna
            }
        }
    }
}
