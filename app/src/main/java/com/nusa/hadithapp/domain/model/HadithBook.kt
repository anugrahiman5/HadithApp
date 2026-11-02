package com.nusa.hadithapp.domain.model

import androidx.compose.ui.graphics.Color

enum class HadithBook(
    val slug: String,
    val shortName: String,
    val fullName: String,
    val totalHadith: Int,
    val initial: String,
    val badgeColor: Color,
    val arabicName: String,         // Judul Klasik Bahasa Arab
    val categoryBadge: String = "Kutubut Tis'ah", // Badge (Kutubus Sittah / Kutubut Tis'ah)
    val cleanName: String = fullName,
    val shortArabicName: String = "",
    val categoryTag: String = "",
    val dotColor: Color = Color(0xFF0F6D4A)
) {
    BUKHARI(
        slug = "bukhari",
        shortName = "Bukhari",
        fullName = "Shahih Al-Bukhari",
        cleanName = "Shahih Bukhari",
        totalHadith = 7008,
        initial = "B",
        badgeColor = Color(0xFF1E3A8A),
        arabicName = "الجامع المسند الصحيح المختصر",
        shortArabicName = "بخاري",
        categoryBadge = "Kutubus Sittah",
        categoryTag = "TIS'AH #1",
        dotColor = Color(0xFF0F6D4A)
    ),
    MUSLIM(
        slug = "muslim",
        shortName = "Muslim",
        fullName = "Shahih Muslim",
        cleanName = "Shahih Muslim",
        totalHadith = 5362,
        initial = "M",
        badgeColor = Color(0xFF065F46),
        arabicName = "المسند الصحيح المختصر",
        shortArabicName = "مسلم",
        categoryBadge = "Kutubus Sittah",
        categoryTag = "TIS'AH #2",
        dotColor = Color(0xFF0F6D4A)
    ),
    TIRMIDZI(
        slug = "tirmidzi",
        shortName = "Tirmidzi",
        fullName = "Sunan Al-Tirmidzi",
        cleanName = "Sunan Tirmidzi",
        totalHadith = 3891,
        initial = "T",
        badgeColor = Color(0xFF9A3412),
        arabicName = "الجامع المختصر من السنن",
        shortArabicName = "الترمذي",
        categoryBadge = "Kutubus Sittah",
        categoryTag = "TIS'AH #4",
        dotColor = Color(0xFFB45309)
    ),
    ABU_DAUD(
        slug = "abu-daud",
        shortName = "Abu Daud",
        fullName = "Sunan Abu Dawud",
        cleanName = "Sunan Abu Daud",
        totalHadith = 4590,
        initial = "D",
        badgeColor = Color(0xFF831843),
        arabicName = "كتاب السنن لأبي داود",
        shortArabicName = "أبو داود",
        categoryBadge = "Kutubus Sittah",
        categoryTag = "TIS'AH #3",
        dotColor = Color(0xFFB45309)
    ),
    NASAI(
        slug = "nasai",
        shortName = "Nasa'i",
        fullName = "Sunan Al-Nasa'i",
        cleanName = "Sunan Nasa'i",
        totalHadith = 5662,
        initial = "N",
        badgeColor = Color(0xFF312E81),
        arabicName = "المجتبى من السنن الصغرى",
        shortArabicName = "النسائي",
        categoryBadge = "Kutubus Sittah",
        categoryTag = "TIS'AH #5",
        dotColor = Color(0xFFB45309)
    ),
    IBNU_MAJAH(
        slug = "ibnu-majah",
        shortName = "Ibnu Majah",
        fullName = "Sunan Ibnu Majah",
        cleanName = "Sunan Ibnu Majah",
        totalHadith = 4332,
        initial = "I",
        badgeColor = Color(0xFF1E293B),
        arabicName = "كتاب السنن لابن ماجه",
        shortArabicName = "ابن ماجه",
        categoryBadge = "Kutubus Sittah",
        categoryTag = "TIS'AH #6",
        dotColor = Color(0xFFB45309)
    ),
    DARIMI(
        slug = "darimi",
        shortName = "Darimi",
        fullName = "Sunan Al-Darimi",
        cleanName = "Sunan Ad-Darimi",
        totalHadith = 3367,
        initial = "Da",
        badgeColor = Color(0xFF0284C7),
        arabicName = "المسند الجامع المعروف بسنن الدارمي",
        shortArabicName = "الدارمي",
        categoryBadge = "Kutubut Tis'ah",
        categoryTag = "TIS'AH #9",
        dotColor = Color(0xFFB45309)
    ),
    AHMAD(
        slug = "ahmad",
        shortName = "Ahmad",
        fullName = "Musnad Ahmad",
        cleanName = "Musnad Ahmad",
        totalHadith = 26363,
        initial = "A",
        badgeColor = Color(0xFFB45309),
        arabicName = "المسند الكبير للإمام أحمد",
        shortArabicName = "أحمد",
        categoryBadge = "Kutubut Tis'ah",
        categoryTag = "TIS'AH #7",
        dotColor = Color(0xFF0F6D4A)
    ),
    MALIK(
        slug = "malik",
        shortName = "Malik",
        fullName = "Muwatha' Malik",
        cleanName = "Muwatha' Malik",
        totalHadith = 1595,
        initial = "Ma",
        badgeColor = Color(0xFF047857),
        arabicName = "كتاب الموطأ للإمام مالك",
        shortArabicName = "مالك",
        categoryBadge = "Kutubut Tis'ah",
        categoryTag = "TIS'AH #8",
        dotColor = Color(0xFF0F6D4A)
    ),
    DARUQUTHNI(
        slug = "daruquthni",
        shortName = "Daruquthni",
        fullName = "Sunan Al-Daraquthni",
        cleanName = "Sunan Ad-Daruquthni",
        totalHadith = 4790,
        initial = "Dq",
        badgeColor = Color(0xFF6B21A8),
        arabicName = "سنن الدارقطني",
        shortArabicName = "الدارقطني",
        categoryBadge = "Kitab Mu'tabar",
        categoryTag = "MUKTABAR #10",
        dotColor = Color(0xFFB45309)
    ),
    IBNU_KHUZAIMAH(
        slug = "ibnu-khuzaimah",
        shortName = "Ibnu Khuzaimah",
        fullName = "Shahih Ibnu Khuzaimah",
        cleanName = "Shahih Ibnu Khuzaimah",
        totalHadith = 1808,
        initial = "IK",
        badgeColor = Color(0xFF0369A1),
        arabicName = "مختصر المختصر من المسند الصحيح",
        shortArabicName = "ابن خزيمة",
        categoryBadge = "Kitab Shahih",
        categoryTag = "MUKTABAR #11",
        dotColor = Color(0xFF0F6D4A)
    ),
    IBNU_HIBBAN(
        slug = "ibnu-hibban",
        shortName = "Ibnu Hibban",
        fullName = "Shahih Ibnu Hibban",
        cleanName = "Shahih Ibnu Hibban",
        totalHadith = 2769,
        initial = "IH",
        badgeColor = Color(0xFF4338CA),
        arabicName = "التقاسيم والأنواع",
        shortArabicName = "ابن حبان",
        categoryBadge = "Kitab Shahih",
        categoryTag = "MUKTABAR #12",
        dotColor = Color(0xFF0F6D4A)
    ),
    AL_HAKIM(
        slug = "al-mustadrak",
        shortName = "Al Hakim",
        fullName = "Al-Mustadrak Al-Hakim",
        cleanName = "Al-Mustadrak Al-Hakim",
        totalHadith = 673,
        initial = "H",
        badgeColor = Color(0xFF0F766E),
        arabicName = "المستدرك على الصحيحين",
        shortArabicName = "الحاكم",
        categoryBadge = "Kitab Mustadrak",
        categoryTag = "MUKTABAR #13",
        dotColor = Color(0xFF0F6D4A)
    ),
    SYAFII(
        slug = "syafii",
        shortName = "Syafi'i",
        fullName = "Musnad Asy-Syafi'i",
        cleanName = "Musnad Asy-Syafi'i",
        totalHadith = 1800,
        initial = "S",
        badgeColor = Color(0xFF374151),
        arabicName = "مسند الإمام المطلبي محمد بن إدريس الشافعي",
        shortArabicName = "الشافعي",
        categoryBadge = "Kitab Masanid",
        categoryTag = "MUKTABAR #14",
        dotColor = Color(0xFF0F6D4A)
    );

    companion object {
        /**
         * Urutan kanonik 14 kitab sesuai grid tampilan 2 kolom:
         * Row 1: Bukhari & Muslim
         * Row 2: Abu Daud & Tirmidzi
         * Row 3: Nasa'i & Ibnu Majah
         * Row 4: Ahmad & Malik
         * Row 5: Darimi & Daruquthni
         * Row 6: Ibnu Khuzaimah & Ibnu Hibban
         * Row 7: Al-Hakim & Syafi'i
         */
        val canonicalOrder: List<HadithBook> = listOf(
            BUKHARI, MUSLIM,
            ABU_DAUD, TIRMIDZI,
            NASAI, IBNU_MAJAH,
            AHMAD, MALIK,
            DARIMI, DARUQUTHNI,
            IBNU_KHUZAIMAH, IBNU_HIBBAN,
            AL_HAKIM, SYAFII
        )

        fun fromSlug(slug: String): HadithBook {
            val clean = slug.lowercase().replace("-", "").replace("_", "").replace(" ", "")
            return entries.find {
                it.slug.lowercase().replace("-", "") == clean ||
                        it.shortName.lowercase() == clean ||
                        clean.contains(it.slug.lowercase().replace("-", ""))
            } ?: BUKHARI
        }
    }
}
