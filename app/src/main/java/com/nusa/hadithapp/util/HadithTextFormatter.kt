package com.nusa.hadithapp.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

data class HadithTextColor(
    val squareBracketColor: Color,      // [Nama Perawi]
    val curlyBracketColor: Color,       // {Teks Qur'an}
    val searchHighlightBg: Color,       // Background stabilo
    val searchHighlightText: Color      // Teks dalam stabilo
)

object HadithTextDefaults {
    @Composable
    fun colors(isDarkTheme: Boolean = isSystemInDarkTheme()): HadithTextColor {
        return if (isDarkTheme) {
            HadithTextColor(
                squareBracketColor = Color(0xFF6980F6),    // Pink Coral terang
                curlyBracketColor = Color(0xFF81C784),     // Hijau Mint
                searchHighlightBg = Color(0xFFFFD54F),     // Amber terang
                searchHighlightText = Color(0xFF121212)    // Teks gelap
            )
        } else {
            HadithTextColor(
                squareBracketColor = Color(0xFF1B24D8),    // Pink pekat
                curlyBracketColor = Color(0xFF2E7D32),     // Hijau tua
                searchHighlightBg = Color(0xFFFFE082),     // Kuning lembut
                searchHighlightText = Color.Black
            )
        }
    }
}

/**
 * Format hadith text dengan color annotations untuk bracket dan search keywords
 * @param text Teks hadith asli
 * @param keywords Kata kunci pencarian (untuk highlight)
 * @param isSearchMode Enable/disable highlight pencarian
 * @param colors Palet warna untuk annotations
 * @return AnnotatedString dengan styling applied
 */
fun formatHadithText(
    text: String,
    keywords: List<String> = emptyList(),
    isSearchMode: Boolean = false,
    highlightSanad: Boolean = true,
    colors: HadithTextColor
): AnnotatedString {
    if (text.isBlank()) return AnnotatedString("")

    return buildAnnotatedString {
        val bracketRegex = Regex("""(\[.*?\])|(\{.*?\})""")
        var lastIndex = 0

        bracketRegex.findAll(text).forEach { matchResult ->
            val matchRange = matchResult.range
            val matchedValue = matchResult.value

            // Append teks biasa sebelum bracket
            if (matchRange.first > lastIndex) {
                val plainText = text.substring(lastIndex, matchRange.first)
                appendWithHighlight(
                    text = plainText,
                    keywords = if (isSearchMode) keywords else emptyList(),
                    colors = colors
                )
            }

            // Process bracket content
            when {
                matchedValue.startsWith("[") -> {
                    // [Nama Perawi]
                    val innerText = matchedValue.removeSurrounding("[", "]")
                    pushStringAnnotation("PERAWI_NAME", innerText)
                    val spanStyle = if (highlightSanad) {
                        SpanStyle(color = colors.squareBracketColor, fontWeight = FontWeight.SemiBold)
                    } else {
                        SpanStyle(fontWeight = FontWeight.Normal)
                    }
                    withStyle(spanStyle) {
                        appendWithHighlight(
                            text = innerText,
                            keywords = if (isSearchMode) keywords else emptyList(),
                            colors = colors
                        )
                    }
                    pop()
                }
                matchedValue.startsWith("{") -> {
                    // {Teks Qur'an}
                    val innerText = matchedValue.removeSurrounding("{", "}")
                    withStyle(SpanStyle(color = colors.curlyBracketColor)) {
                        appendWithHighlight(
                            text = innerText,
                            keywords = if (isSearchMode) keywords else emptyList(),
                            colors = colors
                        )
                    }
                }
            }

            lastIndex = matchRange.last + 1
        }

        // Append remaining text after last bracket
        if (lastIndex < text.length) {
            appendWithHighlight(
                text = text.substring(lastIndex),
                keywords = if (isSearchMode) keywords else emptyList(),
                colors = colors
            )
        }
    }
}

/**
 * Extension function untuk append text dengan keyword highlight
 * @param text Teks yang akan ditambahkan
 * @param keywords Keywords untuk di-highlight (case-insensitive)
 * @param colors Color scheme
 */
private fun AnnotatedString.Builder.appendWithHighlight(
    text: String,
    keywords: List<String>,
    colors: HadithTextColor
) {
    if (text.isBlank()) return

    val validKeywords = keywords
        .filter { it.isNotBlank() }
        .map { it.trim() }

    if (validKeywords.isEmpty()) {
        append(text)
        return
    }

    // Build regex pattern dari keywords (escape special chars)
    val pattern = validKeywords
        .map { Regex.escape(it) }
        .joinToString("|")

    val regex = Regex(pattern, RegexOption.IGNORE_CASE)
    var lastIdx = 0

    regex.findAll(text).forEach { match ->
        // Append text sebelum keyword
        if (match.range.first > lastIdx) {
            append(text.substring(lastIdx, match.range.first))
        }

        // Append keyword dengan highlight
        withStyle(
            SpanStyle(
                background = colors.searchHighlightBg,
                color = colors.searchHighlightText
            )
        ) {
            append(text.substring(match.range))
        }

        lastIdx = match.range.last + 1
    }

    // Append remaining text after last keyword
    if (lastIdx < text.length) {
        append(text.substring(lastIdx))
    }
}

/**
 * Utility untuk extract perawi names dari text
 */
fun extractPerawiNames(text: String): List<String> {
    val bracketRegex = Regex("""\[([^\]]+)\]""")
    return bracketRegex.findAll(text)
        .map { it.groupValues[1] }
        .toList()
}

/**
 * Utility untuk extract Qur'an references dari text
 */
fun extractQuranReferences(text: String): List<String> {
    val bracketRegex = Regex("""\{([^}]+)\}""")
    return bracketRegex.findAll(text)
        .map { it.groupValues[1] }
        .toList()
}

/**
 * Mengubah teks markdown sederhana (**teks**, *teks*, ### heading)
 * menjadi AnnotatedString di Compose agar tampil tebal/miring visual.
 */
fun formatMarkdownContent(rawText: String): AnnotatedString {
    if (rawText.isBlank()) return AnnotatedString("")

    return buildAnnotatedString {
        val lines = rawText.split("\n")
        lines.forEachIndexed { index, line ->
            var remaining = line
            var isHeading = false

            // Cek jika baris adalah Heading (### )
            if (remaining.startsWith("### ")) {
                isHeading = true
                remaining = remaining.removePrefix("### ")
            }

            if (isHeading) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)) {
                    append(remaining)
                }
            } else {
                // Parsing bold (**teks**) dan italic (*teks*)
                val regex = Regex("""(\*\*.*?\*\*)|(\*.*?\*)""")
                var lastIdx = 0

                regex.findAll(remaining).forEach { match ->
                    if (match.range.first > lastIdx) {
                        append(remaining.substring(lastIdx, match.range.first))
                    }

                    val token = match.value
                    if (token.startsWith("**") && token.endsWith("**")) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(token.removeSurrounding("**"))
                        }
                    } else if (token.startsWith("*") && token.endsWith("*")) {
                        withStyle(SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) {
                            append(token.removeSurrounding("*"))
                        }
                    }
                    lastIdx = match.range.last + 1
                }

                if (lastIdx < remaining.length) {
                    append(remaining.substring(lastIdx))
                }
            }

            if (index < lines.lastIndex) {
                append("\n")
            }
        }
    }
}

/**
 * Mengekstrak hanya redaksi Matan (sabda/inti hadits) bahasa Indonesia,
 * membuang awalan nama kitab dan rentetan nama perawi/sanad.
 */
fun extractMatanOnly(translation: String): String {
    // 0. Bersihkan awalan nama kitab & nomor hadits seperti "Muwatha' Malik 838: " atau "Shahih Bukhari 1: "
    val cleaned = translation
        .replace(Regex("""^[A-Za-z\s'’\-]+(?:\d+|\b)\s*:\s*"""), "")
        .trim()

    // 1. Pola 1: Jika dipisahkan oleh enter ganda (standar pemisah sanad & matan di API)
    val paragraphs = cleaned.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }
    if (paragraphs.size >= 2) {
        val candidateMatan = paragraphs.drop(1).joinToString("\n\n")
        if (candidateMatan.length >= 15) return sanitizeMatanQuotes(candidateMatan)
    }

    // 2. Pola 2: Penanda transisi sanad ke sabda/matan
    val markers = listOf(
        "shallallahu 'alaihi wasallam bersabda:",
        "shallallahu 'alaihi wa sallam bersabda:",
        "Rasulullah shallallahu 'alaihi wasallam bersabda",
        "Nabi shallallahu 'alaihi wasallam bersabda",
        "bahwasanya beliau bersabda:",
        "beliau bersabda:",
        "bersabda:",
        "Nabi shallallahu 'alaihi wasallam pernah",
        "Rasulullah shallallahu 'alaihi wasallam pernah",
        "bahwasanya Rasulullah",
        "berkata:"
    )
    for (marker in markers) {
        val index = cleaned.indexOf(marker, ignoreCase = true)
        if (index != -1) {
            val after = cleaned.substring(index + marker.length).trim()
            if (after.length >= 15) {
                return sanitizeMatanQuotes(after)
            }
        }
    }

    // 3. Pola 3: Jika ada tanda kutip pembuka ucapan
    val quoteIndex = cleaned.indexOf("\"")
    if (quoteIndex != -1 && quoteIndex < cleaned.length - 15) {
        val afterQuote = cleaned.substring(quoteIndex).trim()
        val closedQuote = afterQuote.lastIndexOf("\"")
        if (closedQuote > 0) {
            val inside = afterQuote.substring(1, closedQuote).trim()
            if (inside.length >= 15) return inside
        }
    }

    // 4. Pola 4: Ambil setelah titik dua pertama
    val colonIndex = cleaned.indexOf(":")
    if (colonIndex != -1 && colonIndex < cleaned.length - 20) {
        val after = cleaned.substring(colonIndex + 1).trim()
        if (after.length >= 20) {
            return sanitizeMatanQuotes(after)
        }
    }

    return sanitizeMatanQuotes(cleaned)
}

/**
 * Mengekstrak hanya redaksi Matan (sabda/inti hadits) bahasa Arab,
 * membuang awalan nama kitab (misal "موطأ مالك 838 : ") dan silsilah sanad perawi.
 */
fun extractArabicMatan(arabicText: String): String {
    // 0. Hapus awalan nama kitab dan nomor seperti "موطأ مالك 838 : "
    val cleaned = arabicText
        .replace(Regex("""^([^\d:]+)\s*\d+\s*:\s*"""), "")
        .replace(Regex("""^(صحيح|سنن|جامع|مسند|موطأ)\s+[^:]+:\s*"""), "")
        .trim()

    // 1. Pola 1: Jika dipisahkan oleh baris baru
    val paragraphs = cleaned.split("\n").map { it.trim() }.filter { it.isNotBlank() }
    if (paragraphs.size >= 2) {
        val candidate = paragraphs.last()
        if (candidate.length >= 15) return candidate
    }

    // 2. Pola 2: Deteksi batas sanad melalui shalawat Nabi dalam teks Arab ternormalisasi
    val norm = ArabicDiffUtil.normalizeArabic(cleaned)
    val sallallahu = "صلي الله عليه وسلم"
    val pos = norm.indexOf(sallallahu)
    if (pos != -1) {
        val afterNorm = norm.substring(pos + sallallahu.length).trim()
        val wordsNorm = afterNorm.split(Regex("\\s+")).filter { it.isNotBlank() }
        if (wordsNorm.isNotEmpty()) {
            var firstW = wordsNorm[0]
            if ((firstW == "قال" || firstW == "يقول" || firstW == "انه قال") && wordsNorm.size > 1) {
                firstW = wordsNorm[1]
            }

            val cleanedWords = cleaned.split(Regex("\\s+"))
            val startThreshold = (cleanedWords.size / 4).coerceAtLeast(1)
            for (i in startThreshold until cleanedWords.size) {
                if (ArabicDiffUtil.normalizeArabic(cleanedWords[i]) == firstW) {
                    val matan = cleanedWords.subList(i, cleanedWords.size).joinToString(" ").trim()
                    if (matan.length >= 12) return matan
                }
            }
        }
    }

    // 3. Pola 3: Cek titik dua setelah sanad
    val colonIndex = cleaned.indexOf(":")
    if (colonIndex != -1 && cleaned.length - colonIndex > 15) {
        val after = cleaned.substring(colonIndex + 1).trim()
        if (after.length >= 15) return after
    }

    return cleaned
}

private fun sanitizeMatanQuotes(text: String): String {
    return text
        .removePrefix("\"")
        .removeSuffix("\"")
        .removePrefix("“")
        .removeSuffix("”")
        .removePrefix(":")
        .trim()
}