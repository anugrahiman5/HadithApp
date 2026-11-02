package com.nusa.hadithapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ResearcherGradeDto(
    val researcher: String,
    val grade: String
)