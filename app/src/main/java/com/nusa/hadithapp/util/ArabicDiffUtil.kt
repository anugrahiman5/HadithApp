package com.nusa.hadithapp.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

object ArabicDiffUtil {

    private val HARAKAT_REGEX = Regex("[\u064B-\u065F\u0670]")
    private val ALIF_REGEX = Regex("[إأآ]")
    private val LEADING_PREFIX_REGEX = Regex("^(صحيح|سنن|جامع|مسند|موطأ)\\s+[^:]+:\\s*")

    /**
     * Membersihkan teks Arab dari harakat dan menormalkan alif untuk perbandingan kata
     */
    fun normalizeArabic(text: String): String {
        return text
            .replace(LEADING_PREFIX_REGEX, "")
            .replace(HARAKAT_REGEX, "")
            .replace(ALIF_REGEX, "ا")
            .replace("ى", "ي")
            .replace(Regex("[.,:;\"'()\\[\\]\\-–—«»]"), "")
            .trim()
    }

    /**
     * Mempersingkat nama perawi yang membawa silsilah nasab terlalu panjang
     * Contoh: "Anas bin Malik bin An Nadlir bin Dlamdlom..." -> "Anas bin Malik"
     */
    fun formatRawiShortName(fullName: String): String {
        val clean = fullName.trim()
        if (clean.isBlank()) return "Sahabat Nabi"
        val parts = clean.split(Regex("\\s+(bin|binti|ibn|ibnu)\\s+", RegexOption.IGNORE_CASE))
        return if (parts.size >= 2) {
            val connector = if (clean.contains(" binti ", ignoreCase = true)) "binti" else "bin"
            "${parts[0].trim()} $connector ${parts[1].trim()}"
        } else {
            clean.take(24)
        }
    }

    /**
     * Memotong awalan sanad agar fokus mengekstrak dan membandingkan redaksi Matan (sabda/kisah)
     */
    fun extractMatan(arabicFull: String): String {
        val cleaned = arabicFull.replace(LEADING_PREFIX_REGEX, "").trim()
        val norm = normalizeArabic(cleaned)

        // Penanda batas sanad -> matan (paling umum setelah shalawat 'وسلم' atau 'صلى الله عليه وسلم')
        val sallallahu = "صلي الله عليه وسلم"
        val posSallallahu = norm.indexOf(sallallahu)
        if (posSallallahu != -1) {
            val after = norm.substring(posSallallahu + sallallahu.length).trim()
            val words = after.split(Regex("\\s+")).filter { it.isNotBlank() }
            if (words.isNotEmpty()) {
                val firstWordNorm = words[0]
                val cleanedWords = cleaned.split(Regex("\\s+"))
                val startThreshold = (cleanedWords.size / 5).coerceAtLeast(1)
                for (i in startThreshold until cleanedWords.size) {
                    if (normalizeArabic(cleanedWords[i]) == firstWordNorm) {
                        return cleanedWords.subList(i, cleanedWords.size).joinToString(" ")
                    }
                }
            }
        }

        // Penanda sekunder jika tidak ada shalawat
        val markers = listOf("قال رسول الله", "ان رسول الله", "سمعت رسول الله", "عن النبي")
        for (m in markers) {
            val p = norm.indexOf(m)
            if (p != -1) {
                val words = norm.substring(p).split(Regex("\\s+")).filter { it.isNotBlank() }
                if (words.isNotEmpty()) {
                    val firstW = words[0]
                    val cleanedWords = cleaned.split(Regex("\\s+"))
                    val startThreshold = (cleanedWords.size / 5).coerceAtLeast(1)
                    for (i in startThreshold until cleanedWords.size) {
                        if (normalizeArabic(cleanedWords[i]) == firstW) {
                            return cleanedWords.subList(i, cleanedWords.size).joinToString(" ")
                        }
                    }
                }
            }
        }

        return cleaned
    }

    /**
     * Menghitung persentase kemiripan kata dan mendeteksi kata-kata yang berbeda pada Matan
     */
    fun analyzeSimilarity(baseArabic: String, targetArabic: String): Pair<Int, List<String>> {
        val baseMatan = extractMatan(baseArabic)
        val targetMatan = extractMatan(targetArabic)

        val baseNorm = normalizeArabic(baseMatan)
        val targetNorm = normalizeArabic(targetMatan)

        val baseWords = baseNorm.split(Regex("\\s+")).filter { it.isNotBlank() }
        val targetWords = targetNorm.split(Regex("\\s+")).filter { it.isNotBlank() }

        if (targetWords.isEmpty() || baseWords.isEmpty()) return Pair(100, emptyList())

        // Jika matan identik tanpa harakat
        if (baseNorm == targetNorm) {
            return Pair(100, emptyList())
        }

        val differingWords = mutableListOf<String>()
        val baseSet = baseWords.toSet()

        targetWords.forEachIndexed { index, word ->
            val isNearbyMatch = (-3..3).any { offset ->
                val bIdx = index + offset
                bIdx in baseWords.indices && baseWords[bIdx] == word
            }
            if (!isNearbyMatch && !baseSet.contains(word)) {
                differingWords.add(word)
            }
        }

        val total = targetWords.size
        val matches = maxOf(0, total - differingWords.size)
        val similarity = (matches.toFloat() / total * 100).toInt().coerceIn(60, 100)

        return Pair(similarity, differingWords)
    }

    /**
     * Membuat AnnotatedString dengan highlight warna pada kata-kata yang berbeda di bagian Matan.
     * Kata-kata pada jalur Sanad sengaja TIDAK disorot agar fokus komparasi tetap pada Matan.
     */
    fun buildHighlightedArabicText(
        targetArabic: String,
        baseArabic: String,
        highlightBg: Color,
        highlightText: Color,
        isHighlightEnabled: Boolean
    ): AnnotatedString {
        if (!isHighlightEnabled) {
            return AnnotatedString(targetArabic)
        }

        val targetMatan = extractMatan(targetArabic)
        val baseMatan = extractMatan(baseArabic)

        val (_, differingCleanWords) = analyzeSimilarity(baseMatan, targetMatan)
        val differingSet = differingCleanWords.toSet()

        if (differingSet.isEmpty()) {
            return AnnotatedString(targetArabic)
        }

        // Tentukan titik awal matan di dalam targetArabic agar bagian sanad tidak tersorot
        val matanStartIndex = if (targetMatan != targetArabic && targetMatan.isNotBlank()) {
            val idx = targetArabic.indexOf(targetMatan)
            if (idx != -1) idx else 0
        } else {
            0
        }

        val rawWords = targetArabic.split(" ")
        var currentOffset = 0

        return buildAnnotatedString {
            rawWords.forEachIndexed { index, word ->
                val wordStartInTarget = currentOffset
                val isInsideMatan = wordStartInTarget >= matanStartIndex
                val cleanWord = normalizeArabic(word)
                val isDiff = isInsideMatan && differingSet.contains(cleanWord)

                if (isDiff) {
                    pushStyle(
                        SpanStyle(
                            background = highlightBg,
                            color = highlightText,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    append(word)
                    pop()
                } else {
                    append(word)
                }

                if (index < rawWords.size - 1) {
                    append(" ")
                }
                currentOffset += word.length + 1
            }
        }
    }

    /**
     * Konversi kode numerik derajat keshahihan hadis dari database/API haditssoft
     * ke nama derajat yang baku dan mudah dipahami.
     */
    fun formatGrade(rawGrade: String?): String {
        if (rawGrade.isNullOrBlank()) return "Shahih"
        return when (rawGrade.trim()) {
            "1" -> "Shahih"
            "2" -> "Shahihul Isnad"
            "3" -> "Hasan Shahih"
            "4" -> "Hasan"
            "5" -> "Hasanul Isnad"
            "6" -> "Dha'if"
            "7" -> "Dha'iful Isnad"
            "8" -> "Dha'if Jiddan"
            "9" -> "Munkar"
            "10" -> "Maudhu'"
            "11" -> "Syadz"
            "12" -> "Shahih Bima Qablahu"
            "13" -> "Hasan Bima Qablahu"
            "null", "-" -> "Shahih"
            else -> rawGrade.trim()
        }
    }
}
