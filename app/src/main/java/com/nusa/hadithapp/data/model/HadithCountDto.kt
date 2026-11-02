package com.nusa.hadithapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HadithCountDto(
    @SerialName("count(*)")
    val count: Int? = 0
)
