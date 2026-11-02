package com.nusa.hadithapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawiListItemDto(
    @SerialName("KodeRawi") val kodeRawi: String? = null,
    @SerialName("Nama") val nama: String? = null,
    @SerialName("Quality") val quality: String? = null
)