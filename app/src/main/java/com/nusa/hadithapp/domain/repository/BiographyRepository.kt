package com.nusa.hadithapp.domain.repository

import com.nusa.hadithapp.domain.model.biography.ImamBiography
import com.nusa.hadithapp.domain.model.biography.ImamSummary
import kotlinx.coroutines.flow.Flow

interface BiographyRepository {
    fun getAllImams(): List<ImamSummary>
    fun getBiography(imamKey: String): Flow<ImamBiography>
}
