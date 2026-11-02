package com.nusa.hadithapp.util

object SearchSnippetHelper {

    // Default constants
    private const val DEFAULT_MAX_WORDS = 12
    private const val DEFAULT_SNIPPET_LENGTH = 150
    private const val DEFAULT_RADIUS = 80
    private const val ELLIPSIS = "..."

    /**
     * Memotong teks Arab agar lebih ringkas dengan batasan jumlah kata
     * @param arabicText Teks Arab asli
     * @param maxWords Maksimal jumlah kata (default: 12)
     * @return Teks yang diringkas atau teks original jika lebih pendek
     */
    fun summarizeArabic(arabicText: String, maxWords: Int = DEFAULT_MAX_WORDS): String {
        if (arabicText.isBlank()) return ""

        val trimmedText = arabicText.trim()
        val words = trimmedText.split(Regex("\\s+"))

        if (words.size <= maxWords) return trimmedText

        return words.take(maxWords).joinToString(" ") + " $ELLIPSIS"
    }

    /**
     * Ambil cuplikan teks terjemahan di sekitar keyword pencarian
     * @param fullText Teks lengkap terjemahan
     * @param keyword Kata kunci pencarian
     * @param radius Jarak karakter ke kiri/kanan keyword (default: 80)
     * @param maxFallbackLength Panjang maksimal fallback jika keyword tidak ditemukan (default: 150)
     * @return Snippet dengan context atau fallback
     */
    fun getSnippetTranslation(
        fullText: String,
        keyword: String,
        radius: Int = DEFAULT_RADIUS,
        maxFallbackLength: Int = DEFAULT_SNIPPET_LENGTH
    ): String {
        if (fullText.isBlank()) return ""

        val trimmedKeyword = keyword.trim()
        if (trimmedKeyword.isBlank()) {
            return truncateWithEllipsis(fullText, maxFallbackLength)
        }

        val lowerText = fullText.lowercase()
        // Pecah kata kunci menjadi per-kata (misal: "niat amalan" -> ["niat", "amalan"])
        val words = keyword.trim().lowercase().split("\\s+".toRegex()).filter { it.isNotBlank() }

        //Cari kata mana yang pertama kali ditemukan di dalam teks hadits
        var bestIndex = -1
        var matchedKeywordLength = 0

        for (w in words) {
            val idx = lowerText.indexOf(w)
            if (idx != -1 && (bestIndex == -1 || idx < bestIndex)) {
                bestIndex = idx
                matchedKeywordLength = w.length
            }
        }

        // Jika tidak ada satu pun kata kunci yang ditemukan di teks
        if (bestIndex == -1) {
            val matanOnly = extractMatanOnly(fullText)
            return truncateWithEllipsis(matanOnly, maxFallbackLength)
        }

        // Potong teks tepat di sekitar kata kunci yang ditemukan (Radius 60 karakter ke kiri & kanan)
        val start = maxOf(0, bestIndex - radius)
        val end = minOf(fullText.length, bestIndex + matchedKeywordLength + radius)

        var snippet = fullText.substring(start, end).trim()

        // Tambahkan ellipsis jika terpotong
        if (start > 0) snippet = "$ELLIPSIS $snippet"
        if (end < fullText.length) snippet = "$snippet $ELLIPSIS"

        return snippet
    }

    /**
     * Truncate teks dengan ellipsis di akhir
     * @param text Teks yang akan dipotong
     * @param maxLength Panjang maksimal
     * @return Teks yang dipotong dengan ellipsis
     */
    private fun truncateWithEllipsis(text: String, maxLength: Int): String {
        if (text.length <= maxLength) return text
        return text.take(maxLength).trimEnd() + " $ELLIPSIS"
    }

    /**
     * Cari keyword dengan case-insensitive dan return indeks
     * @param text Teks yang akan dicari
     * @param keyword Kata kunci
     * @return Indeks kemunculan pertama atau -1 jika tidak ditemukan
     */
    fun findKeywordIndex(text: String, keyword: String): Int {
        return text.lowercase().indexOf(keyword.lowercase().trim())
    }

    /**
     * Highlight keyword dalam teks (membuat AnnotatedString untuk Compose)
     * Note: Untuk keperluan visual di Jetpack Compose
     */
    fun getHighlightRanges(text: String, keyword: String): List<IntRange> {
        if (keyword.isBlank()) return emptyList()

        val lowerText = text.lowercase()
        val lowerKeyword = keyword.lowercase().trim()
        val ranges = mutableListOf<IntRange>()
        var startIndex = 0

        while (true) {
            val index = lowerText.indexOf(lowerKeyword, startIndex)
            if (index == -1) break
            ranges.add(index until (index + lowerKeyword.length))
            startIndex = index + lowerKeyword.length
        }

        return ranges
    }
}