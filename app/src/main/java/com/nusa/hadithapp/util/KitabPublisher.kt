package com.nusa.hadithapp.util

enum class KitabPublisher(
    val fullName: String,
    val shortName: String,
    val keys: List<String>
) {
    BUKHARI(
        fullName = "Fathul Bari:",
        shortName = "Fathul Bari:",
        keys = listOf("bukhari", "shahih bukhari", "shahihbukhari")
    ),
    MUSLIM(
        fullName = "Syarh Shahih Muslim:",
        shortName = "Syarh S. Muslim:",
        keys = listOf("muslim", "shahih muslim", "shahihmuslim")
    ),
    TIRMIDZI(
        fullName = "Maktabah Al Ma'arif:",
        shortName = "M. Al Ma'arif:",
        keys = listOf("tirmidzi", "sunan tirmidzi", "sunantirmidzi")
    ),
    ABU_DAUD(
        fullName = "Baitul Afkar Ad Dauliah:",
        shortName = "Baitul Afkar:",
        keys = listOf("abu daud", "abudaud", "sunan abu daud", "sunanabudaud")
    ),
    NASAI(
        fullName = "Maktabah Al Ma'arif:",
        shortName = "M. Al Ma'arif:",
        keys = listOf("nasa'i", "nasai", "sunan nasa'i", "sunannasai")
    ),
    IBNU_MAJAH(
        fullName = "Maktabah Al Ma'arif:",
        shortName = "M. Al Ma'arif:",
        keys = listOf("ibnu majah", "ibnumajah", "sunan ibnu majah", "sunanibnumajah")
    ),
    DARIMI(
        fullName = "Darul Mughni:",
        shortName = "Darul Mughni:",
        keys = listOf("darimi", "sunan darimi", "sunandarimi")
    ),
    AHMAD(
        fullName = "Muassasah Ar Risalah:",
        shortName = "M. Ar Risalah:",
        keys = listOf("ahmad", "musnad ahmad", "musnadahmad")
    ),
    MALIK(
        fullName = "Darul Ma'rifah:",
        shortName = "Darul Ma'rifah:",
        keys = listOf("malik", "muwatha' malik", "muwathamalik")
    ),
    DARUQUTHNI(
        fullName = "Muassasah Ar Risalah:",
        shortName = "M. Ar Risalah:",
        keys = listOf("daruquthni", "sunan daruquthni", "sunandaruquthni")
    ),
    IBNU_KHUZAIMAH(
        fullName = "-:",
        shortName = "-:",
        keys = listOf("ibnu khuzaimah", "ibnukhuzaimah", "shahih ibnu khuzaimah")
    ),
    IBNU_HIBBAN(
        fullName = "-:",
        shortName = "-:",
        keys = listOf("ibnu hibban", "ibnuhibban", "shahih ibnu hibban")
    ),
    AL_HAKIM(
        fullName = "-:",
        shortName = "-:",
        keys = listOf("al hakim", "alhakim", "al mustadrak", "almustadrak")
    ),
    SYAFII(
        fullName = "-:",
        shortName = "-:",
        keys = listOf("syafi'i", "syafii", "musnad syafi'i", "musnadsyafii")
    );

    companion object {
        fun getPublisherName(bookNameOrSlug: String, isCompact: Boolean = true): String {
            val normalizedInput = bookNameOrSlug.lowercase().trim()
            val matched = entries.find { publisher ->
                publisher.keys.any { key -> normalizedInput.contains(key) }
            }

            return if (matched != null) {
                if (isCompact) matched.shortName else matched.fullName
            } else {
                "No. Lain:"
            }
        }
    }
}