package com.nusa.hadithapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HaditsSoftRawItem(
    @SerialName("Nomer") val Nomer: Int,
    @SerialName("Arabic") val Arabic: String? = null,
    @SerialName("Indonesia") val Indonesia: String? = null,
    @SerialName("Albani") val Albani: String? = null,
    @SerialName("Darussalam") val Darussalam: String? = null,
    @SerialName("otherNumber") val otherNumber: String? = null,
    @SerialName("babTitle") val babTitle: List<BabItemDto>? = null,
    @SerialName("kitabTitle") val kitabTitle: List<KitabItemDto>? = null,
    @SerialName("hadithCount") val hadithCount: List<HadithCountDto>? = null
)