package com.nusa.hadithapp.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class SearchRequestDto(
    val keyword: List<String>,
    val books: List<String>
)

@Serializable
data class SearchResponseDto(
    val results: Map<String, BookSearchResultDto>?,
    val total: Int = 0
)
@Serializable
data class BookSearchResultDto(
    val count: Int = 0,
    val rows: List<JsonObject>? = null
)