package com.nusa.hadithapp.data.data_source.remote.config

object HadithClassificationMetadata {
    val targetBooks = listOf(
        "ShahihBukhari",
        "ShahihMuslim",
        "SunanTirmidzi",
        "SunanAbuDaud",
        "SunanNasai",
        "SunanIbnuMajah",
        "SunanDarimi",
        "MusnadAhmad",
        "MuwathaMalik"
    )

    private val classificationCounts = mapOf(
        // --- KEDUDUKAN HADITS ---
        "KumpulanAlQuran" to listOf(703, 318, 433, 230, 164, 132, 137, 947, 40),
        "KumpulanQudsi" to listOf(131, 80, 55, 15, 25, 26, 23, 432, 11),
        "KumpulanMutawatir" to listOf(1310, 945, 309, 378, 846, 345, 238, 3655, 167),
        "KumpulanMarfu" to listOf(6409, 5193, 3736, 4332, 5354, 4223, 1983, 25589, 736),
        "KumpulanMauquf" to listOf(461, 87, 84, 163, 214, 82, 571, 313, 599),
        "KumpulanMaqthu" to listOf(7, 2, 16, 80, 69, 1, 790, 29, 248),
        "KumpulanMursal" to listOf(65, 33, 69, 216, 164, 25, 90, 244, 186),
        "KumpulanMunqathi" to listOf(24, 23, 174, 190, 139, 123, 271, 1175, 210),
        "KumpulanMuallaq" to listOf(629, 13, 472, 278, 6, 2, 5, 89, 1),

        // --- TEMA HADITS ---
        "TemaIman" to listOf(1874, 1716, 1466, 910, 1383, 1303, 759, 9249, 261),
        "TemaIlmu" to listOf(1082, 1036, 741, 625, 889, 645, 944, 5608, 305),
        "TemaUmat" to listOf(480, 346, 192, 127, 136, 132, 92, 1334, 26),
        "TemaPerjalanan" to listOf(2742, 1698, 1155, 945, 1198, 746, 531, 8026, 206),
        "TemaAlQuran" to listOf(1131, 461, 624, 327, 405, 285, 439, 2135, 95),
        "TemaAkhlaq" to listOf(1193, 1010, 859, 716, 580, 723, 478, 5243, 194),
        "TemaIbadah" to listOf(3423, 2873, 1782, 2504, 3638, 2383, 1467, 13821, 956),
        "TemaMakanan" to listOf(470, 434, 240, 276, 462, 340, 176, 1806, 91),
        "TemaPakaian" to listOf(437, 271, 193, 284, 444, 199, 87, 1519, 71),
        "TemaKepribadian" to listOf(781, 557, 331, 435, 546, 393, 565, 2049, 223),
        "TemaMuamalah" to listOf(1099, 688, 379, 547, 804, 502, 371, 2852, 237),
        "TemaHukum" to listOf(487, 279, 196, 291, 285, 183, 116, 1178, 107),
        "TemaKriminalitas" to listOf(455, 290, 174, 292, 621, 243, 146, 1276, 107),
        "TemaJihad" to listOf(814, 407, 268, 379, 367, 231, 135, 1837, 68)
    )

    private fun getCounts(queryName: String): List<Int> {
        val cleanQuery = queryName.replace(Regex("[^A-Za-z0-9]"), "").lowercase()
        val entry = classificationCounts.entries.firstOrNull { (key, _) ->
            key.replace(Regex("[^A-Za-z0-9]"), "").lowercase() == cleanQuery
        }
        return entry?.value ?: classificationCounts["TemaIman"] ?: listOf(1874, 1716, 1466, 910, 1383, 1303, 759, 9249, 261)
    }

    private fun normalizeBookSlug(slug: String): String {
        val clean = slug.lowercase()
        return when {
            clean.contains("bukhari") -> "ShahihBukhari"
            clean.contains("muslim") -> "ShahihMuslim"
            clean.contains("tirmidzi") -> "SunanTirmidzi"
            clean.contains("abudaud") -> "SunanAbuDaud"
            clean.contains("nasai") -> "SunanNasai"
            clean.contains("ibnumajah") -> "SunanIbnuMajah"
            clean.contains("darimi") -> "SunanDarimi"
            clean.contains("ahmad") -> "MusnadAhmad"
            clean.contains("malik") -> "MuwathaMalik"
            else -> slug
        }
    }

    fun resolveBookSlug(queryName: String, globalIndex: Int): String {
        val counts = getCounts(queryName)
        var cumulative = 0

        for (i in targetBooks.indices) {
            cumulative += counts[i]
            if (globalIndex <= cumulative) {
                return targetBooks[i]
            }
        }
        return targetBooks.first()
    }

    fun getTotalCount(queryName: String): Int {
        return getCounts(queryName).sum()
    }

    fun getStartingGlobalIndex(queryName: String, bookSlug: String): Int {
        val targetSlug = normalizeBookSlug(bookSlug)
        val counts = getCounts(queryName)
        val bookIndex = targetBooks.indexOfFirst { it.equals(targetSlug, ignoreCase = true) }
        if (bookIndex <= 0) return 1

        var startingIndex = 1
        for (i in 0 until bookIndex) {
            startingIndex += counts[i]
        }
        return startingIndex
    }

    fun getBookCount(queryName: String, bookSlug: String): Int {
        val counts = getCounts(queryName)
        val targetSlug = normalizeBookSlug(bookSlug)
        val bookIndex = targetBooks.indexOfFirst { it.equals(targetSlug, ignoreCase = true) }
        return if (bookIndex in targetBooks.indices) counts[bookIndex] else 0
    }
}