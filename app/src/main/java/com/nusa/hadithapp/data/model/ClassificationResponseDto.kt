package com.nusa.hadithapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassificationResponseDto(
    @SerialName("Nomer") val nomer: String?,
    @SerialName("Arabic") val arabic: String?,
    @SerialName("Indonesia") val indonesia: String?,
    @SerialName("otherNumber") val otherNumber: String?,
    @SerialName("kitabTitle") val kitabTitle: List<KitabItemDto>?,
    @SerialName("babTitle") val babTitle: List<BabItemDto>?
)