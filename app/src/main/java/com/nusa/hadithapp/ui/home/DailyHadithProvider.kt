package com.nusa.hadithapp.ui.home

import java.util.Calendar

object DailyHadithProvider {

    val DAILY_HADITH_COLLECTION: List<HadithOfTheDay> = listOf(
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 1,
            arabicText = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
            translation = "Sesungguhnya setiap amalan tergantung pada niatnya, dan setiap orang akan mendapatkan apa yang ia niatkan."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 13,
            arabicText = "لَا يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لِأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
            translation = "Tidak beriman salah seorang di antara kalian hingga ia mencintai untuk saudaranya apa yang ia cintai untuk dirinya sendiri."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 55,
            arabicText = "الدِّينُ النَّصِيحَةُ : لِلَّهِ وَلِكِتَابِهِ وَلِرَسُولِهِ وَلِأَئِمَّةِ الْمُسْلِمِينَ وَعَامَّتِهِمْ",
            translation = "Agama itu adalah nasihat: bagi Allah, Kitab-Nya, Rasul-Nya, para pemimpin kaum muslimin, dan orang-orang mukmin pada umumnya."
        ),
        HadithOfTheDay(
            bookSlug = "tirmidzi",
            bookName = "Sunan Tirmidzi",
            number = 2516,
            arabicText = "اتَّقِ اللَّهَ حَيْثُمَا كُنْتَ ، وَأَتْبِعِ السَّيِّئَةَ الْحَسَنَةَ تَمْحُهَا ، وَخَالِقِ النَّاسَ بِخُلُقٍ حَسَنٍ",
            translation = "Bertakwalah kepada Allah di mana pun engkau berada, iringilah keburukan dengan kebaikan niscaya akan menghapusnya, dan pergauililah manusia dengan akhlak terpuji."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 6018,
            arabicText = "لَيْسَ الشَّدِيدُ بِالصُّرَعَةِ ، إِنَّمَا الشَّدِيدُ الَّذِي يَمْلِكُ نَفْسَهُ عِنْدَ الْغَضَبِ",
            translation = "Bukanlah orang yang kuat itu yang pandai bergulat, tetapi orang yang kuat adalah yang mampu menguasai dirinya ketika marah."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 2699,
            arabicText = "مَنْ سَلَكَ طَرِيقًا يَلْتَمِسُ فِيهِ عِلْمًا سَهَّلَ اللَّهُ لَهُ بِهِ طَرِيقًا إِلَى الْجَنَّةِ",
            translation = "Barangsiapa menempuh suatu jalan untuk mencari ilmu, maka Allah akan memudahkan baginya jalan menuju surga."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 6464,
            arabicText = "نِعْمَتَانِ مَغْبُونٌ فِيهِمَا كَثِيرٌ مِنَ النَّاسِ : الصِّحَّةُ وَالْفَرَاغُ",
            translation = "Dua kenikmatan yang sering dilalaikan oleh kebanyakan manusia: kesehatan dan waktu luang."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 223,
            arabicText = "الطُّهُورُ شَطْرُ الإِيمَانِ ، وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيزَانَ",
            translation = "Kesucian itu sebagian dari iman, dan ucapan Alhamdulillah memenuhi timbangan amal kebaikan."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 6011,
            arabicText = "مَنْ كَانَ يُؤْمِنُ بِاللَّهِ وَالْيَوْمِ الآخِرِ فَلْيَقُلْ خَيْرًا أَوْ لِيَصْمُتْ",
            translation = "Barangsiapa beriman kepada Allah dan hari akhir, hendaklah ia berkata yang baik atau diam."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 2564,
            arabicText = "كُلُّ الْمُسْلِمِ عَلَى الْمُسْلِمِ حَرَامٌ : دَمُهُ وَمَالُهُ وَعِرْضُهُ",
            translation = "Setiap muslim atas muslim lainnya haram darahnya, hartanya, dan kehormatannya."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 1421,
            arabicText = "الْيَدُ الْعُلْيَا خَيْرٌ مِنَ الْيَدِ السُّفْلَى ، وَابْدَأْ بِمَنْ تَعُولُ",
            translation = "Tangan yang di atas (memberi) lebih baik daripada tangan yang di bawah (meminta), dan mulailah bersedekah dari orang yang menjadi tanggunganmu."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 2588,
            arabicText = "إِنَّ الرِّفْقَ لَا يَكُونُ فِي شَيْءٍ إِلَّا زَانَهُ ، وَلَا يُنْزَعُ مِنْ شَيْءٍ إِلَّا شَانَهُ",
            translation = "Sesungguhnya kelemahlembutan tidaklah berada pada sesuatu melainkan akan menghiasinya, dan tidaklah dicabut dari sesuatu melainkan akan memperburuknya."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 5027,
            arabicText = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
            translation = "Sebaik-baik kalian adalah orang yang belajar Al-Qur'an dan mengajarkannya."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 2674,
            arabicText = "مَنْ دَلَّ عَلَى خَيْرٍ فَلَهُ مِثْلُ أَجْرِ فَاعِلِهِ",
            translation = "Barangsiapa menunjukkan kepada suatu kebaikan, maka baginya pahala semisal pahala orang yang mengerjakannya."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 2442,
            arabicText = "الْمُسْلِمُ أَخُو الْمُسْلِمِ ، لَا يَظْلِمُهُ وَلَا يُسْلِمُهُ ، وَمَنْ كَانَ فِي حَاجَةِ أَخِيهِ كَانَ اللَّهُ فِي حَاجَتِهِ",
            translation = "Seorang muslim adalah saudara bagi muslim lainnya; ia tidak menzhaliminya dan tidak membiarkannya celaka. Barangsiapa menolong kebutuhan saudaranya, Allah akan memenuhi kebutuhannya."
        ),
        HadithOfTheDay(
            bookSlug = "tirmidzi",
            bookName = "Sunan Tirmidzi",
            number = 2317,
            arabicText = "مِنْ حُسْنِ إِسْلَامِ الْمَرْءِ تَرْكُهُ مَا لَا يَعْنِيهِ",
            translation = "Di antara tanda kebaikan keislaman seseorang adalah meninggalkan hal-hal yang tidak bermanfaat baginya."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 6491,
            arabicText = "كَلِمَتَانِ حَبِيبَتَانِ إِلَى الرَّحْمَنِ ، خَفِيفَتَانِ عَلَى اللِّسَانِ ، ثَقِيلَتَانِ فِي الْمِيزَانِ : سُبْحَانَ اللَّهِ وَبِحَمْدِهِ ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            translation = "Dua kalimat yang dicintai oleh Ar-Rahman, ringan di lisan, berat di timbangan: Subhanallah wa bihamdihi, Subhanallahil 'Azhim."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 2865,
            arabicText = "مَا نَقَصَتْ صَدَقَةٌ مِنْ مَالٍ ، وَمَا زَادَ اللَّهُ عَبْدًا بِعَفْوٍ إِلَّا عِزًّا ، وَمَا تَوَاضَعَ أَحَدٌ لِلَّهِ إِلَّا رَفَعَهُ اللَّهُ",
            translation = "Sedekah tidaklah mengurangi harta, dan tidaklah Allah menambahkan kepada seorang hamba yang pemaaf melainkan kemuliaan, serta tidaklah seseorang merendahkan hati karena Allah melainkan Allah akan meninggikan derajatnya."
        ),
        HadithOfTheDay(
            bookSlug = "abudaud",
            bookName = "Sunan Abu Daud",
            number = 4941,
            arabicText = "الرَّاحِمُونَ يَرْحَمُهُمُ الرَّحْمَنُ ، ارْحَمُوا مَنْ فِي الأَرْضِ يَرْحَمْكُمْ مَنْ فِي السَّمَاءِ",
            translation = "Orang-orang yang penyayang akan disayangi oleh Allah Yang Maha Penyayang. Sayangilah siapa yang ada di bumi, niscaya yang ada di langit akan menyayangimu."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 6021,
            arabicText = "مَنْ أَحَبَّ أَنْ يُبْسَطَ لَهُ فِي رِزْقِهِ ، وَيُنْسَأَ لَهُ فِي أَثَرِهِ ، فَلْيَصِلْ رَحِمَهُ",
            translation = "Barangsiapa ingin dilapangkan rezekinya dan dipanjangkan umurnya, hendaklah ia menyambung tali silaturahmi."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 2742,
            arabicText = "مَثَلُ الَّذِي يَذْكُرُ رَبَّهُ وَالَّذِي لَا يَذْكُرُ رَبَّهُ مَثَلُ الْحَيِّ وَالْمَيِّتِ",
            translation = "Perumpamaan orang yang berdzikir mengingat Rabbnya dengan orang yang tidak mengingat Rabbnya adalah seperti perumpamaan orang yang hidup dengan orang yang mati."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 6116,
            arabicText = "إِيَّاكُمْ وَالظَّنَّ ، فَإِنَّ الظَّنَّ أَكْذَبُ الْحَدِيثِ",
            translation = "Jauhilah oleh kalian prasangka buruk, karena prasangka buruk itu adalah sebohong-bohong perkataan."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 1631,
            arabicText = "إِذَا مَاتَ الإِنْسَانُ انْقَطَعَ عَنْهُ عَمَلُهُ إِلَّا مِنْ ثَلَاثَةٍ : إِلَّا مِنْ صَدَقَةٍ جَارِيَةٍ ، أَوْ عِلْمٍ يُنْتَفَعُ بِهِ ، أَوْ وَلَدٍ صَالِحٍ يَدْعُو لَهُ",
            translation = "Jika manusia meninggal dunia, maka terputuslah seluruh amalnya kecuali tiga perkara: sedekah jariyah, ilmu yang bermanfaat, atau anak shalih yang mendoakannya."
        ),
        HadithOfTheDay(
            bookSlug = "tirmidzi",
            bookName = "Sunan Tirmidzi",
            number = 1987,
            arabicText = "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ لَكَ صَدَقَةٌ",
            translation = "Senyumanmu di hadapan saudaramu adalah sedekah bagimu."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 6407,
            arabicText = "كُنْ فِي الدُّنْيَا كَأَنَّكَ غَرِيبٌ أَوْ عَابِرُ سَبِيلٍ",
            translation = "Jadilah engkau di dunia ini seakan-akan orang asing atau seorang pengelana yang menyeberang jalan."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 2691,
            arabicText = "إِنَّ اللَّهَ لَا يَنْظُرُ إِلَى صُوَرِكُمْ وَأَمْوَالِكُمْ ، وَلَكِنْ يَنْظُرُ إِلَى قُلُوبِكُمْ وَأَعْمَالِكُمْ",
            translation = "Sesungguhnya Allah tidak melihat kepada bentuk rupa kalian dan tidak pula harta kalian, tetapi Dia melihat kepada hati dan amal perbuatan kalian."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 5673,
            arabicText = "مَا يُصِيبُ الْمُسْلِمَ مِنْ نَصَبٍ وَلَا وَصَبٍ وَلَا هَمٍّ وَلَا حُزْنٍ وَلَا أَذًى وَلَا غَمٍّ حَتَّى الشَّوْكَةِ يُشَاكُهَا إِلَّا كَفَّرَ اللَّهُ بِهَا مِنْ خَطَايَاهُ",
            translation = "Tidaklah seorang muslim tertimpa kepenatan, penyakit, kekhawatiran, kesedihan, gangguan, bahkan duri yang menusuknya, melainkan Allah akan menghapuskan sebagian kesalahan-kesalahannya karenanya."
        ),
        HadithOfTheDay(
            bookSlug = "muslim",
            bookName = "Shahih Muslim",
            number = 2664,
            arabicText = "الْمُؤْمِنُ الْقَوِيُّ خَيْرٌ وَأَحَبُّ إِلَى اللَّهِ مِنَ الْمُؤْمِنِ الضَّعِيفِ ، وَفِي كُلٍّ خَيْرٌ",
            translation = "Mukmin yang kuat lebih baik dan lebih dicintai oleh Allah daripada mukmin yang lemah, namun pada keduanya ada kebaikan."
        ),
        HadithOfTheDay(
            bookSlug = "bukhari",
            bookName = "Shahih Bukhari",
            number = 6014,
            arabicText = "مَنْ يَضْمَنْ لِي مَا بَيْنَ لَحْيَيْهِ وَمَا بَيْنَ رِجْلَيْهِ أَضْمَنْ لَهُ الْجَنَّةَ",
            translation = "Barangsiapa dapat menjamin bagiku apa yang ada di antara kedua rahangnya (lisan) dan apa yang ada di antara kedua kakinya (kemaluan), aku jaminkan baginya surga."
        ),
        HadithOfTheDay(
            bookSlug = "tirmidzi",
            bookName = "Sunan Tirmidzi",
            number = 2344,
            arabicText = "لَوْ أَنَّكُمْ كُنْتُمْ تَوَكَّلُونَ عَلَى اللَّهِ حَقَّ تَوَكُّلِهِ لَرُزِقْتُمْ كَمَا تُرْزَقُ الطَّيْرُ ، تَغْدُو خِمَاصًا وَتَرُوحُ بِطَانًا",
            translation = "Seandainya kalian bertawakal kepada Allah dengan sebenar-benarnya tawakal, niscaya kalian akan diberi rezeki sebagaimana burung diberi rezeki: ia pergi di pagi hari dalam keadaan lapar dan pulang di petang hari dalam keadaan kenyang."
        )
    )

    fun getDailyHadith(): HadithOfTheDay {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = dayOfYear % DAILY_HADITH_COLLECTION.size
        return DAILY_HADITH_COLLECTION[index]
    }

    fun getRandomHadith(excludeCurrent: HadithOfTheDay? = null): HadithOfTheDay {
        val candidates = if (excludeCurrent != null) {
            DAILY_HADITH_COLLECTION.filter { it.bookSlug != excludeCurrent.bookSlug || it.number != excludeCurrent.number }
        } else {
            DAILY_HADITH_COLLECTION
        }
        return candidates.randomOrNull() ?: DAILY_HADITH_COLLECTION.first()
    }
}
