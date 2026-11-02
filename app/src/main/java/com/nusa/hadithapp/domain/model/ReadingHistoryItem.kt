package com.nusa.hadithapp.domain.model

data class ReadingHistoryItem(
    val bookSlug: String,
    val number: Int,
    val bookName: String,
    val kitab: String,
    val chapter: String,
    val arabicSnippet: String,
    val translationSnippet: String,
    val readAt: Long = System.currentTimeMillis()
)

data class BookReadingProgress(
    val bookSlug: String,
    val bookName: String,
    val readCount: Int,
    val totalHadith: Int,
    val latestHadiths: List<ReadingHistoryItem> = emptyList()
) {
    val progressPercent: Float
        get() = if (totalHadith > 0) (readCount.toFloat() / totalHadith.toFloat()).coerceIn(0f, 1f) else 0f
}
