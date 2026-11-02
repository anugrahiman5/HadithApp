package com.nusa.hadithapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScholarCommentDto(
    @SerialName("Ulama")
    val ulama: String? = null,

    @SerialName("Komentar")
    val komentar: String? = null
)