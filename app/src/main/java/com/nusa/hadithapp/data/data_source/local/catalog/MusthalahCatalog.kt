package com.nusa.hadithapp.data.data_source.local.catalog

import com.nusa.hadithapp.domain.model.musthalah.HujjahStatus
import com.nusa.hadithapp.domain.model.musthalah.MusthalahCategory
import com.nusa.hadithapp.domain.model.musthalah.MusthalahTerm

object MusthalahCatalog {

    val ALL_TERMS: List<MusthalahTerm> = listOf(
        // ==========================================
        // 1. DERAJAT & KUALITAS HADITS
        // ==========================================
        MusthalahTerm(
            id = "shahih_lidzatih",
            latinTitle = "Shahih Lidzatih",
            arabicTitle = "حَدِيْثٌ صَحِيْحٌ لِذَاتِهِ",
            category = MusthalahCategory.DERAJAT,
            hujjahStatus = HujjahStatus.SHAHIH_HUJJAH,
            shortDefinition = "Hadits yang sanadnya bersambung, diriwayatkan oleh perawi yang adil dan dhabith sempurna, terhindar dari kejanggalan (syadz) dan cacat ('illat).",
            fullExplanation = "Shahih Lidzatih merupakan kasta tertinggi otentisitas riwayat dalam ilmu hadits. Keshahihannya melekat pada sanad dan matannya sendiri tanpa memerlukan dukungan jalur riwayat lain. Seluruh ulama salaf dan khalaf bersepakat bahwa hadits jenis ini wajib diterima dan dijadikan pedoman dalam aqidah maupun syariat.",
            criteria = listOf(
                "Ittishal as-Sanad: Setiap perawi mendengar langsung riwayat dari gurunya secara berkesinambungan hingga Rasulullah ﷺ.",
                "'Adalah ar-Ruwat: Setiap perawi muslim, baligh, berakal, bertaqwa, dan menjauhi dosa besar maupun perusak muru'ah.",
                "Dhabth at-Tamm: Hafalan perawi sangat kokoh (dhabth shadr) atau catatannya sangat terjaga (dhabth kitab).",
                "'Adam asy-Syudzudz: Matan dan sanad tidak menyelisihi riwayat perawi yang lebih tsiqah darinya.",
                "'Adam al-'Illah: Bersih dari cacat tersembunyi yang dapat merusak validitas hadits."
            ),
            hukumHujjah = "Wajib diamalkan dan menjadi hujjah pokok dalam aqidah, ibadah, fiqih, dan seluruh sendi hukum Islam.",
            example = "Hadits: 'Amalan itu tergantung niatnya...' (HR. Bukhari no. 1 dan Muslim no. 1907)."
        ),
        MusthalahTerm(
            id = "shahih_lighairih",
            latinTitle = "Shahih Lighairih",
            arabicTitle = "حَدِيْثٌ صَحِيْحٌ لِغَيْرِهِ",
            category = MusthalahCategory.DERAJAT,
            hujjahStatus = HujjahStatus.SHAHIH_HUJJAH,
            shortDefinition = "Hadits hasan lidzatih yang terangkat derajatnya menjadi shahih karena diperkuat oleh jalur periwayatan lain yang setara atau lebih kuat.",
            fullExplanation = "Pada asalnya, hadits ini berada pada tingkatan hasan lidzatih karena hafalan salah satu perawinya sedikit di bawah derajat dhabith sempurna. Namun ketika riwayat serupa datang melalui jalur sanad lain yang valid, keraguan atas potensi kekeliruan hafalan perawi tersebut sirna, sehingga statusnya terangkat menjadi shahih lighairih (shahih karena faktor pendukung luar).",
            criteria = listOf(
                "Memenuhi seluruh kriteria hasan lidzatih pada sanad utamanya.",
                "Ditemukan sanad jalur lain (mutaba'ah atau syahid) yang setara atau lebih kuat.",
                "Tidak bertentangan dengan dalil syar'i yang lebih qath'i."
            ),
            hukumHujjah = "Setara dengan hadits Shahih Lidzatih dalam hal kehujjahan; wajib diamalkan dan sah dijadikan landasan hukum.",
            example = "Hadits bersiwak: 'Kalaulah tidak memberatkan umatku, niscaya aku perintahkan mereka bersiwak setiap kali hendak shalat.' (HR. At-Tirmidzi)."
        ),
        MusthalahTerm(
            id = "hasan_lidzatih",
            latinTitle = "Hasan Lidzatih",
            arabicTitle = "حَدِيْثٌ حَسَنٌ لِذَاتِهِ",
            category = MusthalahCategory.DERAJAT,
            hujjahStatus = HujjahStatus.HASAN_HUJJAH,
            shortDefinition = "Hadits yang bersambung sanadnya, diriwayatkan oleh perawi adil yang hafalannya sedikit di bawah dhabith sempurna, tanpa syadz dan tanpa 'illat.",
            fullExplanation = "Hasan Lidzatih berada satu tingkat di bawah Shahih Lidzatih. Perbedaannya semata-mata terletak pada kadar ketelitian hafalan perawinya (khiffat adh-dhabth), sedangkan keadilan moral, ketersambungan sanad, serta kebersihan dari syadz dan 'illat sama persis dengan hadits shahih.",
            criteria = listOf(
                "Sanad bersambung dari awal hingga akhir riwayat.",
                "Perawi bersifat adil dan amanah.",
                "Kekuatan hafalan perawi berada pada level sedang (shaduq / khofifa dhabtuhu).",
                "Terbebas dari syadz (kejanggalan pertentangan).",
                "Terbebas dari 'illat (cacat tersembunyi)."
            ),
            hukumHujjah = "Sah dan valid sebagai dalil hukum syariat menurut jumhur ulama.",
            example = "Hadits-hadits riwayat Muhammad bin 'Amr dari Abu Salamah dari Abu Hurairah."
        ),
        MusthalahTerm(
            id = "hasan_lighairih",
            latinTitle = "Hasan Lighairih",
            arabicTitle = "حَدِيْثٌ حَسَنٌ لِغَيْرِهِ",
            category = MusthalahCategory.DERAJAT,
            hujjahStatus = HujjahStatus.SYARAT_PENGUAT,
            shortDefinition = "Hadits dha'if ringan yang terangkat derajatnya menjadi hasan karena didukung oleh riwayat lain melalui jalur yang berbeda.",
            fullExplanation = "Hadits ini awalnya berstatus dha'if dengan kelemahan ringan (seperti keterputusan sanad yang tidak parah atau perawi yang hafalannya kurang bagus). Kelemahan ini bukan karena tuduhan dusta atau kefasikan. Ketika terdapat jalur sanad lain yang senada, kelemahan individual tersebut saling menopang dan menguatkan.",
            criteria = listOf(
                "Kelemahan pada sanad utama tergolong ringan (misal: dho'ful hifzhi, irsal, atau tadlis).",
                "Perawi bukan pendusta (kadzdzab) atau tertuduh dusta (muttaham bil kizb).",
                "Didukung oleh satu atau lebih jalur sanad lain yang selevel atau lebih tinggi."
            ),
            hukumHujjah = "Diterima sebagai dalil syar'i dan boleh dijadikan landasan hukum dan fatwa.",
            example = "Hadits: 'Menuntut ilmu adalah kewajiban bagi setiap muslim.' (Diriwayatkan melalui banyak jalur yang saling menguatkan)."
        ),
        MusthalahTerm(
            id = "dhaif",
            latinTitle = "Dha'if",
            arabicTitle = "حَدِيْثٌ ضَعِيْفٌ",
            category = MusthalahCategory.DERAJAT,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Hadits yang tidak memenuhi satu atau lebih kriteria syarat hadits shahih maupun hadits hasan.",
            fullExplanation = "Kelemahan hadits dha'if bisa berasal dari keterputusan sanad (seperti mu'allaq, mursal, munqathi') atau cacat pada kredibilitas perawi (seperti hafalan buruk, kebodohan identitas/majhul, bid'ah, atau kekeliruan fatal). Tingkat kedha'ifannya bertingkat-tingkat dari ringan (dha'if yusir) hingga sangat berat (dha'if jiddan).",
            criteria = listOf(
                "Gugurnya salah satu syarat keshahihan (sanad terputus atau perawi tidak adil/dhabith).",
                "Terdapat cacat keraguan ('illat) atau menyelisihi riwayat yang lebih kuat (syadz)."
            ),
            hukumHujjah = "Tidak boleh dijadikan dalil dalam aqidah dan penetapan halal-haram. Sebagian ulama membolehkan meriwayatkannya untuk fadhailul a'mal (keutamaan amal) dengan syarat kelemahannya tidak berat dan tidak diyakini secara pasti berasal dari Nabi ﷺ.",
            example = "Hadits: 'Tuntutlah ilmu walau ke negeri Cina.' (Sanadnya dha'if jiddan/lemah sekali)."
        ),
        MusthalahTerm(
            id = "maudhu",
            latinTitle = "Maudhu' (Hadits Palsu)",
            arabicTitle = "حَدِيْثٌ مَوْضُوْعٌ",
            category = MusthalahCategory.DERAJAT,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Kabar bohong yang dibuat-buat dan direkayasa lalu disandarkan secara dusta kepada Rasulullah ﷺ.",
            fullExplanation = "Hadits maudhu' pada hakikatnya bukanlah hadits, melainkan kebohongan yang diatasnamakan Nabi ﷺ. Pemalsuan ini terjadi sepanjang sejarah karena motif politik firqah, kefanatikan mazhab, zindiq perusak agama, atau niat keliru orang dungu yang ingin memotivasi ibadah dengan hadits buatan.",
            criteria = listOf(
                "Terdapat perawi pendusta (kadzdzab) atau pemalsu hadits (waddha') dalam sanad.",
                "Pengakuan langsung dari pembuat hadits palsu.",
                "Redaksi bertentangan secara vulgar dengan akal sehat, Al-Qur'an, atau fakta sejarah yang pasti."
            ),
            hukumHujjah = "Haram meriwayatkan dan menyebarkannya kecuali disertai penjelasan tegas bahwa hadits tersebut palsu.",
            example = "Hadits: 'Cinta tanah air adalah sebagian dari iman.' (Dinyatakan maudhu' oleh pakar hadits seperti As-Saghani)."
        ),
        MusthalahTerm(
            id = "muttafaq_alaih",
            latinTitle = "Muttafaq 'Alaih",
            arabicTitle = "مُتَّفَقٌ عَلَيْهِ",
            category = MusthalahCategory.DERAJAT,
            hujjahStatus = HujjahStatus.SHAHIH_HUJJAH,
            shortDefinition = "Hadits yang disepakati keshahihannya dan diriwayatkan bersama oleh Imam Al-Bukhari dan Imam Muslim dalam kitab Shahih keduanya.",
            fullExplanation = "Muttafaq 'Alaih menduduki puncak tertinggi hierarki otentisitas hadits (ashahhul ahadits). Kesepakatan dua maestro hadits terbesar ini atas matan yang bersumber dari sahabat yang sama memberikan kepastian ilmu yang menentramkan seluruh umat Islam.",
            criteria = listOf(
                "Termaktub dalam naskah asli Shahih Al-Bukhari dan Shahih Muslim.",
                "Berasal dari periwayatan sahabat Nabi yang sama."
            ),
            hukumHujjah = "Memiliki kekuatan hujjah paling mutlak setelah Al-Qur'an; wajib diterima tanpa keraguan.",
            example = "Hadits: 'Islam dibangun di atas lima perkara...' (HR. Bukhari no. 8 dan Muslim no. 16)."
        ),

        // ==========================================
        // 2. SANDARAN MATAN (PENGUCAP)
        // ==========================================
        MusthalahTerm(
            id = "marfu",
            latinTitle = "Hadits Marfu'",
            arabicTitle = "حَدِيْثٌ مَرْفُوْعٌ",
            category = MusthalahCategory.SANDARAN,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Hadits yang disandarkan secara khusus kepada Rasulullah ﷺ, baik berupa sabda, perbuatan, persetujuan (taqrir), maupun sifat beliau.",
            fullExplanation = "Marfu' berasal dari kata 'rafa'a' yang berarti mengangkat, karena riwayat ini diangkat kedudukannya hingga bermuara langsung pada baginda Nabi Muhammad ﷺ. Istilah marfu' hanya menjelaskan kepada siapa ucapan itu disandarkan, bukan penentu keshahihannya (hadits marfu' ada yang shahih, hasan, maupun dha'if).",
            criteria = listOf(
                "Marfu' Qauli: Berisi perkataan sabda Rasulullah ﷺ.",
                "Marfu' Fi'li: Menceritakan perbuatan yang dilakukan Rasulullah ﷺ.",
                "Marfu' Taqriri: Mengisahkan persetujuan atau diamnya Rasulullah ﷺ atas perbuatan sahabat.",
                "Marfu' Washfi: Menggambarkan akhlak atau ciri fisik Rasulullah ﷺ."
            ),
            hukumHujjah = "Jika sanadnya shahih/hasan, maka hadits marfu' adalah sumber hukum syariat Islam yang mengikat.",
            example = "Sabda Nabi ﷺ: 'Shalatlah kalian sebagaimana kalian melihat aku shalat.' (HR. Bukhari).",
            relatedCategorySlug = "marfu"
        ),
        MusthalahTerm(
            id = "mauquf",
            latinTitle = "Hadits Mauquf",
            arabicTitle = "حَدِيْثٌ مَوْقُوْفٌ",
            category = MusthalahCategory.SANDARAN,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Riwayat yang terhenti sanadnya dan disandarkan hanya kepada Sahabat Nabi, bukan sabda Rasulullah ﷺ.",
            fullExplanation = "Mauquf berarti 'terhenti'. Riwayat ini memuat ucapan, perbuatan, atau fatwa seorang sahabat Nabi radhiyallahu 'anhum. Namun, jika ucapan sahabat berkaitan dengan perkara gaib (seperti tanda kiamat atau pahala spesifik) yang tidak mungkin berasal dari ijtihad akal, maka riwayat tersebut dihukumi marfu' secara makna (Marfu' Hukmi).",
            criteria = listOf(
                "Sanad berakhir pada salah seorang sahabat Nabi (misal: Umar, Ali, Ibnu Abbas).",
                "Bukan kutipan langsung sabda Nabi ﷺ."
            ),
            hukumHujjah = "Menjadi penjelas konteks pemahaman sahabat terhadap nash syariat (atsar sahabat). Jika berupa marfu' hukmi, sah menjadi dalil syariat.",
            example = "Perkataan Ali bin Abi Thalib: 'Ajaklah manusia berbicara sesuai tingkat pemahaman mereka.' (HR. Bukhari).",
            relatedCategorySlug = "mauquf"
        ),
        MusthalahTerm(
            id = "maqthu",
            latinTitle = "Hadits Maqthu'",
            arabicTitle = "حَدِيْثٌ مَقْطُوْعٌ",
            category = MusthalahCategory.SANDARAN,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Riwayat yang disandarkan kepada generasi Tabi'in atau generasi setelah mereka, baik berupa ucapan maupun perbuatan.",
            fullExplanation = "Maqthu' berarti 'terputus'. Istilah ini jangan dikelirukan dengan 'Munqathi'' (yang berkaitan dengan sanad terputus). Maqthu' adalah status matan ucapan tokoh tabi'in (seperti Sa'id bin al-Musayyab, Hasan al-Bashri, Mujahid, Atha', dll.) yang berkedudukan sebagai ijtihad salafus shalih.",
            criteria = listOf(
                "Sanad berakhir pada tabi'in atau tabi'ut tabi'in.",
                "Merupakan fatwa atau perkataan ulama generasi tabi'in."
            ),
            hukumHujjah = "Bukan merupakan dalil syar'i yang mengikat, namun berfungsi sebagai rujukan penafsiran dan pemahaman generasi terbaik Islam.",
            example = "Perkataan Hasan al-Bashri: 'Shalatlah di belakang pelaku bid'ah, dan dosanya ditanggung olehnya sendiri.'",
            relatedCategorySlug = "maqthu"
        ),
        MusthalahTerm(
            id = "qudsi",
            latinTitle = "Hadits Qudsi",
            arabicTitle = "حَدِيْثٌ قُدْسِيٌّ",
            category = MusthalahCategory.SANDARAN,
            hujjahStatus = HujjahStatus.SHAHIH_HUJJAH,
            shortDefinition = "Hadits yang maknanya diwahyukan oleh Allah ﷻ kepada Rasulullah ﷺ, sedangkan redaksi kalimatnya dirangkai oleh baginda Nabi ﷺ.",
            fullExplanation = "Disebut juga Hadits Ilahi atau Robbani. Berbeda dengan Al-Qur'an, Hadits Qudsi bukan mu'jizat redaksional, tidak dibaca di dalam rukun shalat, dan lafadznya bukan firman mutlak langsung seperti mushaf, melainkan wahyu makna (ilham/mimpi haq) yang disampaikan Nabi dengan bahasa beliau.",
            criteria = listOf(
                "Diawali lafadz: 'Rasulullah bersabda meriwayatkan dari Rabb-nya...', atau 'Allah Ta'ala berfirman...'",
                "Bukan bagian dari ayat surat Al-Qur'an Al-Karim."
            ),
            hukumHujjah = "Wajib diamalkan jika sanadnya shahih/hasan, menjadi pilar utama tazkiyatun nufus dan tauhid asma wa shifat.",
            example = "Firman Allah dalam hadits qudsi: 'Wahai hamba-Ku, sesungguhnya Aku telah mengharamkan kezaliman atas diri-Ku...' (HR. Muslim).",
            relatedCategorySlug = "qudsi"
        ),

        // ==========================================
        // 3. KONDISI SANAD (KETERSAMBUNGAN)
        // ==========================================
        MusthalahTerm(
            id = "muttashil",
            latinTitle = "Muttashil / Maushul",
            arabicTitle = "حَدِيْثٌ مُتَّصِلٌ / مَوْصُوْلٌ",
            category = MusthalahCategory.SANAD,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Hadits yang rangkaian sanadnya saling bersambung, di mana setiap perawi mendengar langsung riwayat dari gurunya tanpa terputus.",
            fullExplanation = "Ketersambungan sanad (Ittishal as-Sanad) adalah rukun pertama dalam keshahihan hadits. Jika sanad tidak bersambung, riwayat akan runtuh ke dalam salah satu kategori inqitha' (terputus) yang menyebabkan statusnya menjadi dha'if.",
            criteria = listOf(
                "Setiap rawi sezaman (mu'asharah) dengan gurunya.",
                "Terbukti adanya pertemuan fisik (liqa') dan mendengarkan langsung (sama').",
                "Bisa berstatus Marfu' (sampai Nabi) atau Mauquf (sampai Sahabat)."
            ),
            hukumHujjah = "Merupakan syarat mutlak diterimanya sebuah hadits sebagai dalil syar'i.",
            example = "Sanad riwayat: Malik dari Nafi' dari Ibnu Umar dari Rasulullah ﷺ (disebut Silsilah adz-Dzahab / Rantai Emas)."
        ),
        MusthalahTerm(
            id = "musnad",
            latinTitle = "Hadits Musnad",
            arabicTitle = "حَدِيْثٌ مُسْنَدٌ",
            category = MusthalahCategory.SANAD,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Hadits yang sanadnya bersambung secara zhahir dari penyusun kitab hingga sampai kepada Rasulullah ﷺ secara marfu'.",
            fullExplanation = "Kata musnad memiliki dua makna dalam khazanah ilmu hadits: (1) Sifat hadits yang sanadnya bersambung sempurna dan marfu' sampai Nabi ﷺ, atau (2) Nama kitab hadits yang disusun berdasarkan bab nama sahabat perawi (seperti Musnad Ahmad dan Musnad Syafi'i).",
            criteria = listOf(
                "Sanad bersambung sempurna dari awal hingga akhir.",
                "Matannya berstatus marfu' kepada Rasulullah ﷺ."
            ),
            hukumHujjah = "Bergantung pada keshahihan perawinya; jika tsiqah maka shahih dan menjadi dalil hukum.",
            example = "Hadits-hadits yang dicantumkan dalam Musnad Imam Ahmad bin Hanbal."
        ),
        MusthalahTerm(
            id = "muallaq",
            latinTitle = "Hadits Mu'allaq",
            arabicTitle = "حَدِيْثٌ مُعَلَّقٌ",
            category = MusthalahCategory.SANAD,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Hadits yang gugur satu atau lebih perawinya secara berturut-turut pada awal sanad dari sisi penyusun kitab.",
            fullExplanation = "Mu'allaq berarti 'tergantung' seperti lampu yang tergantung di atap tanpa tiang penopang dari bawah. Penyusun kitab langsung memotong awal sanad dan berkata: 'Rasulullah bersabda...' atau 'Dari Ibnu Abbas...'. Pada dasarnya hadits mu'allaq tergolong dha'if karena sanad terputus. Namun hadits mu'allaq dalam Shahih Al-Bukhari yang ditulis dengan kalimat aktif/tegas (shighat jazm seperti 'Qaala') dihukumi shahih sampai kepada orang yang disebutkannya.",
            criteria = listOf(
                "Gugur perawi pada awal rantai sanad (guru penyusun kitab).",
                "Bisa gugur 1 orang guru, atau seluruh sanad kecuali sahabat/Nabi."
            ),
            hukumHujjah = "Dha'if menurut kaidah umum sanad, kecuali riwayat mu'allaq dengan shighat jazm dalam Shahih Al-Bukhari.",
            example = "Bab Al-Bukhari: 'Dan berkata Abu Hurairah: Rasulullah ﷺ bersabda...' tanpa menyebut sanad antara beliau dan Abu Hurairah.",
            relatedCategorySlug = "muallaq"
        ),
        MusthalahTerm(
            id = "mursal",
            latinTitle = "Hadits Mursal",
            arabicTitle = "حَدِيْثٌ مُرْسَلٌ",
            category = MusthalahCategory.SANAD,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Hadits yang gugur perawinya di akhir sanad setelah Tabi'in, di mana seorang Tabi'in langsung menyandarkan ucapan kepada Rasulullah ﷺ tanpa menyebut sahabat.",
            fullExplanation = "Tabi'in tidak pernah bertemu Rasulullah ﷺ. Ketika seorang Tabi'in berkata: 'Rasulullah bersabda...', ada mata rantai yang hilang. Sebagian besar ulama hadits (seperti Imam Syafi'i dan Muslim) menghukumi hadits mursal sebagai dha'if karena ada kemungkinan tabi'in tersebut mendengar dari sesama tabi'in yang dha'if. Namun Imam Abu Hanifah dan Imam Malik menerima mursal kibar tabi'in jika tabi'in tersebut terkenal tsiqah.",
            criteria = listOf(
                "Perawi yang meriwayatkan langsung kepada Nabi adalah Tabi'in (bukan sahabat).",
                "Nama sahabat perantara tidak disebutkan."
            ),
            hukumHujjah = "Dihukumi dha'if oleh mayoritas muhadditsin, kecuali diperkuat oleh jalur riwayat lain atau fatwa sahabat.",
            example = "Riwayat Sa'id bin al-Musayyab yang langsung berkata: 'Rasulullah ﷺ melarang jual beli muzabanah...'",
            relatedCategorySlug = "mursal"
        ),
        MusthalahTerm(
            id = "munqathi",
            latinTitle = "Hadits Munqathi'",
            arabicTitle = "حَدِيْثٌ مُنْقَطِعٌ",
            category = MusthalahCategory.SANAD,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Hadits yang gugur salah satu perawinya di tengah-tengah sanad pada satu tempat, atau terdapat perawi yang disamarkan (mubham).",
            fullExplanation = "Munqathi' berarti 'terputus'. Keterputusan ini terjadi di tengah sanad, bukan di awal (mu'allaq) dan bukan di akhir (mursal). Gugurnya perawi di tengah sanad membuka ketidakpastian apakah perawi yang hilang itu tsiqah atau pendusta.",
            criteria = listOf(
                "Gugur satu perawi di bagian tengah jalur sanad.",
                "Atau terdapat perawi yang tidak disebutkan namanya secara jelas (misal: 'an rajulin / dari seorang lelaki)."
            ),
            hukumHujjah = "Dha'if dan tertolak sebagai hujjah hukum syariat karena hilangnya jaminan ketersambungan periwayatan.",
            example = "Riwayat Abdul Razzaq dari Ats-Tsauri dari Abu Ishaq dari Zaid bin Yutsai' dari Hudzaifah (Ats-Tsauri tidak mendengar dari Abu Ishaq secara langsung).",
            relatedCategorySlug = "munqathi"
        ),
        MusthalahTerm(
            id = "mudhal",
            latinTitle = "Hadits Mu'dhal",
            arabicTitle = "حَدِيْثٌ مُعْضَلٌ",
            category = MusthalahCategory.SANAD,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Hadits yang gugur dua orang perawi atau lebih di dalam sanadnya secara berturut-turut.",
            fullExplanation = "Mu'dhal berarti 'rumit dan sukar'. Kedha'ifan hadits mu'dhal lebih berat daripada hadits mursal dan munqathi', karena hilangnya dua generasi perawi secara berurutan menciptakan celah keraguan sanad yang sangat lebar.",
            criteria = listOf(
                "Gugur dua perawi atau lebih.",
                "Gugurnya perawi terjadi secara berurutan pada satu tempat dalam sanad."
            ),
            hukumHujjah = "Dha'if berat (dha'if jiddan) dan tidak sah dijadikan dalil.",
            example = "Riwayat Imam Malik dalam Al-Muwaththa' di mana beliau berkata: 'Telah sampai kepadaku dari Abu Hurairah bahwa Rasulullah bersabda...' (antara Malik dan Abu Hurairah gugur 2 orang perawi)."
        ),
        MusthalahTerm(
            id = "mudallas",
            latinTitle = "Hadits Mudallas",
            arabicTitle = "حَدِيْثٌ مُدَلَّسٌ",
            category = MusthalahCategory.SANAD,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Hadits yang diriwayatkan dengan cara menyamarkan cela dalam sanad agar nampak bagus di mata pendengar.",
            fullExplanation = "Tadlis terbagi menjadi dua: (1) Tadlis Isnad: Rawi meriwayatkan hadits dari guru yang pernah ditemuinya dengan lafadz yang samar (seperti 'dari' / 'an atau 'ia berkata' / qala), padahal ia tidak mendengar riwayat spesifik itu langsung dari sang guru melainkan lewat perantara lain; (2) Tadlis Syuyukh: Rawi menyebut nama gurunya dengan kuniyah, nasab, atau gelar yang tidak populer agar tidak dikenali kelemahannya.",
            criteria = listOf(
                "Perawi menggunakan lafadz periwayatan yang mengandung kemungkinan (shighat muhtamilah) seperti 'An atau Qala.",
                "Perawi tidak menegaskan mendengar langsung (tidak memakai Haddatsana / Akhbarana)."
            ),
            hukumHujjah = "Ditolak riwayatnya kecuali jika perawi mudallis tersebut secara tegas menyatakan mendengar langsung (tashrih bis sama').",
            example = "Hadits-hadits riwayat Al-A'masy atau Sufyan Ats-Tsauri bila menggunakan kata 'An tanpa tashrih sama'."
        ),
        MusthalahTerm(
            id = "syadz",
            latinTitle = "Hadits Syadz & Mahfuzh",
            arabicTitle = "حَدِيْثٌ شَاذٌّ وَمَحْفُوْظٌ",
            category = MusthalahCategory.SANAD,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Hadits yang diriwayatkan oleh perawi yang maqbul (diterima) namun isinya menyelisihi riwayat perawi lain yang lebih tsiqah atau lebih banyak jumlahnya.",
            fullExplanation = "Meskipun perawinya tsiqah, manusia tidak luput dari salah. Ketika satu rawi tsiqah meriwayatkan lafadz yang bertentangan dengan jama'ah rawi tsiqah lainnya, riwayat ganjil yang menyendiri tersebut dihukumi 'Syadz' (tertolak), sedangkan riwayat yang lebih kuat disebut 'Mahfuzh' (terjaga dan diamalkan).",
            criteria = listOf(
                "Perawi berstatus terpercaya (tsiqah).",
                "Riwayatnya bertentangan dengan periwayatan orang-orang yang lebih terpercaya darinya."
            ),
            hukumHujjah = "Hadits Syadz berstatus Dha'if (tidak boleh diamalkan), sedangkan hadits Mahfuzh berstatus Shahih dan wajib diamalkan.",
            example = "Tambahan lafadz membasuh leher saat berwudhu yang diselisihi oleh riwayat mayoritas sahabat."
        ),

        // ==========================================
        // 4. KUANTITAS JALUR PERIWAYATAN
        // ==========================================
        MusthalahTerm(
            id = "mutawatir",
            latinTitle = "Hadits Mutawatir",
            arabicTitle = "حَدِيْثٌ مُتَوَاتِرٌ",
            category = MusthalahCategory.KUANTITAS,
            hujjahStatus = HujjahStatus.SHAHIH_HUJJAH,
            shortDefinition = "Hadits yang diriwayatkan oleh sejumlah besar perawi pada setiap tingkatan sanad, sehingga mustahil secara logika dan kebiasaan mereka bersepakat dusta.",
            fullExplanation = "Mutawatir memberikan faedah ilmu yaqini / dharuri (pengetahuan pasti dan mutlak seperti menyaksikan matahari siang hari). Keshahihannya tidak lagi memerlukan penelusuran biografi perawi satu per satu karena kuantitas massalnya telah menjamin kebenaran kabar tersebut.",
            criteria = listOf(
                "Diriwayatkan oleh banyak orang di setiap thabaqah (tingkatan) sanad.",
                "Jumlah perawi mencapai batasan mustahil bersepakat untuk berbohong.",
                "Pemberitaan berdasar pada panca indera (misal: 'kami mendengar', 'kami melihat')."
            ),
            hukumHujjah = "Menghasilkan keyakinan pasti (ilmu qath'i); mengingkari hadits mutawatir yang ma'lum minad dini bidhdharurah dapat menjerumuskan pada kekufuran.",
            example = "Hadits: 'Barangsiapa berdusta atas namaku secara sengaja, maka bersiaplah menempati tempat duduknya di neraka.' (Diriwayatkan lebih dari 70 sahabat).",
            relatedCategorySlug = "mutawatir"
        ),
        MusthalahTerm(
            id = "ahad",
            latinTitle = "Hadits Ahad",
            arabicTitle = "حَدِيْثٌ آحَادٌ",
            category = MusthalahCategory.KUANTITAS,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Hadits yang jumlah perawi jalurnya tidak mencapai tingkatan derajat hadits mutawatir.",
            fullExplanation = "Mayoritas hadits dalam kitab-kitab induk hadits berstatus hadits Ahad. Hadits Ahad terbagi menjadi tiga berdasarkan jumlah jalurnya: Masyhur, 'Aziz, dan Gharib. Keshahihannya diteliti berdasarkan kredibilitas sanad dan matan.",
            criteria = listOf(
                "Tidak memenuhi syarat kuantitas massal hadits mutawatir.",
                "Bisa berderajat Shahih, Hasan, maupun Dha'if."
            ),
            hukumHujjah = "Wajib diamalkan dan dijadikan hujjah dalam aqidah dan hukum syariat apabila sanadnya terbukti shahih atau hasan.",
            example = "Sebagian besar hadits hukum dalam Kutubus Sittah."
        ),
        MusthalahTerm(
            id = "masyhur",
            latinTitle = "Hadits Masyhur",
            arabicTitle = "حَدِيْثٌ مَشْهُوْرٌ",
            category = MusthalahCategory.KUANTITAS,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Hadits yang diriwayatkan oleh minimal tiga orang perawi di setiap tingkatan sanad, namun belum mencapai derajat mutawatir.",
            fullExplanation = "Dikenal juga dengan istilah hadits Mustafidh di kalangan sebagian fuqaha. Karena didukung minimal tiga jalur independen di setiap kurun zaman, hadits masyhur memiliki bobot otentisitas yang sangat mantap bila seluruh perawinya tsiqah.",
            criteria = listOf(
                "Perawi tidak kurang dari tiga orang pada setiap thabaqah sanad.",
                "Belum mencapai batas mutawatir."
            ),
            hukumHujjah = "Sah dan kuat dijadikan dalil hukum bila perawinya tsiqah.",
            example = "Hadits: 'Sesungguhnya Allah tidak mencabut ilmu dengan mencabutnya sekaligus dari dada manusia, melainkan dengan mewafatkan para ulama...' (HR. Bukhari dan Muslim)."
        ),
        MusthalahTerm(
            id = "aziz",
            latinTitle = "Hadits 'Aziz",
            arabicTitle = "حَدِيْثٌ عَزِيْزٌ",
            category = MusthalahCategory.KUANTITAS,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Hadits yang diriwayatkan oleh tidak kurang dari dua orang perawi pada salah satu tingkatan sanadnya.",
            fullExplanation = "'Aziz berarti mulia atau langka. Ciri khas hadits 'aziz adalah pada salah satu titik generasinya hanya terdapat dua orang perawi yang meriwayatkannya dari generasi sebelumnya, walaupun pada tingkatan lain mungkin diriwayatkan oleh lebih dari dua orang.",
            criteria = listOf(
                "Terdapat minimal satu thabaqah sanad yang hanya diisi oleh dua perawi.",
                "Jumlah perawi tidak boleh kurang dari dua orang di thabaqah manapun."
            ),
            hukumHujjah = "Hujjah yang diterima bila kedua jalurnya shahih.",
            example = "Hadits: 'Tidak beriman salah seorang di antara kalian hingga aku lebih dicintainya daripada orang tuanya, anaknya, dan seluruh manusia.' (Diriwayatkan oleh Anas dan Abu Hurairah)."
        ),
        MusthalahTerm(
            id = "gharib",
            latinTitle = "Hadits Gharib / Fard",
            arabicTitle = "حَدِيْثٌ غَرِيْبٌ / فَرْدٌ",
            category = MusthalahCategory.KUANTITAS,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Hadits yang diriwayatkan oleh hanya satu orang perawi saja pada salah satu tingkatan rantai sanad.",
            fullExplanation = "Penyendirian (tafarrud) satu perawi dalam hadits gharib memerlukan ketelitian ekstra dari kritikus hadits. Jika perawi tunggal tersebut adalah sosok kaliber raksasa seperti az-Zuhri atau Umar bin Khattab yang sangat tsiqah, haditsnya tetap shahih (seperti hadits niat). Namun jika perawi tunggal tersebut lemah, haditsnya menjadi munkar atau dha'if.",
            criteria = listOf(
                "Gharib Muthlaq: Penyendirian terjadi sejak tingkatan sahabat (hanya 1 sahabat yang meriwayatkan).",
                "Gharib Nisbi: Penyendirian terjadi di tengah sanad setelah generasi sahabat."
            ),
            hukumHujjah = "Diterima jika perawi tunggal tersebut memiliki derajat dhabith dan adalah yang sempurna.",
            example = "Hadits: 'Innamal a'maalu bin niyyaat' (Gharib muthlaq di tingkat sahabat, hanya diriwayatkan oleh Umar bin Al-Khatthab)."
        ),

        // ==========================================
        // 5. KREDIBILITAS PERAWI (JARH WA TA'DIL)
        // ==========================================
        MusthalahTerm(
            id = "tsiqah",
            latinTitle = "Tsiqah",
            arabicTitle = "ثِقَةٌ (عَدْلٌ ضَابِطٌ)",
            category = MusthalahCategory.PERAWI,
            hujjahStatus = HujjahStatus.SHAHIH_HUJJAH,
            shortDefinition = "Predikat tertinggi bagi perawi yang mengumpulkan dua sifat kesempurnaan: Keadilan moral ('Adalah) dan Kekuatan hafalan (Dhabith).",
            fullExplanation = "Gelar Tsiqah adalah predikat emas bagi perawi hadits. Perawi tsiqah adalah muslim berakal yang terbukti bertaqwa, lurus aqidahnya, tidak berbuat maksiat atau bid'ah perusak, serta hafalannya sangat presisi tanpa kerancuan ketika menyetor riwayat kepada murid-muridnya.",
            criteria = listOf(
                "'Adil: Taat beragama, bersih dari kefasikan, menjaga kehormatan diri.",
                "Dhabith: Hafalan di luar kepala (hifzhush shadr) atau catatan kitabnya (hifzhul kitab) terpelihara dari sisipan."
            ),
            hukumHujjah = "Riwayat perawi tsiqah melahirkan hadits Shahih dan menjadi dalil paten dalam syariat Islam.",
            example = "Para imam besar hadits seperti Sufyan Ats-Tsauri, Syu'bah bin Al-Hajjaj, Abdurrahman bin Mahdi, dan Yahya bin Ma'in."
        ),
        MusthalahTerm(
            id = "shaduq",
            latinTitle = "Shaduq",
            arabicTitle = "صَدُوْقٌ (لَا بَأْسَ بِهِ)",
            category = MusthalahCategory.PERAWI,
            hujjahStatus = HujjahStatus.HASAN_HUJJAH,
            shortDefinition = "Gelar bagi perawi yang memiliki keadilan moral sempurna dan jujur, namun kekuatan hafalannya berada sedikit di bawah derajat tsiqah.",
            fullExplanation = "Ibnu Hajar menempatkan lafadz Shaduq pada tingkatan ta'dil keempat. Perawi shaduq tidak pernah berbohong, amanah, dan dapat dipercaya, hanya saja terkadang ia mengalami sedikit kekeliruan hafalan (lahu auham).",
            criteria = listOf(
                "Kejujuran dan integritas moral terjamin.",
                "Kapasitas hafalan berada pada tingkat menengah (khiffatudh dhabth)."
            ),
            hukumHujjah = "Hadits yang diriwayatkannya berderajat Hasan Lidzatih dan sah dijadikan landasan dalil hukum.",
            example = "Perawi seperti Muhammad bin Ishaq (penulis Sirah Nabawiyah) atau Muhammad bin 'Amr bin 'Alqamah."
        ),
        MusthalahTerm(
            id = "majhul",
            latinTitle = "Majhul ('Ain & Hal)",
            arabicTitle = "مَجْهُوْلُ العَيْنِ / مَجْهُوْلُ الحَالِ",
            category = MusthalahCategory.PERAWI,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Perawi yang tidak dikenal identitas pribadinya atau tidak diketahui rekam jejak kredibilitas keagamaannya.",
            fullExplanation = "Terbagi menjadi dua: (1) Majhul 'Ain: Perawi yang hanya memiliki 1 murid periwayat dan tidak ada ulama yang men-tautsiq (menilai tsiqah); (2) Majhul Hal (Mastur): Perawi yang memiliki 2 murid atau lebih, namun tidak ada catatan kritik apakah ia adil atau tercela.",
            criteria = listOf(
                "Hanya ada sedikit murid yang mengambil riwayat darinya.",
                "Tidak ada rekomendasi keabsahan dari pakar kritik hadits."
            ),
            hukumHujjah = "Riwayatnya berstatus Dha'if hingga ditemukan saksi penguat jalur lain.",
            example = "Ungkapan rawi: 'Telah menceritakan kepadaku seseorang dari kabilah Bani Asad...'"
        ),
        MusthalahTerm(
            id = "matruk",
            latinTitle = "Matruk",
            arabicTitle = "مَتْرُوْكُ الحَدِيْثِ",
            category = MusthalahCategory.PERAWI,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Perawi yang ditinggalkan dan dibuang haditsnya karena dituduh berdusta atau memiliki kefasikan yang nyata.",
            fullExplanation = "Matruk berada pada jurang jarh yang sangat dalam. Penyebabnya adalah perawi tersebut terbukti berdusta dalam percakapan sehari-hari (walaupun belum terbukti memalsukan hadits Nabi), atau sering melakukan kekeliruan fatal yang tidak dapat ditolerir oleh para kritikus hadits.",
            criteria = listOf(
                "Muttaham bil kizb (tertuduh suka berdusta dalam pergaulan).",
                "Banyak berbuat kelalaian (ghiflah) dan banyak membuat riwayat wahm.",
                "Dikenal fasik dalam perbuatan."
            ),
            hukumHujjah = "Haditsnya berstatus Dha'if Jiddan (sangat lemah), tidak boleh dipakai dan tidak bisa menjadi penguat bagi hadits lain.",
            example = "Perawi seperti Amr bin Syamir Al-Ju'fi atau Shalih bin Nabhan."
        ),
        MusthalahTerm(
            id = "kadzdzab",
            latinTitle = "Kadzdzab / Waddha'",
            arabicTitle = "كَذَّابٌ / وَضَّاعٌ",
            category = MusthalahCategory.PERAWI,
            hujjahStatus = HujjahStatus.NON_HUJJAH,
            shortDefinition = "Tingkatan celaan jarh paling buruk bagi pemalsu dan pembuat kebohongan atas nama Rasulullah ﷺ.",
            fullExplanation = "Kadzdzab adalah orang yang secara sengaja mengarang-ngarang sabda dan sanad lalu melabelkannya sebagai hadits Nabi ﷺ. Para ulama sepakat bahwa jika seorang rawi terbukti satu kali saja memalsukan hadits, maka seluruh riwayatnya gugur selamanya dan tidak akan diterima taubat riwayatnya dalam ilmu hadits.",
            criteria = listOf(
                "Terbukti sengaja membuat hadits palsu.",
                "Mengakui sendiri pemalsuannya atau tertangkap basah meriwayatkan naskah palsu."
            ),
            hukumHujjah = "Riwayatnya dihukumi Maudhu' (Palsu) dan haram disebarkan tanpa peringatan.",
            example = "Tokoh pemalsu seperti Nuh bin Abi Maryam atau Ishaq bin Basyir Al-Kuhali."
        ),

        // ==========================================
        // 6. METODE RIWAYAT (TAHAMMUL WAL ADA')
        // ==========================================
        MusthalahTerm(
            id = "haddatsana",
            latinTitle = "Haddatsana (As-Sama')",
            arabicTitle = "حَدَّثَنَا (السَّمَاعُ مِنْ لَفْظِ الشَّيْخِ)",
            category = MusthalahCategory.METODE,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Metode transmisi periwayatan di mana murid mendengar langsung bacaan hadits yang diucapkan secara lisan oleh sang guru.",
            fullExplanation = "As-Sama' dari lafadz guru adalah tingkatan penerimaan hadits (tahammul) yang paling utama dan tinggi menurut mayoritas muhadditsin. Lafadz 'Haddatsana' (telah menceritakan kepada kami) digunakan jika murid mendengar bersama murid-murid lain, sedangkan 'Haddatsani' digunakan jika murid mendengar sendirian secara privat.",
            criteria = listOf(
                "Guru membacakan hafalan atau naskah kitabnya.",
                "Murid menyimak dan mencatat bacaan lisan sang guru."
            ),
            hukumHujjah = "Tingkat ketersambungan transmisi tertinggi dan bebas dari keraguan tadlis.",
            example = "Pembuka hadits Shahih Bukhari: 'Haddatsana Al-Humaidi Abdullah bin Zubair...'"
        ),
        MusthalahTerm(
            id = "akhbarana",
            latinTitle = "Akhbarana (Al-Qira'ah / 'Ardh)",
            arabicTitle = "أَخْبَرَنَا (القِرَاءَةُ عَلَى الشَّيْخِ)",
            category = MusthalahCategory.METODE,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Metode transmisi di mana murid membaca naskah hadits di hadapan sang guru, dan sang guru menyimak serta membenarkannya.",
            fullExplanation = "Disebut juga metode Al-'Ardh (menyetorkan bacaan). Murid membacakan hadits sementara guru mendengar dengan seksama dari hafalannya atau mencocokkannya dengan naskah aslinya. Imam Malik dan mayoritas ulama Hijaz menganggap derajat 'Akhbarana' setara dengan 'Haddatsana'.",
            criteria = listOf(
                "Murid membaca di hadapan guru.",
                "Guru mengoreksi jika ada salah dan memberikan persetujuan ijazah riwayat."
            ),
            hukumHujjah = "Transmisi valid dan diakui secara ijma' oleh para ulama hadits.",
            example = "Banyak digunakan dalam transmisi periwayatan Al-Muwaththa' Imam Malik."
        ),
        MusthalahTerm(
            id = "ananah",
            latinTitle = "'An'anah (عَنْ)",
            arabicTitle = "العَنْعَنَةُ (رِوَايَةُ فُلَانٍ عَنْ فُلَانٍ)",
            category = MusthalahCategory.METODE,
            hujjahStatus = HujjahStatus.INFORMASIONAL,
            shortDefinition = "Periwayatan hadits menggunakan redaksi kata 'Dari si Fulan ('An Fulan)' tanpa menegaskan apakah mendengar langsung atau lewat perantara.",
            fullExplanation = "Periwayatan 'An'anah dapat diterima dan dihukumi bersambung (muttashil) dengan dua syarat mutlak menurut Imam Muslim: (1) Perawi bukan seorang mudallis (tidak suka menyamarkan sanad), dan (2) Kedua perawi hidup dalam satu zaman (mu'asharah). Imam Bukhari menambahkan syarat ketiga: terbukti pernah bertemu langsung (tsubutul liqa') minimal satu kali.",
            criteria = listOf(
                "Menggunakan kata 'an (عن).",
                "Perawi bersih dari tuduhan tadlis.",
                "Terbukti sezaman dan pernah bertemu."
            ),
            hukumHujjah = "Diterima sebagai sanad bersambung jika perawi bukan mudallis.",
            example = "Sanad: '...dari Malik, dari Nafi', dari Ibnu Umar...'"
        )
    )

    fun getTermById(id: String): MusthalahTerm? {
        val clean = id.trim().lowercase().replace("-", "_")
        return ALL_TERMS.find { it.id.lowercase() == clean }
    }

    fun searchTerms(query: String, category: MusthalahCategory = MusthalahCategory.ALL): List<MusthalahTerm> {
        val cleanQuery = query.trim().lowercase()
        return ALL_TERMS.filter { term ->
            val matchCategory = category == MusthalahCategory.ALL || term.category == category
            val matchText = cleanQuery.isEmpty() ||
                    term.latinTitle.lowercase().contains(cleanQuery) ||
                    term.arabicTitle.contains(cleanQuery) ||
                    term.shortDefinition.lowercase().contains(cleanQuery) ||
                    term.fullExplanation.lowercase().contains(cleanQuery)
            matchCategory && matchText
        }
    }

    /**
     * Menghasilkan kumpulan soal kuis interaktif yang diacak dari katalog istilah.
     */
    fun generateQuizSession(count: Int = 5): List<com.nusa.hadithapp.domain.model.musthalah.MusthalahQuizQuestion> {
        val shuffledTerms = ALL_TERMS.shuffled()
        val selectedTerms = shuffledTerms.take(minOf(count, ALL_TERMS.size))

        return selectedTerms.mapIndexed { index, targetTerm ->
            val isDefQuestion = index % 2 == 0
            val prompt = if (isDefQuestion) {
                "Manakah istilah ilmu hadis yang memiliki definisi berikut:\n\n\"${targetTerm.shortDefinition}\"?"
            } else {
                "Bagaimanakah status kehujjahan atau kedudukan hukum dari istilah \"${targetTerm.latinTitle}\" (${targetTerm.arabicTitle})?"
            }

            val options: List<com.nusa.hadithapp.domain.model.musthalah.MusthalahQuizOption>
            if (isDefQuestion) {
                val distractors = ALL_TERMS.filter { it.id != targetTerm.id }
                    .shuffled()
                    .take(3)
                    .map {
                        com.nusa.hadithapp.domain.model.musthalah.MusthalahQuizOption(
                            id = it.id,
                            text = "${it.latinTitle} (${it.arabicTitle})",
                            isCorrect = false
                        )
                    }
                val correct = com.nusa.hadithapp.domain.model.musthalah.MusthalahQuizOption(
                    id = targetTerm.id,
                    text = "${targetTerm.latinTitle} (${targetTerm.arabicTitle})",
                    isCorrect = true
                )
                options = (distractors + correct).shuffled()
            } else {
                val correctStatus = targetTerm.hujjahStatus.label
                val otherStatuses = com.nusa.hadithapp.domain.model.musthalah.HujjahStatus.entries
                    .map { it.label }
                    .filter { it != correctStatus }
                    .shuffled()
                    .take(3)
                val distractors = otherStatuses.mapIndexed { i, s ->
                    com.nusa.hadithapp.domain.model.musthalah.MusthalahQuizOption(
                        id = "status_$i",
                        text = s,
                        isCorrect = false
                    )
                }
                val correct = com.nusa.hadithapp.domain.model.musthalah.MusthalahQuizOption(
                    id = "status_correct",
                    text = correctStatus,
                    isCorrect = true
                )
                options = (distractors + correct).shuffled()
            }

            com.nusa.hadithapp.domain.model.musthalah.MusthalahQuizQuestion(
                id = "quiz_${targetTerm.id}_$index",
                questionPrompt = prompt,
                categoryBadge = targetTerm.category.displayName,
                options = options,
                explanation = "${targetTerm.latinTitle} (${targetTerm.arabicTitle}): ${targetTerm.shortDefinition}\n\nKaidah Hukum: ${targetTerm.hukumHujjah}"
            )
        }
    }
}
