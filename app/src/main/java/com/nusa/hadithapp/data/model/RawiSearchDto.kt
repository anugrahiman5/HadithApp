package com.nusa.hadithapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawiSearchDto(
    @SerialName("name") val name: String = "",
    @SerialName("kuniyah") val kuniyah: String = "",
    @SerialName("classify") val classify: String = "",
    @SerialName("level") val level: String = ""
)