package com.nusa.hadithapp.data.data_source.local.catalog

import com.nusa.hadithapp.domain.model.musthalah.HujjahStatus
import com.nusa.hadithapp.domain.model.musthalah.MusthalahCategory
import com.nusa.hadithapp.domain.model.musthalah.MusthalahQuizOption
import com.nusa.hadithapp.domain.model.musthalah.MusthalahQuizQuestion

object MusthalahQuizBank {

    /**
     * Kumpulan bank soal kasus nyata dan kaidah ilmu hadis (30+ soal tematik)
     */
    val STATIC_SCENARIO_QUESTIONS: List<MusthalahQuizQuestion> = listOf(
        MusthalahQuizQuestion(
            id = "sc_marfu",
            questionPrompt = "Suatu hadits yang disandarkan secara tegas kepada Rasulullah ﷺ, baik berupa perkataan, perbuatan, maupun persetujuan (taqrir), dinamakan...",
            categoryBadge = "Sandaran Matan",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Marfu' (المَرْفُوْعُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Mauquf (المَوْقُوْفُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Maqthu' (المَقْطُوْعُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Qudsi (القُدْسِيُّ)", false)
            ),
            explanation = "Hadits Marfu' adalah segala riwayat yang disandarkan kepada Nabi Muhammad ﷺ, terlepas dari apakah sanadnya bersambung (muttashil) atau terputus (munqathi')."
        ),
        MusthalahQuizQuestion(
            id = "sc_mauquf",
            questionPrompt = "Riwayat perkataan atau perbuatan yang disandarkan kepada Sahabat Nabi ﷺ dan terhenti hanya pada Sahabat tersebut (tidak sampai kepada Rasulullah) dinamakan...",
            categoryBadge = "Sandaran Matan",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Mauquf (المَوْقُوْفُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Maqthu' (المَقْطُوْعُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Marfu' (المَرْفُوْعُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Mursal (المُرْسَلُ)", false)
            ),
            explanation = "Hadits Mauquf adalah riwayat yang berhenti pada sahabat Nabi, sering disebut juga sebagai atsar sahabat (misalnya fatwa atau ketetapan Umar bin Khattab ra.)."
        ),
        MusthalahQuizQuestion(
            id = "sc_maqthu",
            questionPrompt = "Perkataan, fatwa, atau ketetapan yang bersumber murni dari generasi Tabi'in atau generasi setelahnya dinamakan...",
            categoryBadge = "Sandaran Matan",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Maqthu' (المَقْطُوْعُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Munqathi' (المُنْقَطِعُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Mauquf (المَوْقُوْفُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Mu'dhal (المُعْضَلُ)", false)
            ),
            explanation = "Hadits Maqthu' adalah sandaran redaksi kepada Tabi'in. Jangan tertukar dengan 'Munqathi' (yang merupakan istilah keterputusan rantai sanad)."
        ),
        MusthalahQuizQuestion(
            id = "sc_qudsi",
            questionPrompt = "Hadits yang makna dan wahyunya berasal langsung dari Allah ﷻ, namun redaksi lafaznya disabdakan oleh Rasulullah ﷺ dengan awalan semisal 'Qalallahu Ta'ala' disebut...",
            categoryBadge = "Sandaran Matan",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Qudsi (الأَحَادِيثُ القُدْسِيَّةُ)", true),
                MusthalahQuizOption("opt_2", "Al-Qur'anul Karim", false),
                MusthalahQuizOption("opt_3", "Hadits Mutawatir", false),
                MusthalahQuizOption("opt_4", "Hadits Marfu' Hukmi", false)
            ),
            explanation = "Hadits Qudsi dinisbatkan maknanya kepada Allah namun redaksi katanya dari Nabi ﷺ. Berbeda dari Al-Qur'an, Hadits Qudsi tidak dibaca saat shalat dan tidak bernilai mukjizat lafaz."
        ),
        MusthalahQuizQuestion(
            id = "sc_muallaq",
            questionPrompt = "Jika seorang mukharrij/penulis kitab hadits menggugurkan seorang atau seluruh perawi di awal sanadnya (misal langsung berkata: 'Berkata Az-Zuhri...'), hadits ini dinamakan...",
            categoryBadge = "Kondisi Sanad",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Mu'allaq (المُعَلَّقُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Mursal (المُرْسَلُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Mu'dhal (المُعْضَلُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Mudallas (المُدَلَّسُ)", false)
            ),
            explanation = "Hadits Mu'allaq (tergantung) adalah hadits yang gugur satu atau lebih perawinya secara berurutan dari awal sanad (sisi penulis kitab)."
        ),
        MusthalahQuizQuestion(
            id = "sc_mursal",
            questionPrompt = "Seorang Tabi'in (misal Sa'id bin al-Musayyab) langsung berkata: 'Rasulullah ﷺ bersabda...', tanpa menyebutkan nama Sahabat yang menjadi perantaranya. Hadits semacam ini disebut...",
            categoryBadge = "Kondisi Sanad",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Mursal (المُرْسَلُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Mu'allaq (المُعَلَّقُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Munqathi' (المُنْقَطِعُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Syadz (الشَّاذُّ)", false)
            ),
            explanation = "Hadits Mursal adalah hadits yang digugurkan perawinya setelah tingkatan Tabi'in, yaitu nama sahabat Nabi tidak disebutkan."
        ),
        MusthalahQuizQuestion(
            id = "sc_mudhal",
            questionPrompt = "Keadaan di mana dua orang perawi atau lebih gugur secara berturut-turut di tengah rantai sanad dinamakan...",
            categoryBadge = "Kondisi Sanad",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Mu'dhal (المُعْضَلُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Munqathi' (المُنْقَطِعُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Mudallas (المُدَلَّسُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Mursal (المُرْسَلُ)", false)
            ),
            explanation = "Hadits Mu'dhal adalah hadits yang terputus dua perawi atau lebih secara berturut-turut. Derajat kelemahannya lebih berat daripada munqathi' biasa."
        ),
        MusthalahQuizQuestion(
            id = "sc_munqathi",
            questionPrompt = "Hadits yang terputus satu orang perawi di bagian mana pun dalam sanad (selain di awal sanad dan selain di tingkatan Sahabat) disebut...",
            categoryBadge = "Kondisi Sanad",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Munqathi' (المُنْقَطِعُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Maqthu' (المَقْطُوْعُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Mu'dhal (المُعْضَلُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Munkar (المُنْكَرُ)", false)
            ),
            explanation = "Hadits Munqathi' adalah istilah umum untuk sanad yang tidak tersambung dengan gugurnya satu perawi tidak berurutan."
        ),
        MusthalahQuizQuestion(
            id = "sc_mudallas",
            questionPrompt = "Tindakan perawi yang menyembunyikan kecacatan dalam sanad atau menyamarkan nama gurunya dengan julukan yang tidak populer agar dianggap banyak guru disebut...",
            categoryBadge = "Kondisi Sanad",
            options = listOf(
                MusthalahQuizOption("opt_1", "Tadlis / Hadits Mudallas (المُدَلَّسُ)", true),
                MusthalahQuizOption("opt_2", "Tashhif (التَّصْحِيْفُ)", false),
                MusthalahQuizOption("opt_3", "Idraj (الإِدْرَاجُ)", false),
                MusthalahQuizOption("opt_4", "Irsal Khafi (الإِرْسَالُ الخَفِيُّ)", false)
            ),
            explanation = "Tadlis terbagi menjadi Tadlis Isnad (menggugurkan guru lemah dengan kata samar) dan Tadlis Syuyukh (menyebut guru dengan nama asing)."
        ),
        MusthalahQuizQuestion(
            id = "sc_syadz",
            questionPrompt = "Hadits yang diriwayatkan oleh seorang perawi yang terpercaya (tsiqah), namun isi riwayatnya bertentangan dengan riwayat perawi yang jauh lebih tsiqah atau jama'ah perawi lainnya, disebut...",
            categoryBadge = "Derajat & Kualitas",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Syadz (الشَّاذُّ)", true),
                MusthalahQuizOption("opt_2", "Hadits Munkar (المُنْكَرُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Mahfuzh (المَحْفُوْظُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Ma'lul (المَعْلُوْلُ)", false)
            ),
            explanation = "Hadits Syadz tertolak karena terdapat unsur pertentangan (mukhalafah) terhadap riwayat yang lebih kuat. Lawan dari Syadz adalah riwayat yang benar, yaitu Mahfuzh."
        ),
        MusthalahQuizQuestion(
            id = "sc_munkar",
            questionPrompt = "Hadits yang diriwayatkan oleh perawi yang lemah (dha'if), dan riwayatnya tersebut bertentangan dengan riwayat perawi yang terpercaya (tsiqah), disebut...",
            categoryBadge = "Derajat & Kualitas",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Munkar (المُنْكَرُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Syadz (الشَّاذُّ)", false),
                MusthalahQuizOption("opt_3", "Hadits Ma'ruf (المَعْرُوْفُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Mudraj (المُدْرَجُ)", false)
            ),
            explanation = "Hadits Munkar terjadi bila perawi yang lemah menyelisihi perawi tsiqah. Lawan dari Munkar adalah Ma'ruf (riwayat perawi terpercaya yang diakui)."
        ),
        MusthalahQuizQuestion(
            id = "sc_maudhu",
            questionPrompt = "Tingkatan hadits yang paling buruk dan haram diriwayatkan kecuali untuk menjelaskan kepalsuannya adalah...",
            categoryBadge = "Derajat & Kualitas",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Maudhu' / Palsu (المَوْضُوْعُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Dha'if Jiddan (الضَّعِيْفُ جِدًّا)", false),
                MusthalahQuizOption("opt_3", "Hadits Munkar (المُنْكَرُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Matruk (المَتْرُوْكُ)", false)
            ),
            explanation = "Hadits Maudhu' adalah riwayat bohong yang diada-adakan atas nama Rasulullah ﷺ. Ijma' ulama mengharamkan periwayatannya tanpa membongkar kepalsuannya."
        ),
        MusthalahQuizQuestion(
            id = "sc_shahih_lighairih",
            questionPrompt = "Hadits Hasan Lidzatihi yang terangkat derajatnya menjadi setara dengan Hadits Shahih karena didukung oleh jalur sanad lain yang setara atau lebih kuat dinamakan...",
            categoryBadge = "Derajat & Kualitas",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Shahih Lighairih (صَحِيْحٌ لِغَيْرِهِ)", true),
                MusthalahQuizOption("opt_2", "Hadits Hasan Lighairih (حَسَنٌ لِغَيْرِهِ)", false),
                MusthalahQuizOption("opt_3", "Hadits Mutawatir Lafzhi", false),
                MusthalahQuizOption("opt_4", "Hadits Masyhur", false)
            ),
            explanation = "Kekurangan sedikit pada kekuatan hafalan perawi hasan lidzatih tertutupi ketika datang sanad pendukung lain, sehingga naik kelas menjadi Shahih Lighairih."
        ),
        MusthalahQuizQuestion(
            id = "sc_hasan_lighairih",
            questionPrompt = "Hadits dha'if dengan kelemahan ringan (bukan pendusta atau fasik) yang naik tingkat menjadi dapat diterima (hasan) karena ditopang oleh beberapa riwayat lain disebut...",
            categoryBadge = "Derajat & Kualitas",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Hasan Lighairih (حَسَنٌ لِغَيْرِهِ)", true),
                MusthalahQuizOption("opt_2", "Hadits Shahih Lighairih (صَحِيْحٌ لِغَيْرِهِ)", false),
                MusthalahQuizOption("opt_3", "Hadits Hasan Lidzatih (حَسَنٌ لِذَاتِهِ)", false),
                MusthalahQuizOption("opt_4", "Hadits Aziz (العَزِيْزُ)", false)
            ),
            explanation = "Hadits Hasan Lighairih adalah hadits dha'if ringan yang memiliki jalur mutaba'ah atau syahid yang menguatkan keraguan dalam riwayat aslinya."
        ),
        MusthalahQuizQuestion(
            id = "sc_mutawatir",
            questionPrompt = "Hadits yang diriwayatkan oleh orang banyak di setiap tingkatan sanad, sehingga secara logika dan kebiasaan mustahil mereka bersepakat untuk berdusta, disebut...",
            categoryBadge = "Kuantitas Jalur",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Mutawatir (المُتَوَاتِرُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Ahad (الآحَادُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Masyhur (المَشْهُوْرُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Aziz (العَزِيْزُ)", false)
            ),
            explanation = "Hadits Mutawatir memberikan faedah ilmu yakin qath'i (pasti benar dari Rasulullah ﷺ) dan tidak perlu lagi diperiksa keadilan masing-masing individunya."
        ),
        MusthalahQuizQuestion(
            id = "sc_aziz",
            questionPrompt = "Hadits yang diriwayatkan oleh minimal dua orang perawi pada salah satu tingkatan sanadnya dinamakan...",
            categoryBadge = "Kuantitas Jalur",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits 'Aziz (العَزِيْزُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Gharib (الغَرِيْبُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Masyhur (المَشْهُوْرُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Musalsal (المُسَلْسَلُ)", false)
            ),
            explanation = "Hadits 'Aziz adalah hadits yang jalurnya tidak kurang dari dua rawi di seluruh atau sebagian tingkatan thabaqah sanad."
        ),
        MusthalahQuizQuestion(
            id = "sc_gharib",
            questionPrompt = "Hadits yang hanya diriwayatkan oleh satu orang perawi saja pada salah satu tingkatan thabaqah sanad dinamakan...",
            categoryBadge = "Kuantitas Jalur",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Gharib (الغَرِيْبُ / الفَرْدُ)", true),
                MusthalahQuizOption("opt_2", "Hadits 'Aziz (العَزِيْزُ)", false),
                MusthalahQuizOption("opt_3", "Hadits Masyhur (المَشْهُوْرُ)", false),
                MusthalahQuizOption("opt_4", "Hadits Dha'if", false)
            ),
            explanation = "Gharib berarti menyendiri (tafarrud). Keterasingan jumlah perawi tidak otomatis menjadikannya dha'if, contohnya hadits 'Innamal a'malu binniyyat' yang awalnya gharib pada tingkatan sahabat."
        ),
        MusthalahQuizQuestion(
            id = "sc_masyhur",
            questionPrompt = "Hadits yang diriwayatkan oleh minimal 3 orang perawi pada setiap tingkatan sanad, namun belum mencapai derajat mutawatir, disebut...",
            categoryBadge = "Kuantitas Jalur",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Masyhur / Mustafidh (المَشْهُوْرُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Mutawatir", false),
                MusthalahQuizOption("opt_3", "Hadits 'Aziz", false),
                MusthalahQuizOption("opt_4", "Hadits Gharib", false)
            ),
            explanation = "Hadits Masyhur memiliki jalur 3 perawi atau lebih di tiap thabaqat sanadnya, namun belum memenuhi kriteria jumlah hadits mutawatir."
        ),
        MusthalahQuizQuestion(
            id = "sc_musalsal",
            questionPrompt = "Hadits yang setiap perawinya meriwayatkan dengan redaksi, sifat, atau perbuatan yang serupa (misal setiap rawi berkata 'ia memegang tanganku' atau 'ia tersenyum') disebut...",
            categoryBadge = "Metode Riwayat",
            options = listOf(
                MusthalahQuizOption("opt_1", "Hadits Musalsal (المُسَلْسَلُ)", true),
                MusthalahQuizOption("opt_2", "Hadits Mutawatir", false),
                MusthalahQuizOption("opt_3", "Hadits Mu'an'an", false),
                MusthalahQuizOption("opt_4", "Hadits Musnad", false)
            ),
            explanation = "Hadits Musalsal adalah keunikan periwayatan di mana terdapat keseragaman keadaan para perawi ketika menyampaikan hadits tersebut."
        ),
        MusthalahQuizQuestion(
            id = "sc_syarat_shahih",
            questionPrompt = "Manakah di antara pilihan berikut yang BUKAN merupakan salah satu dari 5 syarat hadits Shahih Lidzatihi?",
            categoryBadge = "Derajat & Kualitas",
            options = listOf(
                MusthalahQuizOption("opt_1", "Diriwayatkan minimal oleh 10 perawi di setiap tingkatan", true),
                MusthalahQuizOption("opt_2", "Ketersambungan sanad (Ittishal as-Sanad)", false),
                MusthalahQuizOption("opt_3", "Keadilan perawi ('Adalah ar-Ruwat)", false),
                MusthalahQuizOption("opt_4", "Terhindar dari cacat tersembunyi ('Adam al-'Illah)", false)
            ),
            explanation = "5 Rukun Shahih Lidzatihi adalah: (1) Sanad bersambung, (2) Perawi adil, (3) Hafalan sempurna (dhabith), (4) Tidak syadz, dan (5) Bebas dari 'illat. Jumlah perawi 10 adalah syarat mutawatir, bukan syarat shahih."
        ),
        MusthalahQuizQuestion(
            id = "sc_dhaif_hukum",
            questionPrompt = "Bagaimanakah sikap jumhur ulama hadits terhadap pengamalan Hadits Dha'if?",
            categoryBadge = "Derajat & Kualitas",
            options = listOf(
                MusthalahQuizOption("opt_1", "Boleh diamalkan hanya pada Fadhailul A'mal dengan syarat kelemahan tidak berat", true),
                MusthalahQuizOption("opt_2", "Wajib diamalkan dalam seluruh masalah aqidah dan hukum fiqih", false),
                MusthalahQuizOption("opt_3", "Sama sekali tidak boleh diamalkan dalam hal apa pun tanpa pengecualian", false),
                MusthalahQuizOption("opt_4", "Boleh diamalkan untuk menetapkan hukum halal dan haram baru", false)
            ),
            explanation = "Ibnu Hajar merumuskan 3 syarat mengamalkan hadits dha'if: (1) Kelemahan tidak parah, (2) Masuk di bawah dalil pokok yang shahih, (3) Diamalkan hanya untuk fadhailul a'mal (keutamaan amal), bukan aqidah atau halal-haram."
        ),
        MusthalahQuizQuestion(
            id = "sc_tsiqah",
            questionPrompt = "Seorang perawi hadits yang memiliki integritas ketakwaan moral yang lurus serta kekuatan hafalan/catatan yang sempurna digelari sebagai...",
            categoryBadge = "Kredibilitas Perawi",
            options = listOf(
                MusthalahQuizOption("opt_1", "Tsiqah Dhabith (الثِّقَةُ الضَّابِطُ)", true),
                MusthalahQuizOption("opt_2", "Shaduq (الصَّدُوْقُ)", false),
                MusthalahQuizOption("opt_3", "Majhul (المَجْهُوْلُ)", false),
                MusthalahQuizOption("opt_4", "Mudallis (المُدَلِّسُ)", false)
            ),
            explanation = "Tsiqah menggabungkan dua rukun utama perawi: 'Adalah (integritas agama & akhlak) dan Dhabth (ketelitian daya ingat dan penjagaan tulisan)."
        )
    )

    /**
     * Mengacak dan menghasilkan sesi kuis yang sangat variatif:
     * Menggabungkan bank skenario kasus nyata + pertanyaan definisi prosedural dari seluruh istilah katalog.
     */
    fun generateDiverseQuizSession(count: Int = 5): List<MusthalahQuizQuestion> {
        val staticPool = STATIC_SCENARIO_QUESTIONS.shuffled()
        val proceduralPool = MusthalahCatalog.ALL_TERMS.shuffled().mapIndexed { index, term ->
            val prompt = if (index % 2 == 0) {
                "Manakah istilah ilmu hadits yang tepat untuk definisi:\n\n\"${term.shortDefinition}\"?"
            } else {
                "Bagaimanakah kedudukan kehujjahan hukum dari \"${term.latinTitle}\" (${term.arabicTitle})?"
            }

            val options: List<MusthalahQuizOption>
            if (index % 2 == 0) {
                val distractors = MusthalahCatalog.ALL_TERMS
                    .filter { it.id != term.id }
                    .shuffled()
                    .take(3)
                    .map { MusthalahQuizOption(it.id, "${it.latinTitle} (${it.arabicTitle})", false) }
                val correct = MusthalahQuizOption(term.id, "${term.latinTitle} (${term.arabicTitle})", true)
                options = (distractors + correct).shuffled()
            } else {
                val correctStatus = term.hujjahStatus.label
                val otherStatuses = HujjahStatus.entries.map { it.label }.filter { it != correctStatus }.shuffled().take(3)
                val distractors = otherStatuses.mapIndexed { i, s -> MusthalahQuizOption("st_$i", s, false) }
                val correct = MusthalahQuizOption("st_correct", correctStatus, true)
                options = (distractors + correct).shuffled()
            }

            MusthalahQuizQuestion(
                id = "proc_${term.id}_$index",
                questionPrompt = prompt,
                categoryBadge = term.category.displayName,
                options = options,
                explanation = "${term.latinTitle} (${term.arabicTitle}): ${term.shortDefinition}\n\nKaidah: ${term.hukumHujjah}"
            )
        }

        // Gabungkan bank soal skenario (prioritas utama) + prosedural
        val combinedPool = (staticPool + proceduralPool).shuffled()
        return combinedPool.take(minOf(count, combinedPool.size)).map { question ->
            // Pastikan urutan 4 opsi selalu diacak setiap kali kuis dimulai
            question.copy(options = question.options.shuffled())
        }
    }
}
