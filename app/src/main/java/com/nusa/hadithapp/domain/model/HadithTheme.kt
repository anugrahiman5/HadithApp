package com.nusa.hadithapp.domain.model

enum class HadithTheme(
    val className: String,
    val slug: String,
    val displayName: String
) {
    IMAN("TemaIman", "iman", "Iman"),
    ILMU("TemaIlmu", "ilmu", "Ilmu"),
    UMAT("TemaUmat", "umat", "Umat Terdahulu"),
    PERJALANAN("TemaPerjalanan", "perjalanan", "Perjalanan Hidup"),
    ALQURAN("TemaAlQuran", "quran", "Al Qur'an"),
    AKHLAQ("TemaAkhlaq", "akhlaq", "Akhlaq dan Adab"),
    IBADAH("TemaIbadah", "ibadah", "Ibadah"),
    MAKANAN("TemaMakanan", "makanan", "Makanan & Minuman"),
    PAKAIAN("TemaPakaian", "pakaian", "Pakaian & Perhiasan"),
    KEPRIBADIAN("TemaKepribadian", "kepribadian", "Kepribadian"),
    MUAMALAH("TemaMuamalah", "muamalah", "Muamalah"),
    HUKUM("TemaHukum", "hukum", "Hukum"),
    KRIMINALITAS("TemaKriminalitas", "kriminalitas", "Kriminalitas"),
    JIHAD("TemaJihad", "jihad", "Jihad");

    companion object {
        fun fromSlug(slug: String): HadithTheme? = entries.find { it.slug == slug }
        fun fromClassName(className: String): HadithTheme? = entries.find { it.className == className }
    }
}