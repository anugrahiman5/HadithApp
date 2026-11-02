package com.nusa.hadithapp.domain.model

data class RawiSearchItem(
    val kodeRawi: Int,
    val name: String,
    val kalangan: String? = null,
    val quality: Int
)