package com.nusa.hadithapp.domain.model.biography

data class ImamBiography(
    val id: String,                         // e.g. "Bukhari", "Muslim", "Abu Daud", ...
    val shortName: String,                  // e.g. "Imam Al-Bukhari"
    val arabicName: String,                 // e.g. "الإِمَامُ البُخَارِيُّ رحمه الله"
    val fullNameLatin: String,              // e.g. "Abu Abdillah Muhammad bin Ismail"
    val nasab: String,                      // e.g. "bin Ibrahim bin Al-Mughirah bin Bardizbah Al-Ju'fi Al-Bukhari"
    val titles: List<String>,               // e.g. ["AMIRUL MUKMININ FIL HADITS", "Nashir Sunnah"]
    val categoryBadge: String,              // e.g. "Kutubus Sittah #1"
    val periodHijri: String,                // e.g. "194 H - 256 H"
    val periodMasehi: String,               // e.g. "810 M - 870 M / 62 Tahun"
    val birthAndDeathPlace: String,         // e.g. "Bukhara \u2192 Khartank"
    val countryRegion: String,              // e.g. "Uzbekistan (Samarkand)"
    val targetBookSlug: String,             // e.g. "ShahihBukhari"
    val metrics: ImamMetrics,
    val timeline: List<BiographyTimelineNode>,
    val methodology: List<BiographyMethodologyItem>,
    val majorWorks: List<BiographyMajorWork>,
    val scholarTestimonials: List<ScholarTestimonial>,
    val apiRawText: String? = null,
    val parsedTeachers: List<String> = emptyList(),
    val parsedStudents: List<String> = emptyList(),
    val parsedSummaryParagraphs: List<String> = emptyList()
)

data class ImamMetrics(
    val totalHadithsDisplay: String,        // "7.008"
    val totalHadithsDesc: String,           // "Total hadis dengan sanad bersambung"
    val selectionPeriod: String,            // "16 Thn"
    val selectionPeriodDesc: String,        // "Disaring dari 600.000 riwayat hadis"
    val teachersCount: String,              // "1.000+"
    val teachersDesc: String,               // "Syaikh perawi terverifikasi adil & tsiqah"
    val chaptersCount: String,              // "97 Kitab"
    val chaptersDesc: String                // "Tercakup dalam 3.450+ sub-bab fikih"
)

data class BiographyTimelineNode(
    val stepNumber: Int,
    val yearBadge: String,                  // "194 H (810 M)"
    val locationBadge: String,              // "Bukhara"
    val title: String,                      // "Kelahiran & Hafalan Masa Kecil"
    val description: String
)

data class BiographyMethodologyItem(
    val number: Int,
    val title: String,                      // "Syarat Al-Liqa' (Pertemuan Fisik Nyata)"
    val explanation: String
)

data class BiographyMajorWork(
    val titleLatin: String,                 // "Al-Jami' Ash-Shahih (Shahih Bukhari)"
    val titleArabic: String,                // "الجامع المسند الصحيح المختصر"
    val badgePrimary: String,               // "Kitab Rujukan Utama"
    val badgeSecondary: String? = null,     // "16 Tahun Kajian"
    val description: String,
    val isMainBook: Boolean = false
)

data class ScholarTestimonial(
    val scholarName: String,                // "Al-Imam Ahmad bin Hanbal"
    val scholarTitle: String,               // "Imam Ahlussunnah (Wafat 241 H)"
    val quote: String
)

enum class BiographyTab(val title: String) {
    SUMMARY("Ringkasan"),
    TIMELINE("Rihlah Ilmiah"),
    WORKS("Kitab & Karya"),
    METHODOLOGY("Metodologi")
}

data class ImamSummary(
    val id: String,
    val shortName: String,
    val targetBookSlug: String
)
