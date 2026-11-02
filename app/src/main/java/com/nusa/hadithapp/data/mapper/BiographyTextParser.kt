package com.nusa.hadithapp.data.mapper

object BiographyTextParser {

    data class ParsedBioContent(
        val teachers: List<String>,
        val students: List<String>,
        val summaryParagraphs: List<String>
    )

    fun parse(rawText: String?): ParsedBioContent {
        if (rawText.isNullOrBlank()) {
            return ParsedBioContent(emptyList(), emptyList(), emptyList())
        }

        val teachers = mutableListOf<String>()
        val students = mutableListOf<String>()
        val summaryParagraphs = mutableListOf<String>()

        val lines = rawText.split("\n")
        var currentSection = SectionType.SUMMARY

        for (rawLine in lines) {
            val line = rawLine.trim()
            if (line.isBlank()) continue

            val lineLower = line.lowercase()

            // Trigger perpindahan seksi
            when {
                lineLower.contains("guru") && (lineLower.contains("beliau") || line.length < 35) -> {
                    currentSection = SectionType.TEACHERS
                    continue
                }
                lineLower.contains("murid") && (lineLower.contains("beliau") || line.length < 35) -> {
                    currentSection = SectionType.STUDENTS
                    continue
                }
                lineLower.contains("karya") || lineLower.contains("karakter") ||
                lineLower.contains("wafat") || lineLower.contains("persaksian") -> {
                    currentSection = SectionType.OTHER
                }
            }

            when (currentSection) {
                SectionType.SUMMARY -> {
                    // Kumpulkan paragraf ringkasan pembuka (maksimal 6 paragraf)
                    if (summaryParagraphs.size < 6 && line.length > 15 && !line.startsWith("-")) {
                        summaryParagraphs.add(line)
                    }
                }
                SectionType.TEACHERS -> {
                    if (line.startsWith("-") || line.matches(Regex("""^\d+[\.\)]\s*.*"""))) {
                        val cleanName = line.replace(Regex("""^[-–—\d\.\)\s]+"""), "").trim()
                        if (cleanName.length > 2 && !teachers.contains(cleanName)) {
                            teachers.add(cleanName)
                        }
                    }
                }
                SectionType.STUDENTS -> {
                    if (line.startsWith("-") || line.matches(Regex("""^\d+[\.\)]\s*.*"""))) {
                        val cleanName = line.replace(Regex("""^[-–—\d\.\)\s]+"""), "").trim()
                        if (cleanName.length > 2 && !students.contains(cleanName)) {
                            students.add(cleanName)
                        }
                    }
                }
                SectionType.OTHER -> {
                    // Do nothing for other sections in bullet form
                }
            }
        }

        return ParsedBioContent(
            teachers = teachers,
            students = students,
            summaryParagraphs = summaryParagraphs
        )
    }

    private enum class SectionType {
        SUMMARY,
        TEACHERS,
        STUDENTS,
        OTHER
    }
}
