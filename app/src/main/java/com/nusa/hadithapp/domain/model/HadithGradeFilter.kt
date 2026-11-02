package com.nusa.hadithapp.domain.model

/**
 * Filter status derajat keshahihan hadits pada layar pencarian
 */
enum class HadithGradeFilter(val label: String) {
    ALL("Semua"),
    SHAHIH("Shahih"),
    HASAN("Hasan"),
    DHAIF("Dha'if")
}

/**
 * Evaluasi apakah item hadis pencarian sesuai dengan filter derajat yang dipilih.
 */
fun matchesHadithGrade(item: HadithSearchItem, filter: HadithGradeFilter): Boolean {
    val gradeText = item.grade.trim()
    val isBukhariOrMuslim = item.bookSlug.contains("bukhari", ignoreCase = true) ||
            item.bookSlug.contains("muslim", ignoreCase = true)

    return when (filter) {
        HadithGradeFilter.ALL -> true
        HadithGradeFilter.SHAHIH -> {
            gradeText.contains("Shahih", ignoreCase = true) || isBukhariOrMuslim
        }
        HadithGradeFilter.HASAN -> {
            gradeText.contains("Hasan", ignoreCase = true) && !gradeText.contains("Shahih", ignoreCase = true)
        }
        HadithGradeFilter.DHAIF -> {
            gradeText.contains("Dha'if", ignoreCase = true) ||
                    gradeText.contains("Dhaif", ignoreCase = true) ||
                    gradeText.contains("Maudhu", ignoreCase = true) ||
                    gradeText.contains("Mungkar", ignoreCase = true) ||
                    gradeText.contains("Lemah", ignoreCase = true)
        }
    }
}
