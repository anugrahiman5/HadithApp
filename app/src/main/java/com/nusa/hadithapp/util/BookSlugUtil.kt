package com.nusa.hadithapp.util

import android.util.Log

object BookSlugUtil {

    private const val TAG = "BookSlugUtil"

    /**
     * Set canonical book slugs yang dikenali sistem
     * Menggunakan Set untuk O(1) lookup performance
     */
    val ALL_BOOK_SLUGS: Set<String> = setOf(
        "ShahihBukhari",
        "ShahihMuslim",
        "SunanTirmidzi",
        "SunanAbuDaud",
        "SunanNasai",
        "SunanIbnuMajah",
        "SunanDarimi",
        "MusnadAhmad",
        "MuwathaMalik",
        "SunanDaruquthni",
        "ShahihIbnuKhuzaimah",
        "ShahihIbnuHibban",
        "AlMustadrak",
        "MusnadSyafii"
    )

    /**
     * Map dari alias ke canonical slug
     * Digunakan untuk normalisasi input dari berbagai sumber
     */
    private val ALIAS_TO_CANONICAL = mapOf(
        // Bukhari
        "bukhari" to "ShahihBukhari",
        "shahihbukhari" to "ShahihBukhari",
        "fathulbari" to "ShahihBukhari",

        // Muslim
        "muslim" to "ShahihMuslim",
        "shahihmuslim" to "ShahihMuslim",

        // Tirmidzi
        "tirmidzi" to "SunanTirmidzi",
        "sunan tirmidzi" to "SunanTirmidzi",
        "tirmizi" to "SunanTirmidzi",  // Alternative spelling

        // Abu Daud
        "abudaud" to "SunanAbuDaud",
        "abu daud" to "SunanAbuDaud",
        "abud" to "SunanAbuDaud",

        // Nasai
        "nasai" to "SunanNasai",
        "nasai" to "SunanNasai",
        "nasa'i" to "SunanNasai",

        // Ibnu Majah
        "ibnumajah" to "SunanIbnuMajah",
        "ibnu majah" to "SunanIbnuMajah",

        // Darimi
        "darimi" to "SunanDarimi",

        // Ahmad
        "ahmad" to "MusnadAhmad",
        "musnadahmad" to "MusnadAhmad",

        // Malik
        "malik" to "MuwathaMalik",
        "muwatha" to "MuwathaMalik",
        "muwathamalik" to "MuwathaMalik",

        // Daruquthni
        "daruquthni" to "SunanDaruquthni",

        // Ibnu Khuzaimah
        "ibnukhuzaimah" to "ShahihIbnuKhuzaimah",
        "khuzaimah" to "ShahihIbnuKhuzaimah",

        // Ibnu Hibban
        "ibnuhibban" to "ShahihIbnuHibban",
        "hibban" to "ShahihIbnuHibban",

        // Al-Hakim / Al-Mustadrak
        "alhakim" to "AlMustadrak",
        "hakim" to "AlMustadrak",
        "mustadrak" to "AlMustadrak",
        "almustadrak" to "AlMustadrak",

        // Syafi'i / Syafii
        "syafii" to "MusnadSyafii",
        "syafi'i" to "MusnadSyafii",
        "musnadsyafii" to "MusnadSyafii"
    )

    /**
     * Normalisasi book name/slug ke canonical format
     * Menghapus special characters, lowercase, lalu match ke canonical slug
     *
     * @param slug Input book name atau slug (bisa berbagai format)
     * @return Canonical slug atau input original jika tidak dikenali
     *
     * Contoh:
     * "shahih-bukhari" → "ShahihBukhari"
     * "Shahih Muslim" → "ShahihMuslim"
     * "bukhari" → "ShahihBukhari"
     * "unknown" → "unknown" (fallback)
     */
    fun normalize(slug: String): String {
        if (slug.isBlank()) {
            Log.w(TAG, "Empty slug provided to normalize()")
            return slug
        }

        // Bersihkan input: lowercase + hapus special characters
        val cleanKey = slug.lowercase().trim()
            .replace(Regex("[^a-z0-9]"), "")  // Hapus semua non-alphanumeric

        // Cek di alias map
        val canonical = ALIAS_TO_CANONICAL[cleanKey]
        if (canonical != null) {
            Log.d(TAG, "Normalized: '$slug' → '$canonical'")
            return canonical
        }

        // Jika tidak ada di alias map, cek langsung di canonical set
        // (case-insensitive comparison)
        val directMatch = ALL_BOOK_SLUGS.find {
            it.lowercase() == cleanKey
        }
        if (directMatch != null) {
            Log.d(TAG, "Direct match: '$slug' → '$directMatch'")
            return directMatch
        }

        // Tidak ditemukan, return original
        Log.w(TAG, "No match found for slug: '$slug', returning original")
        return slug
    }

    /**
     * Cek apakah slug adalah canonical slug yang valid
     *
     * @param slug Slug untuk dicek
     * @return true jika slug ada dalam ALL_BOOK_SLUGS
     */
    fun isValidSlug(slug: String): Boolean {
        return ALL_BOOK_SLUGS.contains(slug)
    }

    /**
     * Cek apakah input dapat dikenali sebagai book slug
     * (baik canonical maupun alias)
     *
     * @param input Input book name/slug
     * @return true jika input dapat di-normalize
     */
    fun isRecognizedBook(input: String): Boolean {
        return try {
            val normalized = normalize(input)
            isValidSlug(normalized)
        } catch (e: Exception) {
            Log.e(TAG, "Error checking if book is recognized: $input", e)
            false
        }
    }

    /**
     * Dapatkan list semua canonical book slugs
     */
    fun getAllSlugsList(): List<String> = ALL_BOOK_SLUGS.toList()

    /**
     * Dapatkan index dari canonical slug
     * Berguna untuk sorting/ordering
     *
     * @param slug Canonical slug
     * @return Index dalam urutan canonical, atau -1 jika tidak ditemukan
     */
    fun getSlugIndex(slug: String): Int {
        return getAllSlugsList().indexOf(slug)
    }

    // 🟢 Tambahkan fungsi ini untuk mendapatkan nama resmi sesuai dokumentasi API
    fun getOfficialBookName(slug: String): String {
        return when (normalize(slug)) {
            "ShahihBukhari" -> "Shahih Al-Bukhari"
            "ShahihMuslim" -> "Shahih Muslim"
            "SunanTirmidzi" -> "Sunan At-Tirmidzi"
            "SunanAbuDaud" -> "Sunan Abu Dawud"
            "SunanNasai" -> "Sunan An-Nasa'i"
            "SunanIbnuMajah" -> "Sunan Ibnu Majah"
            "SunanDarimi" -> "Sunan Ad-Darimi"
            "MusnadAhmad" -> "Musnad Ahmad"
            "MuwathaMalik" -> "Muwatha Malik"
            "SunanDaruquthni" -> "Sunan Ad-Daraquthni"
            "ShahihIbnuKhuzaimah" -> "Shahih Ibnu Khuzaimah"
            "ShahihIbnuHibban" -> "Shahih Ibnu Hibban"
            "AlMustadrak" -> "Al-Mustadrak Ala As-Shahihain"
            "MusnadSyafii" -> "Musnad As-Syafi'i"
            else -> slug.replace(Regex("(?<!^)(?=[A-Z])"), " ")
        }
    }
}