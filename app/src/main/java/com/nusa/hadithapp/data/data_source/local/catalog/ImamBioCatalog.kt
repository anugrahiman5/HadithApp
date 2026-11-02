package com.nusa.hadithapp.data.data_source.local.catalog

import com.nusa.hadithapp.domain.model.biography.BiographyMajorWork
import com.nusa.hadithapp.domain.model.biography.BiographyMethodologyItem
import com.nusa.hadithapp.domain.model.biography.BiographyTimelineNode
import com.nusa.hadithapp.domain.model.biography.ImamBiography
import com.nusa.hadithapp.domain.model.biography.ImamMetrics
import com.nusa.hadithapp.domain.model.biography.ImamSummary
import com.nusa.hadithapp.domain.model.biography.ScholarTestimonial

object ImamBioCatalog {

    val ALL_IMAMS: List<ImamSummary> = listOf(
        ImamSummary("Bukhari", "Imam Al-Bukhari", "ShahihBukhari"),
        ImamSummary("Muslim", "Imam Muslim", "ShahihMuslim"),
        ImamSummary("Abu Daud", "Imam Abu Daud", "SunanAbuDaud"),
        ImamSummary("Tirmidzi", "Imam At-Tirmidzi", "SunanTirmidzi"),
        ImamSummary("Nasai", "Imam An-Nasa'i", "SunanNasai"),
        ImamSummary("Ibnu Majah", "Imam Ibnu Majah", "SunanIbnuMajah"),
        ImamSummary("Ahmad", "Imam Ahmad", "MusnadAhmad"),
        ImamSummary("Malik", "Imam Malik", "MuwathaMalik"),
        ImamSummary("Darimi", "Imam Ad-Darimi", "SunanDarimi"),
        ImamSummary("Daruquthni", "Imam Ad-Daruquthni", "SunanDaruquthni"),
        ImamSummary("Ibnu Khuzaimah", "Imam Ibnu Khuzaimah", "ShahihIbnuKhuzaimah"),
        ImamSummary("Ibnu Hibban", "Imam Ibnu Hibban", "ShahihIbnuHibban"),
        ImamSummary("Al-Hakim", "Imam Al-Hakim", "AlMustadrakAlHakim"),
        ImamSummary("Syafii", "Imam Asy-Syafi'i", "MusnadAsySyafii")
    )

    fun getImam(key: String): ImamBiography {
        val cleanKey = key.trim().lowercase()
        return when {
            cleanKey.contains("bukhari") -> bukhari
            cleanKey.contains("muslim") -> muslim
            cleanKey.contains("abu daud") || cleanKey.contains("abudaud") || cleanKey.contains("dawud") -> abuDaud
            cleanKey.contains("tirmidzi") || cleanKey.contains("tirmizi") -> tirmidzi
            cleanKey.contains("nasai") || cleanKey.contains("nasa'i") || cleanKey.contains("nasa") -> nasai
            cleanKey.contains("majah") || cleanKey.contains("maja") -> ibnuMajah
            cleanKey.contains("ahmad") || cleanKey.contains("hanbal") -> ahmad
            cleanKey.contains("malik") -> malik
            cleanKey.contains("darimi") -> darimi
            cleanKey.contains("daruquthni") || cleanKey.contains("daraquthni") -> daruquthni
            cleanKey.contains("khuzaimah") -> ibnuKhuzaimah
            cleanKey.contains("hibban") -> ibnuHibban
            cleanKey.contains("hakim") || cleanKey.contains("mustadrak") -> alHakim
            cleanKey.contains("syafii") || cleanKey.contains("shafii") || cleanKey.contains("syafi'i") -> syafii
            else -> bukhari
        }
    }

    // 1. Imam Al-Bukhari
    private val bukhari = ImamBiography(
        id = "Bukhari",
        shortName = "Imam Al-Bukhari",
        arabicName = "الإِمَامُ البُخَارِيُّ رحمه الله",
        fullNameLatin = "Abu Abdillah Muhammad bin Ismail",
        nasab = "bin Ibrahim bin Al-Mughirah bin Bardizbah Al-Ju'fi Al-Bukhari",
        titles = listOf("AMIRUL MUKMININ FIL HADITS", "Nashir Sunnah"),
        categoryBadge = "Kutubus Sittah #1",
        periodHijri = "194 H - 256 H",
        periodMasehi = "810 M - 870 M / 62 Tahun",
        birthAndDeathPlace = "Bukhara \u2192 Khartank",
        countryRegion = "Uzbekistan (Samarkand)",
        targetBookSlug = "ShahihBukhari",
        metrics = ImamMetrics(
            totalHadithsDisplay = "7.008",
            totalHadithsDesc = "Total hadis dengan sanad bersambung",
            selectionPeriod = "16 Thn",
            selectionPeriodDesc = "Disaring dari 600.000 riwayat hadis",
            teachersCount = "1.000+",
            teachersDesc = "Syaikh perawi terverifikasi adil & tsiqah",
            chaptersCount = "97 Kitab",
            chaptersDesc = "Tercakup dalam 3.450+ sub-bab fikih"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "194 H (810 M)",
                locationBadge = "Bukhara",
                title = "Kelahiran & Hafalan Masa Kecil",
                description = "Lahir pada 13 Syawwal 194 H. Tumbuh yatim di bawah asuhan ibunda yang shalihah. Menghafal Al-Qur'an dan ribuan hadis sejak usia 10 tahun dengan daya ingat luar biasa (hifzh mutqin)."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "210 H (826 M)",
                locationBadge = "Hijaz",
                title = "Rihlah Haji & Menetap di Tanah Suci",
                description = "Berangkat haji ke Makkah bersama ibu dan saudaranya (Ahmad). Ketika keluarganya pulang ke Bukhara, beliau memilih menetap di Makkah dan Madinah untuk berguru kepada ulama senior Hijaz."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "216 H - 226 H",
                locationBadge = "Rihlah Kubra",
                title = "Pengembaraan Ilmu Antar Benua",
                description = "Mengembara ribuan kilometer ke Bashrah, Kufah, Baghdad, Syam, dan Mesir. Menemui imam hadis besar: Ahmad bin Hanbal, Ali bin Al-Madini, Ishaq bin Rahawaih, dan Yahya bin Ma'in."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "256 H (870 M)",
                locationBadge = "Khartank",
                title = "Wafat Mulia di Malam Idul Fitri",
                description = "Wafat di Khartank (dekat Samarkand) pada malam Idul Fitri 256 H di usia 62 tahun. Makam beliau menebarkan semerbak wangi kasturi disaksikan para perawi sezaman."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Syarat Al-Liqa' (Pertemuan Fisik Nyata)",
                explanation = "Berbeda dari Imam Muslim yang mencukupkan syarat 'Mu'asharah' (hidup satu zaman), Imam Bukhari mensyaratkan bukti otentik perawi benar-benar pernah saling bertemu langsung dan mendengar matan (tsubut al-liqa')."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Tsiqah Tingkat Utama (Adil & Dhabith Kamil)",
                explanation = "Hanya meriwayatkan dari perawi lapisan puncak (Thabaqat Ula) yang sempurna integritas agamanya ('Adalah fid Diin) dan ketelitian hafalannya ('Dhabth al-Kitab wa Ash-Shadr')."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Nafyu Asy-Syudzudz wa Al-'Ilal",
                explanation = "Sanad dan matan diuji secara ketat agar steril dari syadz (kejanggalan periwayatan yang menyelisihi perawi lebih tsiqah) serta bersih dari 'illat qadihah (cacat tersembunyi yang merusak)."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Al-Jami' Ash-Shahih (Shahih Bukhari)",
                titleArabic = "الْجَامِعُ الْمُسْنَدُ الصَّحِيحُ الْمُخْتَصَرُ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "16 Tahun Kajian",
                description = "Kitab paling otentik di muka bumi setelah Kitabullah Al-Qur'an. Disusun dan disaring di Raudhah Nabawiyyah dengan shalat istikharah 2 rakaat di setiap hadisnya.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Al-Tarikh Al-Kabir",
                titleArabic = "التَّارِيخُ الْكَبِيرُ",
                badgePrimary = "Biografi Rawi",
                description = "Ensiklopedia biografi perawi hadis monumental mencakup puluhan ribu sahabat, tabi'in, dan atba' tabi'in beserta status jarh wa ta'dil."
            ),
            BiographyMajorWork(
                titleLatin = "Al-Adab Al-Mufrad",
                titleArabic = "الْأَدَبُ الْمُفْرَدُ",
                badgePrimary = "Akhlaq & Adab",
                description = "Kompilasi 1.300+ hadis dan atsar adab Islami, akhlaq karimah, birrul walidain, hak tetangga, dan etika muamalah harian seorang muslim."
            ),
            BiographyMajorWork(
                titleLatin = "Khalq Af'al Al-'Ibad",
                titleArabic = "خَلْقُ أَفْعَالِ الْعِبَادِ",
                badgePrimary = "Aqidah Sunnah",
                description = "Karya ilmiah pembelaan akidah Ahlussunnah terhadap penyimpangan paham Jahmiyyah dan Mu'tazilah mengenai Kalamullah dan penciptaan perbuatan hamba."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Al-Imam Ahmad bin Hanbal",
                scholarTitle = "Imam Ahlussunnah (Wafat 241 H)",
                quote = "Kawasan Khurasan belum pernah melahirkan sosok sejenius dan sealim Muhammad bin Ismail (Al-Bukhari)."
            ),
            ScholarTestimonial(
                scholarName = "Al-Imam Muslim bin Al-Hajjaj",
                scholarTitle = "Penyusun Shahih Muslim (Wafat 261 H)",
                quote = "Biarkan aku mencium kedua kakimu wahai guru para guru, sayyid perawi hadis, dan thabib dalam pengobatan cacat-cacat hadis dalam periwayatan!"
            ),
            ScholarTestimonial(
                scholarName = "Al-Imam Ali bin Al-Madini",
                scholarTitle = "Guru Terbesar Al-Bukhari (Wafat 234 H)",
                quote = "Demi Allah! Aku tidak pernah memandang diriku kecil di hadapan siapa pun di dunia ini selain saat berhadapan dengan keluasan ilmu hadis Al-Bukhari."
            )
        )
    )

    // 2. Imam Muslim
    private val muslim = ImamBiography(
        id = "Muslim",
        shortName = "Imam Muslim",
        arabicName = "الإِمَامُ مُسْلِمٌ رحمه الله",
        fullNameLatin = "Abul Husain Muslim bin Al-Hajjaj",
        nasab = "bin Muslim bin Kausyaz Al-Qusyairi An-Naisaburi",
        titles = listOf("HUJJATUL ISLAM", "Amirul Hifzh"),
        categoryBadge = "Kutubus Sittah #2",
        periodHijri = "204 H - 261 H",
        periodMasehi = "820 M - 875 M / 57 Tahun",
        birthAndDeathPlace = "Naisabur \u2192 Nashrabad",
        countryRegion = "Khurasan (Iran Timur)",
        targetBookSlug = "ShahihMuslim",
        metrics = ImamMetrics(
            totalHadithsDisplay = "5.362",
            totalHadithsDesc = "Total hadis dengan sanad bersambung",
            selectionPeriod = "15 Thn",
            selectionPeriodDesc = "Disaring dari 300.000 riwayat hadis",
            teachersCount = "300+",
            teachersDesc = "Syaikh perawi terverifikasi adil & tsiqah",
            chaptersCount = "54 Kitab",
            chaptersDesc = "Disusun sistematis menurut bab fikih"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "204 H (820 M)",
                locationBadge = "Naisabur",
                title = "Kelahiran di Pusat Peradaban Khurasan",
                description = "Lahir di Naisabur, kota metropolitan ilmu pada abad ke-3 H. Tumbuh dalam keluarga pedagang yang mulia dan terhormat."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "218 H (833 M)",
                locationBadge = "Haji Awal",
                title = "Mulai Menulis & Mendengar Hadis",
                description = "Menunaikan ibadah haji pada usia belia (14 tahun) dan langsung belajar kepada ulama-ulama besar di Makkah dan Madinah."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "230 H - 250 H",
                locationBadge = "Rihlah Ilmiah",
                title = "Jelajah Irak, Hijaz, Syam, dan Mesir",
                description = "Mengumpulkan jalur sanad dan berguru kepada Ahmad bin Hanbal, Ishaq bin Rahawaih, serta mendampingi Imam Bukhari di Naisabur."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "261 H (875 M)",
                locationBadge = "Nashrabad",
                title = "Wafat di Naisabur",
                description = "Wafat pada hari Ahad 25 Rajab 261 H di Nashrabad (pinggiran Naisabur) pada usia 57 tahun dengan warisan metodologi sanad terbaik."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Syarat Mu'asharah ma'a Imkanil Liqa'",
                explanation = "Menetapkan bahwa syarat ittishal (ketersambungan) sanad terpenuhi jika dua perawi hidup satu zaman (mu'asharah) dan memungkinkan secara geografis untuk saling bertemu, tanpa syubhat tadlis."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Ijtima' Thuruq fi Makān Wahid (Himpunan Jalur Terpadu)",
                explanation = "Keunggulan khas Shahih Muslim: menghimpun seluruh lafazh, variasi sanad, dan matan dalam satu tempat bab tanpa memecah hadis ke bab-bab lain."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Tamyiz Shiyagh Al-Ada' (Ketelitian Lafazh Periwayatan)",
                explanation = "Sangat cermat membedakan antara 'Haddatsana' (guru membacakan langsung) dan 'Akhbarana' (murid membacakan di depan guru)."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Al-Musnad Ash-Shahih (Shahih Muslim)",
                titleArabic = "الْمُسْنَدُ الصَّحِيحُ الْمُخْتَصَرُ مِنَ السُّنَنِ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "15 Tahun Kajian",
                description = "Rujukan hadis shahih dengan sistematika penyusunan jalur sanad terbaik dan paling terpadu dalam sejarah Islam.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Kitab Al-Kuna wal Asma'",
                titleArabic = "كِتَابُ الْكُنَى وَالْأَسْمَاءِ",
                badgePrimary = "Ilmu Rijal",
                description = "Karya ilmiah rujukan dalam mengidentifikasi nama asli perawi hadis yang sering hanya dipanggil dengan julukan kunyah."
            ),
            BiographyMajorWork(
                titleLatin = "Kitab At-Tamyiz",
                titleArabic = "كِتَابُ التَّمْيِيزِ",
                badgePrimary = "Metodologi 'Ilal",
                description = "Panduan aplikatif cara mengkritisi kekeliruan, wahm (ilusi perawi), dan cacat tersembunyi pada jalur sanad."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Abu Quraisy Al-Hafizh",
                scholarTitle = "Ulama Hadis Naisabur",
                quote = "Penghafal hadis dunia ada empat: Abu Zur'ah di Ray, Muslim bin Al-Hajjaj di Naisabur, Abdullah Ad-Darimi di Samarkand, dan Muhammad bin Ismail di Bukhara."
            ),
            ScholarTestimonial(
                scholarName = "Al-Hafizh Adz-Dzahabi",
                scholarTitle = "Penulis Siyar A'lam An-Nubala",
                quote = "Kitab Shahih Muslim adalah karya puncak dalam keindahan susunan dan kebersihan riwayat dari kebingungan redaksi."
            )
        )
    )

    // 3. Imam Abu Daud
    private val abuDaud = ImamBiography(
        id = "Abu Daud",
        shortName = "Imam Abu Daud",
        arabicName = "الإِمَامُ أَبُو دَاوُدَ رحمه الله",
        fullNameLatin = "Sulaiman bin Al-Asy'ats",
        nasab = "bin Ishaq bin Basyir bin Syaddad Al-Azdi As-Sijistani",
        titles = listOf("SULTANUL FUQAHA FIL HADITS", "Syaikhus Sunan"),
        categoryBadge = "Kutubus Sittah #3",
        periodHijri = "202 H - 275 H",
        periodMasehi = "817 M - 889 M / 73 Tahun",
        birthAndDeathPlace = "Sijistan \u2192 Bashrah",
        countryRegion = "Iran Timur / Irak",
        targetBookSlug = "SunanAbuDaud",
        metrics = ImamMetrics(
            totalHadithsDisplay = "4.590",
            totalHadithsDesc = "Kompilasi hadis ahkam fikih terlengkap",
            selectionPeriod = "20 Thn",
            selectionPeriodDesc = "Disaring dari 500.000 riwayat hadis",
            teachersCount = "300+",
            teachersDesc = "Berguru ke ulama Hijaz, Irak, dan Khurasan",
            chaptersCount = "35 Kitab",
            chaptersDesc = "Rujukan utama fatwa imam madzhab"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "202 H (817 M)",
                locationBadge = "Sijistan",
                title = "Kelahiran Sang Faqih Hadis",
                description = "Lahir di wilayah Sijistan (sekarang perbatasan Iran-Afghanistan), keturunan suku Arab Azd yang masyhur."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "220 H (835 M)",
                locationBadge = "Baghdad",
                title = "Mulazamah kepada Imam Ahmad bin Hanbal",
                description = "Tiba di Baghdad dan menjadi murid terkemuka Imam Ahmad. Beliau mencatat banyak fatwa fikih dan masail dari sang guru."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "240 H (854 M)",
                locationBadge = "Tarsus",
                title = "Penulisan Sunan Abu Daud",
                description = "Menetap di Tarsus selama 20 tahun untuk menyusun kitab Sunan yang difokuskan pada dalil-dalil hukum halal dan haram."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "275 H (889 M)",
                locationBadge = "Bashrah",
                title = "Wafat di Bashrah",
                description = "Wafat di Bashrah pada 16 Syawwal 275 H di usia 73 tahun setelah menghidupkan kembali majelis hadis di kota tersebut."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Fokus Penuh pada Hadis Ahkam",
                explanation = "Tidak memasukkan hadis zuhud, tarikh, atau fadhail secara acak, melainkan khusus hadis yang dijadikan landasan istinbath hukum fikih oleh para fuqaha."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Penjelasan Derajat Cacat (Bayan Adh-Dhu'f)",
                explanation = "Setiap hadis yang memiliki kelemahan fatal diterangkan oleh beliau. Jika suatu hadis beliau diamkan (sakatta 'anhu), maka hadis tersebut berstatus shalih li al-ihtijaj (layak jadi dalil)."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "I'timad Hadits Hasan wa Lasyina Bihi",
                explanation = "Menjadi pelopor penggunaan hadis berderajat hasan untuk memperluas dalil fikih jika tidak ditemukan hadis shahih dalam suatu bab."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Sunan Abu Daud",
                titleArabic = "سُنَنُ أَبِي دَاوُدَ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "4.590 Hadis",
                description = "Rujukan nomor satu dalam fikih Islam, memuat hadis-hadis landasan hukum perdata, pidana, ibadah, dan muamalah.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Kitab Al-Marasil",
                titleArabic = "كِتَابُ الْمَرَاسِيلِ",
                badgePrimary = "Musthalah",
                description = "Koleksi 500+ hadis mursal yang diteliti derajat kekuatan dan kelemahannya sebagai rujukan fikih."
            ),
            BiographyMajorWork(
                titleLatin = "Risalah ila Ahli Makkah",
                titleArabic = "رِسَالَةُ أَبِي دَاوُدَ إِلَى أَهْلِ مَكَّةَ",
                badgePrimary = "Manhaj",
                description = "Surat berharga yang menjelaskan secara langsung metodologi, kriteria keshahihan, dan syarat penyusunan kitab Sunan-nya."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Al-Imam Ahmad bin Hanbal",
                scholarTitle = "Imam Ahlussunnah",
                quote = "Abu Daud adalah sosok yang diciptakan untuk menjaga hadis Nabi shallallahu 'alaihi wa sallam."
            ),
            ScholarTestimonial(
                scholarName = "Al-Khatthabi",
                scholarTitle = "Pensyarah Sunan Abu Daud (Ma'alim As-Sunan)",
                quote = "Belum pernah ada kitab ilmu agama yang ditulis menyerupai Sunan Abu Daud dalam kemudahan rujukan fatwa fikihnya."
            )
        )
    )

    // 4. Imam At-Tirmidzi
    private val tirmidzi = ImamBiography(
        id = "Tirmidzi",
        shortName = "Imam At-Tirmidzi",
        arabicName = "الإِمَامُ التِّرْمِذِيُّ رحمه الله",
        fullNameLatin = "Abu Isa Muhammad bin Isa",
        nasab = "bin Saurah bin Musa bin Adh-Dhahhak As-Sulami At-Tirmidzi",
        titles = listOf("PELOPOR HASAN SHAHIH", "Al-Hafizh Al-Allamah"),
        categoryBadge = "Kutubus Sittah #4",
        periodHijri = "209 H - 279 H",
        periodMasehi = "824 M - 892 M / 70 Tahun",
        birthAndDeathPlace = "Tirmidz \u2192 Bugh",
        countryRegion = "Uzbekistan Selatan",
        targetBookSlug = "SunanTirmidzi",
        metrics = ImamMetrics(
            totalHadithsDisplay = "3.891",
            totalHadithsDesc = "Lengkap dengan penilaian derajat keshahihan",
            selectionPeriod = "15 Thn",
            selectionPeriodDesc = "Disertai komparasi pendapat 4 madzhab",
            teachersCount = "400+",
            teachersDesc = "Murid terdekat Imam Bukhari & Muslim",
            chaptersCount = "49 Kitab",
            chaptersDesc = "Mencakup akidah, ibadah, adab, dan tafsir"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "209 H (824 M)",
                locationBadge = "Tirmidz",
                title = "Kelahiran di Tepian Sungai Jaihun",
                description = "Lahir di kota Tirmidz (sekarang Uzbekistan). Tumbuh dengan kecerdasan luar biasa dan daya ingat fotografis."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "235 H (850 M)",
                locationBadge = "Khurasan & Irak",
                title = "Rihlah Ilmiah & Berguru kepada Bukhari",
                description = "Mengembara ke Kufah, Bashrah, Baghdad, dan Hijaz. Bertemu Imam Bukhari dan menjadi murid terdekatnya dalam ilmu 'ilal."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "260 H (874 M)",
                locationBadge = "Penyusunan Jami'",
                title = "Penyusunan Al-Jami' & Asy-Syamail",
                description = "Menyelesaikan karya ensiklopedis Al-Jami' yang memperkenalkan standarisasi istilah 'Hadits Hasan Shahih'."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "279 H (892 M)",
                locationBadge = "Bugh (Tirmidz)",
                title = "Wafat dalam Keadaan Khusyuk",
                description = "Wafat di desa Bugh pada 13 Rajab 279 H di usia 70 tahun setelah mengalami kebutaan di masa tua karena banyak menangis takut kepada Allah."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Peletakan Definisi Resmi Hadits Hasan",
                explanation = "Merupakan pelopor yang mendefinisikan hadits hasan secara baku: hadis yang diriwayatkan melalui beberapa jalur, perawinya tidak dituduh dusta, dan matannya tidak syadz."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Komparasi Pendapat Fuqaha (Fiqh Al-Hadits)",
                explanation = "Di setiap akhir hadis, beliau menyebutkan: 'Wa bihi yaqulu Syafi'i wa Ahmad wa Ishaq...', menerangkan siapa saja ulama yang mengamalkan hadis tersebut."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Bayan Al-'Ilal wal Asanid (Pembedahan Cacat Sanad)",
                explanation = "Selalu mengklarifikasi apakah perawi pernah mendengar langsung, variasi nama kunyah, dan riwayat pembanding dalam bab yang sama (wa fil babi 'an...)."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Jami' At-Tirmidzi (Sunan At-Tirmidzi)",
                titleArabic = "الْجَامِعُ الْمُخْتَصَرُ مِنَ السُّنَنِ عَنْ رَسُولِ اللَّهِ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "3.891 Hadis",
                description = "Kitab hadis paling komprehensif yang memadukan derajat hadis, kritik sanad, dan perbandingan fiqih sahabat dan tabi'in.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Asy-Syamail Al-Muhammadiyyah",
                titleArabic = "الشَّمَائِلُ الْمُحَمَّدِيَّةُ",
                badgePrimary = "Sirah & Karakter",
                description = "Karya terindah sepanjang zaman yang menghimpun sifat fisik, akhlaq, pakaian, ibadah, dan keseharian Rasulullah shallallahu 'alaihi wa sallam."
            ),
            BiographyMajorWork(
                titleLatin = "Kitab Al-'Ilal Al-Kabir",
                titleArabic = "كِتَابُ الْعِلَلِ الْكَبِيرُ",
                badgePrimary = "Ilmu Cacat Hadis",
                description = "Transkrip dialog ilmiah dan tanya jawab beliau kepada sang guru Imam Bukhari mengenai cacat tersembunyi sanad hadis."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Al-Imam Al-Bukhari",
                scholarTitle = "Guru Utama At-Tirmidzi",
                quote = "Ilmu yang aku ambil manfaatnya darimu (At-Tirmidzi) jauh lebih banyak daripada ilmu yang kau ambil manfaatnya dariku."
            ),
            ScholarTestimonial(
                scholarName = "Abu Ya'la Al-Khalili",
                scholarTitle = "Ulama Ahli Kritik Hadis",
                quote = "Muhammad bin Isa At-Tirmidzi adalah seorang tsiqah mutfaq 'alaih (disepakati kredibilitasnya), tiada tandingan dalam hafalan."
            )
        )
    )

    // 5. Imam An-Nasa'i
    private val nasai = ImamBiography(
        id = "Nasai",
        shortName = "Imam An-Nasa'i",
        arabicName = "الإِمَامُ النَّسَائِيُّ رحمه الله",
        fullNameLatin = "Abu Abdirrahman Ahmad bin Syu'aib",
        nasab = "bin Ali bin Sinan bin Bahr Al-Khurasani An-Nasa'i",
        titles = listOf("SYAIKHUL JARH WAT TA'DIL", "Al-Qadhi Al-Hafizh"),
        categoryBadge = "Kutubus Sittah #5",
        periodHijri = "215 H - 303 H",
        periodMasehi = "829 M - 915 M / 88 Tahun",
        birthAndDeathPlace = "Nasa \u2192 Ramlah / Makkah",
        countryRegion = "Turkmenistan / Palestina",
        targetBookSlug = "SunanNasai",
        metrics = ImamMetrics(
            totalHadithsDisplay = "5.662",
            totalHadithsDesc = "Kitab Sunan dengan seleksi rawi terketat",
            selectionPeriod = "25 Thn",
            selectionPeriodDesc = "Diringkas dari As-Sunan Al-Kubra",
            teachersCount = "350+",
            teachersDesc = "Belajar langsung dari Qutaibah bin Sa'id",
            chaptersCount = "51 Kitab",
            chaptersDesc = "Kaya akan bab fikih ibadah mendetail"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "215 H (829 M)",
                locationBadge = "Nasa (Khurasan)",
                title = "Kelahiran Sang Kritikus Sanad",
                description = "Lahir di kota Nasa (Turkmenistan). Mulai menghafal Al-Qur'an dan hadis sejak usia kanak-kanak."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "230 H (844 M)",
                locationBadge = "Baghlan",
                title = "Berguru ke Qutaibah bin Sa'id",
                description = "Di usia 15 tahun melakukan perjalanan ke Baghlan dan menetap selama setahun lebih bersama muhaddits besar Qutaibah bin Sa'id."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "260 H - 295 H",
                locationBadge = "Mesir",
                title = "Menjadi Rujukan Utama di Mesir",
                description = "Menetap di Mesir puluhan tahun, mendirikan majelis hadis terkemuka dan menyusun As-Sunan Al-Kubra serta As-Sunan Al-Mujtaba."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "303 H (915 M)",
                locationBadge = "Ramlah / Makkah",
                title = "Wafat dalam Keadaan Syahid",
                description = "Wafat pada bulan Shafar 303 H di usia 88 tahun setelah mengalami penganiayaan karena kecintaannya terhadap ahlul bait di Damaskus."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Standar Seleksi Perawi Paling Ketat",
                explanation = "Syarat perawi An-Nasa'i diakui para ulama lebih ketat daripada Abu Daud dan At-Tirmidzi, bahkan mendekati standar ketat Imam Muslim."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Penyingkapan Perbedaan Lafazh (Ikhtilaf Riwayat)",
                explanation = "Sangat teliti memaparkan perbedaan satu kata atau huruf di antara murid-murid dari seorang syaikh yang sama."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Pemisahan Matan Shahih dari Tambahan Lemah",
                explanation = "Mampu membedakan ziyadatul tsiqah yang maqbulah (diterima) dari riwayat yang mudraj (disisipkan)."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Al-Mujtaba (Sunan An-Nasa'i)",
                titleArabic = "الْمُجْتَبَى مِنَ السُّنَنِ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "5.662 Hadis",
                description = "Koleksi hadis pilihan hasil saringan emas dari As-Sunan Al-Kubra dengan derajat shahih dan hasan tinggi.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Khasa'is Amirul Mukminin Ali bin Abi Thalib",
                titleArabic = "خَصَائِصُ أَمِيرِ الْمُؤْمِنِينَ عَلِيِّ بْنِ أَبِي طَالِبٍ",
                badgePrimary = "Fadhail Sahabat",
                description = "Kitab kompilasi hadis otentik mengenai keutamaan dan kedudukan sahabat mulia Ali bin Abi Thalib radhiyallahu 'anhu."
            ),
            BiographyMajorWork(
                titleLatin = "Kitab Adh-Dhu'afa wal Matrukin",
                titleArabic = "كِتَابُ الضُّعَفَاءِ وَالْمَتْرُوكِينَ",
                badgePrimary = "Jarh wa Ta'dil",
                description = "Rujukan penting dalam penilaian perawi-perawi lemah dan yang ditinggalkan riwayatnya."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Al-Imam Ad-Daruquthni",
                scholarTitle = "Amirul Mukminin fil Hadits",
                quote = "Abu Abdirrahman An-Nasa'i adalah orang yang paling terdepan dalam ilmu hadis di zamannya tanpa ada keraguan."
            ),
            ScholarTestimonial(
                scholarName = "Al-Hafizh Ibnu Hajar Al-Asqalani",
                scholarTitle = "Penulis Fathul Bari",
                quote = "Sunan An-Nasa'i adalah kitab sunan yang paling sedikit memuat hadis dha'if setelah Shahih Bukhari dan Muslim."
            )
        )
    )

    // 6. Imam Ibnu Majah
    private val ibnuMajah = ImamBiography(
        id = "Ibnu Majah",
        shortName = "Imam Ibnu Majah",
        arabicName = "الإِمَامُ ابْنُ مَاجَهْ رحمه الله",
        fullNameLatin = "Abu Abdillah Muhammad bin Yazid",
        nasab = "bin Majah Al-Qazwini Ar-Raba'i",
        titles = listOf("SHAHIBUS SUNAN", "Mufassir wa Mu'arrikh"),
        categoryBadge = "Kutubus Sittah #6",
        periodHijri = "209 H - 273 H",
        periodMasehi = "824 M - 887 M / 64 Tahun",
        birthAndDeathPlace = "Qazwin \u2192 Qazwin",
        countryRegion = "Iran Utara",
        targetBookSlug = "SunanIbnuMajah",
        metrics = ImamMetrics(
            totalHadithsDisplay = "4.341",
            totalHadithsDesc = "Penyempurna Kutubus Sittah",
            selectionPeriod = "18 Thn",
            selectionPeriodDesc = "Memuat 1.339 hadis zawa'id unik",
            teachersCount = "300+",
            teachersDesc = "Melakukan rihlah ke Irak, Bashrah, dan Mesir",
            chaptersCount = "37 Kitab",
            chaptersDesc = "Sistematika bab fikih paling runtut"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "209 H (824 M)",
                locationBadge = "Qazwin",
                title = "Kelahiran di Negeri Qazwin",
                description = "Lahir di Qazwin (wilayah Iran utara). 'Majah' adalah gelar kehormatan ayah beliau Yazid."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "230 H (844 M)",
                locationBadge = "Rihlah Ilmiah",
                title = "Menjelajah Pusat-Pusat Hadis Dunia",
                description = "Mengembara ke Khurasan, Irak (Kufah, Baghdad, Bashrah), Hijaz, Syam, dan Mesir untuk mencatat hadis dari ratusan syaikh."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "250 H (864 M)",
                locationBadge = "Penyusunan Kitab",
                title = "Menulis Sunan, Tafsir, dan Tarikh",
                description = "Menyusun kitab Sunan yang sangat dipuji para ulama karena keindahan urutan bab fikihnya (husnut tartib)."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "273 H (887 M)",
                locationBadge = "Qazwin",
                title = "Wafat di Tanah Kelahiran",
                description = "Wafat pada 22 Ramadhan 273 H di usia 64 tahun dan dimakamkan di Qazwin dengan penghormatan para ulama."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Keindahan Sistematika Bab (Husnut Tartib)",
                explanation = "Kitab Sunan Ibnu Majah diakui sebagai kitab yang susunan babnya paling rapi dan jarang mengulang hadis di banyak bab."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Spesialis Hadis Zawa'id (Tambahan)",
                explanation = "Memuat ribuan riwayat hadis pelengkap yang tidak terdapat di lima kitab induk lainnya (Shahihain, Abu Daud, Tirmidzi, Nasa'i)."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Kekayaan Jalur Latha'if Al-Isnad",
                explanation = "Menyajikan jalur-jalur periwayatan unik dari ulama wilayah utara dan Syam yang memperkaya khazanah takhrij hadis."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Sunan Ibnu Majah",
                titleArabic = "سُنَنُ ابْنِ مَاجَهْ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "4.341 Hadis",
                description = "Kitab ke-6 dalam Kutubus Sittah yang melengkapi khazanah hukum fikih Islam dengan susunan bab terindah.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Tafsir Al-Qur'an Al-Azhim",
                titleArabic = "تَفْسِيرُ الْقُرْآنِ الْعَظِيمِ",
                badgePrimary = "Tafsir Atsar",
                description = "Karya tafsir bil ma'tsur monumental yang menghimpun riwayat sahabat dan tabi'in dalam penafsiran ayat Al-Qur'an."
            ),
            BiographyMajorWork(
                titleLatin = "Tarikh Qazwin",
                titleArabic = "تَارِيخُ قَزْوِينَ",
                badgePrimary = "Sejarah & Rijal",
                description = "Kitab sejarah kota Qazwin dan biografi para ulama serta perawi hadis yang pernah bermukim di sana."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Abu Ya'la Al-Khalili",
                scholarTitle = "Ulama Hadis Qazwin",
                quote = "Ibnu Majah adalah ulama besar, tsiqah, disepakati keagungannya, dan kitab Sunan-nya adalah karya bernilai tinggi."
            ),
            ScholarTestimonial(
                scholarName = "Al-Hafizh Ibnu Katsir",
                scholarTitle = "Penulis Tafsir Ibnu Katsir",
                quote = "Kitab Sunan Ibnu Majah adalah karya agung yang menunjukkan keluasan ilmu, kekuatan hafalan, dan ketepatan pemahamannya terhadap fikih."
            )
        )
    )

    // 7. Imam Ahmad bin Hanbal
    private val ahmad = ImamBiography(
        id = "Ahmad",
        shortName = "Imam Ahmad",
        arabicName = "الإِمَامُ أَحْمَدُ بْنُ حَنْبَلٍ رحمه الله",
        fullNameLatin = "Abu Abdillah Ahmad bin Muhammad",
        nasab = "bin Hanbal bin Hilal bin Asad Asy-Syaibani Al-Baghdadi",
        titles = listOf("IMAM AHLUSSUNNAH WAL JAMA'AH", "Syaikhul Islam"),
        categoryBadge = "9 Kitab Utama #7",
        periodHijri = "164 H - 241 H",
        periodMasehi = "780 M - 855 M / 77 Tahun",
        birthAndDeathPlace = "Baghdad \u2192 Baghdad",
        countryRegion = "Irak",
        targetBookSlug = "MusnadAhmad",
        metrics = ImamMetrics(
            totalHadithsDisplay = "26.363",
            totalHadithsDesc = "Ensiklopedia hadis terbesar dalam Islam",
            selectionPeriod = "40 Thn",
            selectionPeriodDesc = "Disaring dari 700.000+ riwayat hadis",
            teachersCount = "400+",
            teachersDesc = "Murid terkemuka Imam Asy-Syafi'i",
            chaptersCount = "1.000+",
            chaptersDesc = "Disusun berdasarkan nama sahabat perawi"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "164 H (780 M)",
                locationBadge = "Baghdad",
                title = "Kelahiran Pembela Sunnah",
                description = "Lahir di Baghdad dari kabilah Arab Bani Syaiban. Tumbuh sebagai anak yatim dalam kesederhanaan dan ketakwaan tinggi."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "179 H (795 M)",
                locationBadge = "Thalabul Ilmi",
                title = "Menghafal Ratusan Ribu Hadis",
                description = "Mulai menuntut ilmu hadis pada usia 15 tahun. Berjalan kaki ribuan kilometer ke Kufah, Bashrah, Makkah, Madinah, dan Yaman."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "218 H - 232 H",
                locationBadge = "Fitnah Khalqul Qur'an",
                title = "Keteguhan Aqidah dalam Mihnah",
                description = "Dipenjara dan dicambuk oleh para penguasa selama bertahun-tahun demi mempertahankan akidah bahwa Al-Qur'an adalah Kalamullah bukan makhluk."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "241 H (855 M)",
                locationBadge = "Baghdad",
                title = "Pemakaman Terbesar dalam Sejarah",
                description = "Wafat pada 12 Rabi'ul Awwal 241 H di usia 77 tahun. Jenazah beliau diantar oleh ratusan ribu kaum muslimin di jalanan Baghdad."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Sistematika Musnad per Sahabat",
                explanation = "Menyusun hadis berdasarkan nama sahabat perawi (Musnad Khulafaur Rasyidin, Musnad Abu Hurairah, dll.) untuk menghimpun seluruh riwayat mereka."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Mendahulukan Hadis Dha'if Ringan atas Ra'yu (Akal)",
                explanation = "Jika tidak ada hadis shahih dalam suatu hukum, beliau mendahulukan hadis dha'if yang tidak munkar daripada qiyas atau pendapat akal murni."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Kekuatan Verifikasi Sanad & Wara'",
                explanation = "Sangat wara' dalam menerima hadis dan mencoret periwayatan dari siapa pun yang terindikasi bid'ah atau berdusta."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Al-Musnad (Musnad Ahmad)",
                titleArabic = "الْمُسْنَدُ لِلْإِمَامِ أَحْمَدَ بْنِ حَنْبَلٍ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "26.363 Hadis",
                description = "Ensiklopedia hadis raksasa terlengkap dalam Islam, memuat puluhan ribu riwayat hadis dari ratusan sahabat.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Kitab Az-Zuhd",
                titleArabic = "كِتَابُ الزُّهْدِ",
                badgePrimary = "Penyucian Jiwa",
                description = "Kompilasi agung ucapan dan riwayat keteladanan para Nabi, sahabat, dan tabi'in dalam kezuhudan dunia."
            ),
            BiographyMajorWork(
                titleLatin = "Al-Masa'il",
                titleArabic = "مَسَائِلُ الْإِمَامِ أَحْمَدَ",
                badgePrimary = "Fiqih & Fatwa",
                description = "Kumpulan jawaban fatwa fiqih dan akidah yang dicatat oleh murid-murid terdekat beliau (Abu Daud, Al-Atsram, Al-Marwazi)."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Al-Imam Asy-Syafi'i",
                scholarTitle = "Pendiri Madzhab Syafi'i",
                quote = "Aku meninggalkan Baghdad dan tidak mendapati seorang pun yang lebih bertakwa, lebih faqih, dan lebih alim daripada Ahmad bin Hanbal."
            ),
            ScholarTestimonial(
                scholarName = "Ali bin Al-Madini",
                scholarTitle = "Ulama Ahli Kritik Hadis",
                quote = "Allah meneguhkan agama Islam ini melalui dua orang: Abu Bakar Ash-Shiddiq pada hari kemurtadan, dan Ahmad bin Hanbal pada hari mihnah."
            )
        )
    )

    // 8. Imam Malik bin Anas
    private val malik = ImamBiography(
        id = "Malik",
        shortName = "Imam Malik",
        arabicName = "الإِمَامُ مَالِكُ بْنُ أَنَسٍ رحمه الله",
        fullNameLatin = "Abu Abdillah Malik bin Anas",
        nasab = "bin Malik bin Abi Amir Al-Ashbahi Al-Madani",
        titles = listOf("IMAM DARIL HIJRAH", "Najm Ats-Tsaqib"),
        categoryBadge = "9 Kitab Utama #8",
        periodHijri = "93 H - 179 H",
        periodMasehi = "711 M - 795 M / 86 Tahun",
        birthAndDeathPlace = "Madinah \u2192 Jannatul Baqi'",
        countryRegion = "Hijaz (Arab Saudi)",
        targetBookSlug = "MuwathaMalik",
        metrics = ImamMetrics(
            totalHadithsDisplay = "1.841",
            totalHadithsDesc = "Kitab hadis & atsar tertua yang terpelihara",
            selectionPeriod = "40 Thn",
            selectionPeriodDesc = "Diuji dan disetujui 70 ulama Madinah",
            teachersCount = "900+",
            teachersDesc = "Berguru ke tabi'in senior Kota Madinah",
            chaptersCount = "61 Kitab",
            chaptersDesc = "Panduan hukum amaliyah warga Madinah"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "93 H (711 M)",
                locationBadge = "Madinah",
                title = "Kelahiran di Kota Nabi",
                description = "Lahir di Madinah Munawwarah dari keluarga mulia Yaman. Kakeknya adalah sahabat mulia Malik bin Abi Amir."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "110 H (728 M)",
                locationBadge = "Masjid Nabawi",
                title = "Berguru kepada Tabi'in Agung",
                description = "Menghafal Al-Qur'an dan hadis di bawah bimbingan Nafi' maula Ibnu Umar, Ibnu Syihab Az-Zuhri, dan Rabi'ah Ar-Ra'yi."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "140 H (757 M)",
                locationBadge = "Penulisan Muwatha'",
                title = "Menyusun Kitab Al-Muwatha'",
                description = "Menghabiskan waktu 40 tahun untuk menyaring dan menyusun Al-Muwatha' hingga menjadi kesepakatan seluruh ulama Hijaz."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "179 H (795 M)",
                locationBadge = "Baqi'",
                title = "Wafat & Dimakamkan di Baqi'",
                description = "Wafat pada 14 Rabi'ul Awwal 179 H di usia 86 tahun dan dimakamkan di pekuburan Baqi' berdampingan dengan para sahabat Nabi."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Silsilah Adz-Dzahab (Jalur Sanad Emas)",
                explanation = "Sanad beliau (Malik dari Nafi' dari Ibnu Umar dari Nabi SAW) dinobatkan oleh para imam hadis sebagai silsilah sanad paling shahih di muka bumi."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "'Amal Ahli Al-Madinah (Konsensus Warga Madinah)",
                explanation = "Menjadikan amalan turun-temurun warga Madinah sebagai dalil otentik mutawatir amali yang dapat menguatkan atau menafsirkan hadis ahad."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Pengagungan Luar Biasa terhadap Hadis Nabi",
                explanation = "Tidak pernah membacakan hadis kecuali dalam keadaan suci berwudhu, memakai pakaian terbaik, dan wewangian karena memuliakan sabda Rasulullah."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Al-Muwatha'",
                titleArabic = "الْمُوَطَّأُ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "1.841 Hadis",
                description = "Kitab hadis dan fikih paling otentik di zamannya sebelum kemunculan Shahih Bukhari.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Risalah fi Al-Qadar",
                titleArabic = "رِسَالَةُ مَالِكٍ فِي الْقَدَرِ",
                badgePrimary = "Aqidah Sunnah",
                description = "Surat pembelaan akidah Ahlussunnah mengenai takdir Allah membantah pemikiran Qadariyyah."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Al-Imam Asy-Syafi'i",
                scholarTitle = "Murid Terbesar Imam Malik",
                quote = "Jika para ulama hadis disebut, maka Imam Malik adalah bintangnya yang paling terang gemerlap."
            ),
            ScholarTestimonial(
                scholarName = "Sufyan bin Uyainah",
                scholarTitle = "Ulama Hadis Makkah",
                quote = "Semoga Allah merahmati Malik, betapa telitinya beliau dalam menyeleksi para perawi hadis."
            )
        )
    )

    // 9. Imam Ad-Darimi
    private val darimi = ImamBiography(
        id = "Darimi",
        shortName = "Imam Ad-Darimi",
        arabicName = "الإِمَامُ الدَّارِمِيُّ رحمه الله",
        fullNameLatin = "Abu Muhammad Abdullah bin Abdirrahman",
        nasab = "bin Al-Fadhl bin Bahram At-Tamimi As-Samarqandi",
        titles = listOf("SYAIKHUL ISLAM BI KHURASAN", "Al-Imam Al-Hafizh"),
        categoryBadge = "9 Kitab Utama #9",
        periodHijri = "181 H - 255 H",
        periodMasehi = "797 M - 869 M / 74 Tahun",
        birthAndDeathPlace = "Samarkand \u2192 Marwa",
        countryRegion = "Uzbekistan",
        targetBookSlug = "SunanDarimi",
        metrics = ImamMetrics(
            totalHadithsDisplay = "3.367",
            totalHadithsDesc = "Kitab Sunan dengan sanad tertinggi ('uluw)",
            selectionPeriod = "20 Thn",
            selectionPeriodDesc = "Dilengkapi Muqaddimah Adab Hadits",
            teachersCount = "200+",
            teachersDesc = "Guru dari Imam Muslim & Abu Daud",
            chaptersCount = "24 Kitab",
            chaptersDesc = "Rujukan utama hadis 'Ali tsulatsiyyat"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "181 H (797 M)",
                locationBadge = "Samarkand",
                title = "Kelahiran Sang Imam Samarkand",
                description = "Lahir di Samarkand dari kabilah Bani Tamim (Darim). Menjadi mercusuar ilmu di kawasan Asia Tengah."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "205 H (820 M)",
                locationBadge = "Rihlah Ilmiah",
                title = "Mengembara ke Hijaz, Syam, dan Irak",
                description = "Menemui guru-guru besar seperti Yazid bin Harun, Sa'id bin Amir, dan Abu Ashim An-Nabil."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "240 H (854 M)",
                locationBadge = "Samarkand",
                title = "Penulisan Al-Musnad Al-Jami'",
                description = "Menyusun kitab Sunan yang memuat mukadimah manhaj ilmu hadis dan hadis-hadis tsulatsiyyat (hanya 3 perawi ke Rasulullah)."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "255 H (869 M)",
                locationBadge = "Marwa",
                title = "Wafat di Hari Tarwiyah",
                description = "Wafat pada hari Tarwiyah (8 Dzulhijjah) 255 H di usia 74 tahun di kota Marwa dengan duka mendalam kaum muslimin."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Keutamaan 'Uluw Al-Isnad (Sanad Pendek)",
                explanation = "Kitab Sunan Ad-Darimi memuat banyak hadis tsulatsiyyat di mana jarak antara Ad-Darimi dengan Rasulullah SAW hanya 3 perawi."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Muqaddimah Metodologi & Adab Ilmu",
                explanation = "Satu-satunya kitab sunan induk yang diawali mukadimah komprehensif tentang adab menuntut ilmu, ittiba' sunnah, dan bahaya bid'ah."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Kekritisan terhadap Atsar Shahabat",
                explanation = "Menyeleksi atsar fatwa para sahabat dan tabi'in secara cermat untuk memperjelas asbabun nuzul dan konteks hadis."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Sunan Ad-Darimi (Al-Musnad Al-Jami')",
                titleArabic = "سُنَنُ الدَّارِمِيِّ (الْمُسْنَدُ الْجَامِعُ)",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "3.367 Hadis",
                description = "Koleksi hadis agung penyempurna 9 Kitab Utama dengan derajat keshahihan tinggi dan sanad ringkas.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Ats-Tsulatsiyyat",
                titleArabic = "ثُلَاثِيَّاتُ الدَّارِمِيِّ",
                badgePrimary = "Sanad 'Ali",
                description = "Kumpulan hadis-hadis istimewa yang hanya memiliki tiga generasi perawi antara Ad-Darimi dan Rasulullah SAW."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Al-Imam Ahmad bin Hanbal",
                scholarTitle = "Imam Ahlussunnah",
                quote = "Abdullah bin Abdirrahman Ad-Darimi adalah imam yang telah mengorbankan jiwa dan raganya untuk menjaga sunnah."
            ),
            ScholarTestimonial(
                scholarName = "Al-Imam Abu Hatim Ar-Razi",
                scholarTitle = "Ulama Ahli Jarh wa Ta'dil",
                quote = "Ad-Darimi adalah imam di zamannya, jujur, terpercaya, dan sangat menguasai seluk beluk hadis."
            )
        )
    )

    // 10. Imam Ad-Daruquthni
    private val daruquthni = ImamBiography(
        id = "Daruquthni",
        shortName = "Imam Ad-Daruquthni",
        arabicName = "الإِمَامُ الدَّارَقُطْنِيُّ رحمه الله",
        fullNameLatin = "Abul Hasan Ali bin Umar",
        nasab = "bin Ahmad bin Mahdi bin Mas'ud Al-Baghdadi Ad-Daruquthni",
        titles = listOf("AMIRUL MUKMININ FIL 'ILAL", "Al-Imam Al-Muqri' Al-Hafizh"),
        categoryBadge = "14 Kitab #10",
        periodHijri = "306 H - 385 H",
        periodMasehi = "918 M - 995 M / 79 Tahun",
        birthAndDeathPlace = "Darul Quthn (Baghdad) \u2192 Baghdad",
        countryRegion = "Irak",
        targetBookSlug = "SunanDaruquthni",
        metrics = ImamMetrics(
            totalHadithsDisplay = "4.898",
            totalHadithsDesc = "Rujukan utama kritik sanad dan cacat hadis",
            selectionPeriod = "30 Thn",
            selectionPeriodDesc = "Fokus pada hadis-hadis gharib & musykil",
            teachersCount = "300+",
            teachersDesc = "Pakar hadis dan qira'at Al-Qur'an terkemuka",
            chaptersCount = "38 Kitab",
            chaptersDesc = "Disusun sistematis untuk analisis perawi"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "306 H (918 M)",
                locationBadge = "Baghdad",
                title = "Kelahiran Sang Jenius 'Ilal",
                description = "Lahir di Darul Quthn (kampung di Baghdad). Sejak belia telah dianugerahi kecerdasan menghafal sanad secara terbalik maupun lurus."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "330 H (941 M)",
                locationBadge = "Majelis Baghdad",
                title = "Menguasai Ilmu Qira'at dan Hadis",
                description = "Menjadi rujukan nomor satu di Baghdad dalam ilmu qira'at Al-Qur'an, nahwu bahasa Arab, dan 'ilal hadis."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "360 H (970 M)",
                locationBadge = "Mesir & Syam",
                title = "Rihlah ke Mesir & Menulis Kitab Al-'Ilal",
                description = "Mengadakan rihlah ke Mesir dan membantu para wazir serta ulama dalam mentahqiq kitab-kitab induk hadis."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "385 H (995 M)",
                locationBadge = "Baghdad",
                title = "Wafat di Baghdad",
                description = "Wafat pada 8 Dzulqa'dah 385 H di usia 79 tahun dan dimakamkan di pekuburan Babud Dair dekat makam Ma'ruf Al-Karkhi."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Spesialis Pengujian Cacat Tersembunyi ('Ilal)",
                explanation = "Merupakan pakar tertinggi sepanjang zaman dalam mendeteksi wahm (kekeliruan), idraj (penyisipan lafazh), dan irsal khafiy pada sanad."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Penghimpunan Jalur Gharib untuk Pembuktian",
                explanation = "Menyusun Sunan-nya dengan menyajikan jalur-jalur hadis yang diperdebatkan para ulama agar diketahui letak kelemahan atau penguatnya."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Naqd Mutasyabih Asma' Rijal",
                explanation = "Sangat mahir membedakan nama-nama perawi hadis yang tulisannya sama namun pelafalannya berbeda (Al-Mu'talif wal Mukhtalif)."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Sunan Ad-Daruquthni",
                titleArabic = "سُنَنُ الدَّارَقُطْنِيِّ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "4.898 Hadis",
                description = "Kitab hadis rujukan utama para mujtahid dan kritikus sanad dalam meneliti keabsahan hadis-hadis hukum fikih.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Kitab Al-'Ilal Al-Waridah fil Ahadits",
                titleArabic = "الْعِلَلُ الْوَارِدَةُ فِي الْأَحَادِيثِ النَّبَوِيَّةِ",
                badgePrimary = "Masterpiece 'Ilal",
                description = "Karya ilmiah raksasa 11 jilid yang didektekan beliau dari hafalannya tanpa melihat naskah catatan satu kali pun."
            ),
            BiographyMajorWork(
                titleLatin = "Al-Mu'talif wal Mukhtalif",
                titleArabic = "الْمُؤْتَلِفُ وَالْمُخْتَلِفُ",
                badgePrimary = "Nama Perawi",
                description = "Karya rujukan nomor satu dalam meluruskan kekeliruan pembacaan nama, nasab, dan kunyah para perawi hadis."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Al-Hakim An-Naisaburi",
                scholarTitle = "Penulis Al-Mustadrak",
                quote = "Ad-Daruquthni adalah manusia yang tiada bandingannya di muka bumi dalam hafalan, kecermatan, dan ilmu cacat hadis."
            ),
            ScholarTestimonial(
                scholarName = "Al-Khatib Al-Baghdadi",
                scholarTitle = "Penulis Tarikh Baghdad",
                quote = "Ad-Daruquthni adalah imam di zamannya, rujukan akhir dalam ilmu rijal, fashih lisannya, dan terpercaya agamanya."
            )
        )
    )

    // 11. Imam Ibnu Khuzaimah
    private val ibnuKhuzaimah = ImamBiography(
        id = "Ibnu Khuzaimah",
        shortName = "Imam Ibnu Khuzaimah",
        arabicName = "الإِمَامُ ابْنُ خُزَيْمَةَ رحمه الله",
        fullNameLatin = "Abu Bakr Muhammad bin Ishaq",
        nasab = "bin Khuzaimah bin Al-Mughirah bin Mu'taz As-Sulami An-Naisaburi",
        titles = listOf("IMAMUL A'IMMAH (Imam Para Imam)", "Syaikhul Islam"),
        categoryBadge = "Kitab Shahih Muktabar",
        periodHijri = "223 H - 311 H",
        periodMasehi = "838 M - 924 M / 88 Tahun",
        birthAndDeathPlace = "Naisabur (Nishapur) \u2192 Naisabur",
        countryRegion = "Iran (Khurasan Raya)",
        targetBookSlug = "ShahihIbnuKhuzaimah",
        metrics = ImamMetrics(
            totalHadithsDisplay = "1.808",
            totalHadithsDesc = "Total hadis dalam manuskrip terpelihara",
            selectionPeriod = "40+ Thn",
            selectionPeriodDesc = "Dedikasi riset dan verifikasi keshahihan",
            teachersCount = "200+",
            teachersDesc = "Berguru ke Ishaq bin Rahawaih & murid Bukhari",
            chaptersCount = "Bab Tematik",
            chaptersDesc = "Disusun bab demi bab dengan fiqih mendalam"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "223 H (838 M)",
                locationBadge = "Naisabur",
                title = "Kelahiran di Kota Ilmu Khurasan",
                description = "Lahir di Naisabur pada bulan Shafar 223 H. Mempelajari Al-Qur'an dan dasar-dasar hadis sejak belia di lingkungan keluarga terpandang."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "240 H - 265 H",
                locationBadge = "Rihlah Akbar",
                title = "Pengembaraan ke Irak, Syam, Hijaz & Mesir",
                description = "Mengembara menuntut hadis ke Rayy, Baghdad, Bashrah, Kufah, Syam, Makkah, Madinah, hingga Mesir. Mengumpulkan sanad-sanad tertinggi dari para hafizh agung."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "280 H (893 M)",
                locationBadge = "Naisabur",
                title = "Gelar Kehormatan Imamul A'immah",
                description = "Diakui para ulama timur dan barat sebagai 'Imamul A'immah' (Imamnya para Imam) karena ketelitian analisis sanad dan fiqih hadisnya yang tiada tara."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "311 H (924 M)",
                locationBadge = "Naisabur",
                title = "Wafat di Usia 88 Tahun",
                description = "Wafat pada malam Sabtu 2 Dzulqa'dah 311 H. Dishalatkan oleh ribuan kaum muslimin dan dimakamkan di kamar rumahnya di Naisabur."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Syarat Keshahihan Ketat Setara Syaikhani",
                explanation = "Hanya meriwayatkan hadis dari perawi yang adil, dhabith mutqin, bersambung sanadnya tanpa tadlis kecuali dinyatakan mendengar langsung (tashrih bis sima')."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Kehati-hatian Luar Biasa (In Shahha Al-Khabar)",
                explanation = "Sering memberi catatan kritis 'In shahha al-khabar' (jika riwayat ini shahih) bila menemukan keraguan kecil pada sanad, menunjukkan integritas ilmiah yang sangat tinggi."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Keahlian Mengompromikan Hadis Bertentangan",
                explanation = "Terkemuka dalam mendamaikan hadis-hadis yang tampak kontradiktif (Al-Jam'u baina Al-Mukhtalif) sehingga tidak ada sabda Nabi yang terbuang sia-sia."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Mukhtashar al-Mukhtashar min al-Musnad ash-Shahih (Shahih Ibnu Khuzaimah)",
                titleArabic = "مُخْتَصَرُ الْمُخْتَصَرِ مِنَ الْمُسْنَدِ الصَّحِيحِ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "Tingkat Keshahihan Tinggi",
                description = "Kitab hadis shahih yang derajat otentisitasnya ditempatkan para ulama tepat di bawah Shahih Bukhari dan Muslim.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Kitabut Tauhid wa Itsbat Shifatir Rabb",
                titleArabic = "كِتَابُ التَّوْحِيدِ وَإِثْبَاتِ صِفَاتِ الرَّبِّ",
                badgePrimary = "Aqidah Salaf",
                description = "Karya agung pembelaan aqidah Ahlus Sunnah wal Jama'ah berdasarkan dalil-dalil hadis shahih."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Imam Adz-Dzahabi",
                scholarTitle = "Penulis Siyar A'lam An-Nubala",
                quote = "Ibnu Khuzaimah adalah Imamul A'immah, Syaikhul Islam, lautan ilmu, dan salah satu tokoh paling cemerlang dalam sejarah Islam."
            ),
            ScholarTestimonial(
                scholarName = "Abu Ali An-Naisaburi",
                scholarTitle = "Ulama Hadis Khurasan",
                quote = "Saya belum pernah melihat di bawah kolong langit ini orang yang memahami sunnah dan mampu menghafalnya seperti Ibnu Khuzaimah."
            )
        )
    )

    // 12. Imam Ibnu Hibban
    private val ibnuHibban = ImamBiography(
        id = "Ibnu Hibban",
        shortName = "Imam Ibnu Hibban",
        arabicName = "الإِمَامُ ابْنُ حِبَّانَ رحمه الله",
        fullNameLatin = "Abu Hatim Muhammad bin Hibban",
        nasab = "bin Ahmad bin Hibban bin Mu'adz At-Tamimi Ad-Darimi Al-Busti",
        titles = listOf("AL-IMAM AL-ALLAMAH", "Al-Hafizh Al-Hujjah", "Qadhi Samarkand"),
        categoryBadge = "Kitab Shahih Muktabar",
        periodHijri = "270 H - 354 H",
        periodMasehi = "884 M - 965 M / 84 Tahun",
        birthAndDeathPlace = "Bust (Sijistan) \u2192 Bust",
        countryRegion = "Afganistan (Khurasan Selatan)",
        targetBookSlug = "ShahihIbnuHibban",
        metrics = ImamMetrics(
            totalHadithsDisplay = "2.769",
            totalHadithsDesc = "Total hadis shahih dalam pustaka aplikasi",
            selectionPeriod = "30+ Thn",
            selectionPeriodDesc = "Rihlah ilmu dan klasifikasi ensiklopedis",
            teachersCount = "2.000+",
            teachersDesc = "Murid langsung Ibnu Khuzaimah & An-Nasa'i",
            chaptersCount = "5 Divisi Utama",
            chaptersDesc = "Terbagi ke dalam 400 jenis sub-bab"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "270 H (884 M)",
                locationBadge = "Bust (Sijistan)",
                title = "Kelahiran di Bust",
                description = "Lahir di kota Bust (wilayah Afganistan kini). Menghafal Al-Qur'an dan mendalami sastra Arab serta hadis sejak usia muda."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "295 H - 320 H",
                locationBadge = "Rihlah Ilmiah",
                title = "Menuntut Hadis dari Samarkand hingga Mesir",
                description = "Mengembara ke Khurasan, Irak, Hijaz, Syam, dan Mesir. Belajar intensif kepada Imam Ibnu Khuzaimah, Imam An-Nasa'i, dan Abu Ya'la Al-Maushili."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "330 H (942 M)",
                locationBadge = "Samarkand",
                title = "Menjabat Qadhi (Hakim Agung)",
                description = "Diangkat menjadi Qadhi di Samarkand, Naisabur, dan berbagai kota Khurasan Raya karena integritas moral dan keluasan fatwa fiqihnya."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "354 H (965 M)",
                locationBadge = "Bust",
                title = "Wafat di Tanah Kelahiran",
                description = "Wafat pada malam Jumat di Bust pada usia 84 tahun, mewariskan perpustakaan pribadi dan ratusan jilid kitab berharga."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Klasifikasi Inovatif At-Taqasim wal Anwa'",
                explanation = "Menyusun hadis bukan berdasarkan bab fikih biasa, melainkan 5 divisi konseptual: Perintah, Larangan, Berita (Ikhbar), Izin, dan Perbuatan Nabi SAW."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "5 Syarat Ketat Kesahihan Sanad",
                explanation = "Mensyaratkan perawi adil dalam agama, tsiqah dalam hafalan, sanad bersambung tanpa inqitha', selamat dari tadlis, dan steril dari 'illat."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Pakar Terkemuka Jarh wa Ta'dil",
                explanation = "Memiliki metodologi kritik perawi yang khas dan menyusun ensiklopedi biografi perawi hadis tepercaya (Kitab Ats-Tsiqat) dan bermasalah (Kitab Al-Majruhin)."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Al-Musnad ash-Shahih 'alat Taqasim wal Anwa' (Shahih Ibnu Hibban)",
                titleArabic = "صَحِيحُ ابْنِ حِبَّانَ (الْمُسْنَدُ الصَّحِيحُ)",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "Sistematis & Komprehensif",
                description = "Salah satu kitab himpunan hadis shahih paling terkemuka dalam khazanah Islam, dikenal dengan penataan tema yang sangat brilian.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Kitab Ats-Tsiqat",
                titleArabic = "كِتَابُ الثِّقَاتِ",
                badgePrimary = "Kritik Rijal",
                description = "Karya biografi ensiklopedis yang mendokumentasikan ribuan perawi hadis yang kredibel dan tepercaya."
            ),
            BiographyMajorWork(
                titleLatin = "Rawdhatul 'Uqala' wa Nuzhatul Fudhala'",
                titleArabic = "رَوْضَةُ الْعُقَلَاءِ وَنُزْهَةُ الْفُضَلَاءِ",
                badgePrimary = "Adab & Akhlaq",
                description = "Kitab adab, kebijaksanaan, dan pembinaan jiwa yang sarat dengan atsar dan nasihat hikmah."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Al-Hakim An-Naisaburi",
                scholarTitle = "Penulis Al-Mustadrak",
                quote = "Ibnu Hibban adalah salah satu benteng ilmu hadis, fiqih, bahasa, dan kalam di zamannya."
            ),
            ScholarTestimonial(
                scholarName = "Al-Hafizh Ibnu Hajar Al-Asqalani",
                scholarTitle = "Penulis Fathul Bari",
                quote = "Ibnu Hibban adalah imam hafizh yang sangat luas ilmunya dan memiliki kecermatan tinggi dalam meneliti sanad."
            )
        )
    )

    // 13. Imam Al-Hakim An-Naisaburi
    private val alHakim = ImamBiography(
        id = "Al-Hakim",
        shortName = "Imam Al-Hakim",
        arabicName = "الإِمَامُ الحَاكِمُ النَّيْسَابُورِيُّ رحمه الله",
        fullNameLatin = "Abu Abdillah Muhammad bin Abdillah",
        nasab = "bin Muhammad bin Hamdawaih bin Na'im Adh-Dhabbi An-Naisaburi (Ibnu Al-Bayyi')",
        titles = listOf("IMAMUL MUHADDITSIN", "Bapak Mushthalah Hadits", "Al-Hakim"),
        categoryBadge = "Kitab Mustadrak Muktabar",
        periodHijri = "321 H - 405 H",
        periodMasehi = "933 M - 1014 M / 84 Tahun",
        birthAndDeathPlace = "Naisabur \u2192 Naisabur",
        countryRegion = "Iran (Khurasan)",
        targetBookSlug = "AlMustadrakAlHakim",
        metrics = ImamMetrics(
            totalHadithsDisplay = "673",
            totalHadithsDesc = "Hadis pilihan mustadrak dalam pustaka aplikasi",
            selectionPeriod = "50+ Thn",
            selectionPeriodDesc = "Mengembara ke Irak, Hijaz, dan Khurasan",
            teachersCount = "1.000+",
            teachersDesc = "Murid utama Abu Ali An-Naisaburi & Ad-Daruquthni",
            chaptersCount = "50+ Kitab Fiqih",
            chaptersDesc = "Meliputi Ushul, Hukum, Fadhail, dan Tarikh"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "321 H (933 M)",
                locationBadge = "Naisabur",
                title = "Kelahiran di Keluarga Ahli Hadis",
                description = "Lahir pada bulan Rabi'ul Awwal 321 H. Ayah dan pamannya adalah ulama hadis terkemuka yang memperkenalkannya pada sima' hadis sejak usia kanak-kanak."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "341 H (952 M)",
                locationBadge = "Baghdad & Kufah",
                title = "Rihlah Akbar ke Jantung Khilafah",
                description = "Mengembara ke Irak pada usia 20 tahun, belajar kepada ulama besar Baghdad, Kufah, dan Bashrah, lalu melanjutkan perjalanan haji ke Hijaz."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "370 H - 395 H",
                locationBadge = "Naisabur",
                title = "Penulisan Karya Monumental Ilmu Hadis",
                description = "Menyusun kitab legendaris 'Ma'rifat 'Ulumil Hadits' dan 'Al-Mustadrak 'ala ash-Shahihain', menjadikannya rujukan nomor satu para muhadditsin."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "405 H (1014 M)",
                locationBadge = "Naisabur",
                title = "Wafat di Naisabur",
                description = "Wafat pada hari Rabu bulan Shafar 405 H di usia 84 tahun, setelah memimpin majelis hadis puluhan tahun dan melahirkan generasi ulama besar."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Konsep Istidrak (Melengkapi Keshahihan Syaikhani)",
                explanation = "Menghimpun hadis-hadis shahih yang memenuhi syarat keshahihan Bukhari dan Muslim ('Ala syarthi asy-Syaikhain) namun tidak sempat dicantumkan di kedua kitab tersebut."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Peletak Fondasi 52 Cabang Musthalah Hadits",
                explanation = "Tokoh pertama yang mengklasifikasikan ilmu hadis secara sistematis ke dalam 52 jenis cabang ilmu dalam kitab 'Ma'rifat 'Ulumil Hadits'."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Pengujian Jalur Periwayatan Khurasan & Irak",
                explanation = "Sangat cermat menelusuri rantai sanad muttashil yang beredar di wilayah Khurasan, Transoxiana, dan Irak."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Al-Mustadrak 'ala ash-Shahihain",
                titleArabic = "الْمُسْتَدْرَكُ عَلَى الصَّحِيحَيْنِ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "Koleksi Hadis Shahih",
                description = "Karya raksasa yang menghimpun ribuan hadis shahih sesuai kriteria Imam Bukhari dan Muslim.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Ma'rifat 'Ulum al-Hadits",
                titleArabic = "مَعْرِفَةُ عُلُومِ الْحَدِيثِ",
                badgePrimary = "Bapak Musthalah",
                description = "Kitab induk metodologi ilmu hadis pertama yang menjadi pedoman seluruh ulama musthalah setelahnya."
            ),
            BiographyMajorWork(
                titleLatin = "Tarikh Naisabur",
                titleArabic = "تَارِيخُ نَيْسَابُورَ",
                badgePrimary = "Sejarah & Rijal",
                description = "Karya sejarah terlengkap mengenai biografi para ulama, perawi, dan cendekiawan yang pernah menetap di Naisabur."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Al-Hafizh Ad-Daruquthni",
                scholarTitle = "Imam Ahli Hadis Baghdad",
                quote = "Al-Hakim adalah orang yang tsiqah, memiliki martabat mulia, dan hafalan hadisnya sangat luas."
            ),
            ScholarTestimonial(
                scholarName = "Al-Khatib Al-Baghdadi",
                scholarTitle = "Penulis Tarikh Baghdad",
                quote = "Al-Hakim adalah imam kaum mukminin dalam hadis, sangat teliti, dan menguasai seluruh cabang ilmu riwayah."
            )
        )
    )

    // 14. Imam Asy-Syafi'i
    private val syafii = ImamBiography(
        id = "Syafii",
        shortName = "Imam Asy-Syafi'i",
        arabicName = "الإِمَامُ الشَّافِعِيُّ رحمه الله",
        fullNameLatin = "Abu Abdillah Muhammad bin Idris",
        nasab = "bin Al-Abbas bin Utsman bin Syafi' bin As-Sa'ib bin Ubaid bin Abdi Yazid bin Hasyim bin Al-Muththalib",
        titles = listOf("NASHIRUS SUNNAH (Pembela Sunnah)", "Mujaddid Abad ke-2 H", "Peletak Ushul Fiqih"),
        categoryBadge = "Kitab Masanid Muktabar",
        periodHijri = "150 H - 204 H",
        periodMasehi = "767 M - 820 M / 54 Tahun",
        birthAndDeathPlace = "Gaza (Palestina) \u2192 Fusthat (Kairo, Mesir)",
        countryRegion = "Palestina \u2192 Hijaz \u2192 Mesir",
        targetBookSlug = "MusnadAsySyafii",
        metrics = ImamMetrics(
            totalHadithsDisplay = "1.800",
            totalHadithsDesc = "Total riwayat sanad 'Ali dalam Musnad beliau",
            selectionPeriod = "35+ Thn",
            selectionPeriodDesc = "Menggabungkan Ahlul Hadits & Ahlur Ra'yi",
            teachersCount = "80+",
            teachersDesc = "Murid terkemuka Imam Malik & Sufyan bin Uyainah",
            chaptersCount = "Seluruh Bab Fiqih",
            chaptersDesc = "Thaharah hingga Al-Umm & Ar-Risalah"
        ),
        timeline = listOf(
            BiographyTimelineNode(
                stepNumber = 1,
                yearBadge = "150 H (767 M)",
                locationBadge = "Gaza & Makkah",
                title = "Lahir Yatim & Hijrah ke Makkah",
                description = "Lahir di Gaza pada tahun wafatnya Imam Abu Hanifah. Dibawa ibunya ke Makkah, hidup sederhana, menghafal Al-Qur'an pada usia 7 tahun dan fasih bersastra Arab."
            ),
            BiographyTimelineNode(
                stepNumber = 2,
                yearBadge = "163 H (780 M)",
                locationBadge = "Madinah Munawwarah",
                title = "Menghafal Al-Muwatha' & Berguru ke Imam Malik",
                description = "Menghafal kitab Al-Muwatha' luar kepala dalam 9 hari, lalu berguru langsung kepada Imam Malik di Madinah hingga wafatnya sang guru besar."
            ),
            BiographyTimelineNode(
                stepNumber = 3,
                yearBadge = "180 H - 198 H",
                locationBadge = "Baghdad",
                title = "Rihlah ke Irak & Merumuskan Mazhab Qadim",
                description = "Belajar kepada murid Abu Hanifah (Muhammad bin Hasan Asy-Syaibani), memadukan madrasah hadis Hijaz dan madrasah ra'yi Irak, serta menyusun kitab Ar-Risalah."
            ),
            BiographyTimelineNode(
                stepNumber = 4,
                yearBadge = "199 H - 204 H",
                locationBadge = "Mesir",
                title = "Melahirkan Mazhab Jadid & Wafat di Fusthat",
                description = "Menetap di Mesir, mengimla'kan kitab Al-Umm, dan wafat pada malam Jumat akhir Rajab 204 H di usia 54 tahun. Makam beliau di Kairo senantiasa diziarahi."
            )
        ),
        methodology = listOf(
            BiographyMethodologyItem(
                number = 1,
                title = "Hujjiyyah Khabar Ahad & Sanad Bersambung",
                explanation = "Menetapkan kaidah ushuliyyah bahwa hadis ahad yang shahih wajib diamalkan dan menjadi hujjah hukum yang mengikat sebagaimana Al-Qur'an."
            ),
            BiographyMethodologyItem(
                number = 2,
                title = "Kaidah Emas: 'Idza Shahha Al-Hadits Fahuwa Madzhabi'",
                explanation = "Menyatakan prinsip terkenal: 'Bila suatu hadis telah terbukti shahih, maka itulah mazhabku, dan buanglah pendapatku yang menyelisihi hadis tersebut'."
            ),
            BiographyMethodologyItem(
                number = 3,
                title = "Sanad Tertinggi (Silsilah Dzahabiyyah)",
                explanation = "Meriwayatkan hadis melalui jalur emas terpendek: Asy-Syafi'i dari Malik dari Nafi' dari Abdullah bin Umar dari Rasulullah SAW."
            )
        ),
        majorWorks = listOf(
            BiographyMajorWork(
                titleLatin = "Musnad Al-Imam Asy-Syafi'i",
                titleArabic = "مُسْنَدُ الإِمَامِ الشَّافِعِيِّ",
                badgePrimary = "Kitab Rujukan Utama",
                badgeSecondary = "Sanad Emas 'Ali",
                description = "Koleksi hadis-hadis yang diriwayatkan Imam Asy-Syafi'i dalam karya-karya beliau yang dihimpun para muridnya.",
                isMainBook = true
            ),
            BiographyMajorWork(
                titleLatin = "Ar-Risalah",
                titleArabic = "الرِّسَالَةُ فِي أُصُولِ الْفِقْهِ",
                badgePrimary = "Kitab Ushul Pertama",
                description = "Kitab pertama dalam sejarah peradaban Islam yang merumuskan kaidah-kaidah Ushul Fiqih dan metodologi dalil hadis."
            ),
            BiographyMajorWork(
                titleLatin = "Kitab Al-Umm",
                titleArabic = "كِتَابُ الْأُمِّ",
                badgePrimary = "Ensiklopedi Fiqih",
                description = "Karya agung mazhab baru (jadid) yang menjadi mahakarya fikih perbandingan berbasis dalil sunnah shahihah."
            )
        ),
        scholarTestimonials = listOf(
            ScholarTestimonial(
                scholarName = "Imam Ahmad bin Hanbal",
                scholarTitle = "Penulis Musnad Ahmad",
                quote = "Asy-Syafi'i adalah laksana matahari bagi dunia dan laksana kesehatan bagi raga manusia. Siapa yang mampu menggantikannya?"
            ),
            ScholarTestimonial(
                scholarName = "Imam Sufyan bin 'Uyainah",
                scholarTitle = "Muhaddits Tanah Suci Makkah",
                quote = "Jika engkau melihat Muhammad bin Idris Asy-Syafi'i, sesungguhnya engkau telah melihat pemuda yang paling cerdas dan faqih di zamannya."
            )
        )
    )
}
