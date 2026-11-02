package com.nusa.hadithapp.util

import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.model.HadithNote

/**
 * Format hadith untuk dibagikan melalui intent Android
 * @param hadith Data hadith yang akan dibagikan
 * @param includeMetadata Jika true, tambahkan metadata seperti no hadith & bab
 * @return String terformat untuk share
 */
fun formatHadithForSharing(
    hadith: Hadith,
    includeMetadata: Boolean = true
): String {
    return buildString {
        // Metadata
        if (includeMetadata) {
            appendLine("📖 ${hadith.bookName.trim()} - No. ${hadith.number}")
            if (hadith.chapter.isNotBlank()) {
                appendLine("📌 Bab: ${hadith.chapter.trim()}")
            }
            appendLine()
        }

        // Teks Arab
        if (hadith.arabicText.isNotBlank()) {
            appendLine(hadith.arabicText.trim())
            appendLine()
        }

        // Terjemahan
        if (hadith.translation.isNotBlank()) {
            appendLine("Artinya:")
            appendLine("\"${hadith.translation.trim()}\"")
            appendLine()
        }

        // Penilaian
        appendLine("👨‍🎓 Penilaian Ulama:")
        append(formatResearchersInfo(hadith))
    }
}

/**
 * Format informasi penilaian dari peneliti/ulama
 */
private fun formatResearchersInfo(hadith: Hadith): String {
    return if (hadith.researcherList.isNotEmpty()) {
        hadith.researcherList
            .filter { it.researcher.isNotBlank() && it.grade.isNotBlank() }
            .joinToString("\n") { "• ${it.researcher.trim()}: ${it.grade.trim()}" }
            .takeIf { it.isNotBlank() } ?: "• Derajat: ${hadith.grade}"
    } else {
        "• Derajat: ${hadith.grade.takeIf { it.isNotBlank() } ?: "Tidak ditentukan"}"
    }
}

/**
 * Format hadith dalam bentuk ringkas (hanya teks Arab + terjemahan)
 */
fun formatHadithCompact(hadith: Hadith): String {
    return buildString {
        appendLine(hadith.arabicText.trim())
        appendLine()
        appendLine("Artinya: ${hadith.translation.trim()}")
        appendLine()
        appendLine("(${hadith.bookName.trim()} no. ${hadith.number})")
    }
}

/**
 * Format catatan kajian lengkap untuk dibagikan ke WhatsApp / Telegram
 */
fun formatStudyNoteForSharing(note: HadithNote): String {
    return buildString {
        appendLine("📝 CATATAN KAJIAN")
        appendLine("━━━━━━━━━━━━━━━━━━━━━")
        appendLine("📌 ${note.title.trim()}")

        if (note.categoryTag.isNotBlank()) {
            appendLine("📁 Binder: ${note.categoryTag.trim()}")
        }

        if (note.tags.isNotEmpty()) {
            val tagText = note.tags.joinToString(" ") { if (it.startsWith("#")) it else "#$it" }
            appendLine("🏷️ $tagText")
        }
        appendLine("━━━━━━━━━━━━━━━━━━━━━")
        appendLine()

        // Isi Catatan / Ulasan Pengguna
        appendLine(note.content.trim())
        appendLine()

        // Rujukan Hadits Tersemat (Jika ada)
        if (note.bookName.isNotBlank() || note.arabicSnippet.isNotBlank()) {
            appendLine("─────────────────────")
            appendLine("📖 Hadits Rujukan:")
            appendLine("${note.bookName} No. ${note.number}")

            if (note.arabicSnippet.isNotBlank()) {
                appendLine()
                appendLine(note.arabicSnippet.trim())
            }
            if (note.firstSanad.isNotBlank()) {
                appendLine()
                appendLine("🔀 ${note.firstSanad.trim()}")
            }
        }
    }
}

/**
 * Format hadits dari bookmark untuk dibagikan secara rapi
 */
fun formatBookmarkForSharing(bookmark: Bookmark): String {
    return buildString {
        appendLine("📖 ${bookmark.bookName} - No. ${bookmark.number}")
        if (bookmark.babTitle.isNotBlank()) {
            appendLine("📌 Bab: ${bookmark.babTitle.trim()}")
        }
        appendLine("🔰 Derajat: ${bookmark.grade}")
        appendLine()

        if (bookmark.arabicText.isNotBlank()) {
            appendLine(bookmark.arabicText.trim())
            appendLine()
        }

        if (bookmark.translation.isNotBlank()) {
            appendLine("Artinya:")
            appendLine("\"${bookmark.translation.trim()}\"")
            appendLine()
        }

        if (bookmark.sanadSnippet.isNotBlank()) {
            appendLine("🔀 Jalur Sanad:")
            appendLine(bookmark.sanadSnippet.trim())
        }
    }
}