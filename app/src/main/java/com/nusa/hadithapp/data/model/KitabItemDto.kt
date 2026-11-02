package com.nusa.hadithapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KitabItemDto(
    @SerialName("NKitab") val nKitab: String? = null,
    @SerialName("VMember") val vMember: String? = null,
    @SerialName("Awalan") val awalan: String? =null
)
