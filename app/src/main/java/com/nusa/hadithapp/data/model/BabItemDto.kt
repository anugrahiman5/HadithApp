package com.nusa.hadithapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BabItemDto(
    @SerialName("NBab") val nBab: String? = null,
    @SerialName("VMemberBab") val vMemberBab: String? = null,
    @SerialName("AwalanBab") val awalanBab: String? = null
)
