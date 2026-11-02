package com.nusa.hadithapp.util

object ArabicChapterHelper {

    // 🟢 1. DATASET ASLI KUTUBUS SITTAH (DARI JSON KAMU)
    // Format Key: "${slug}_${chapterIndex}"
    private val KUTUBUS_SITTAH_TITLES: Map<String, String> = buildMap {
        // ========== SHAHIH BUKHARI (77 BAB) ==========
        put("bukhari_1", "كتاب بدء الوحى")
        put("bukhari_2", "كتاب الإيمان")
        put("bukhari_3", "كتاب العلم")
        put("bukhari_4", "كتاب الوضوء")
        put("bukhari_5", "كتاب الغسل")
        put("bukhari_6", "كتاب الحيض")
        put("bukhari_7", "كتاب التيمم")
        put("bukhari_8", "كتاب الصلاة")
        put("bukhari_9", "كتاب مواقيت الصلاة")
        put("bukhari_10", "كتاب الأذان")
        put("bukhari_11", "كتاب الجمعة")
        put("bukhari_12", "كتاب الجنائز")
        put("bukhari_13", "كتاب الزكاة")
        put("bukhari_14", "كتاب الحج")
        put("bukhari_15", "كتاب الصوم")
        put("bukhari_16", "كتاب صلاة التراويح")
        put("bukhari_17", "كتاب الاعتكاف")
        put("bukhari_18", "كتاب البيوع")
        put("bukhari_19", "كتاب السلم")
        put("bukhari_20", "كتاب الشفعة")
        put("bukhari_21", "كتاب الإجارة")
        put("bukhari_22", "كتاب الحوالات")
        put("bukhari_23", "كتاب الوكالة")
        put("bukhari_24", "كتاب المزارعة")
        put("bukhari_25", "كتاب المساقاة")
        put("bukhari_26", "كتاب فى الاستقراض")
        put("bukhari_27", "كتاب الخصومات")
        put("bukhari_28", "كتاب فى اللقطة")
        put("bukhari_29", "كتاب المظالم")
        put("bukhari_30", "كتاب الشركة")
        put("bukhari_31", "كتاب الرهن")
        put("bukhari_32", "كتاب العتق")
        put("bukhari_33", "كتاب الهبة وفضلها والتحريض عليها")
        put("bukhari_34", "كتاب الشهادات")
        put("bukhari_35", "كتاب الصلح")
        put("bukhari_36", "كتاب الشروط")
        put("bukhari_37", "كتاب الوصايا")
        put("bukhari_38", "كتاب الجهاد والسير")
        put("bukhari_39", "كتاب فرض الخمس")
        put("bukhari_40", "كتاب الجزية والموادعة")
        put("bukhari_41", "كتاب بدء الخلق")
        put("bukhari_42", "كتاب أحاديث الأنبياء")
        put("bukhari_43", "كتاب المناقب")
        put("bukhari_44", "كتاب المغازى")
        put("bukhari_45", "كتاب التفسير")
        put("bukhari_46", "كتاب فضائل القرآن")
        put("bukhari_47", "كتاب النكاح")
        put("bukhari_48", "كتاب الطلاق")
        put("bukhari_49", "كتاب النفقات")
        put("bukhari_50", "كتاب الأطعمة")
        put("bukhari_51", "كتاب العقيقة")
        put("bukhari_52", "كتاب الذبائح والصيد")
        put("bukhari_53", "كتاب الأضاحي")
        put("bukhari_54", "كتاب الأشربة")
        put("bukhari_55", "كتاب المرضى")
        put("bukhari_56", "كتاب الطب")
        put("bukhari_57", "كتاب اللباس")
        put("bukhari_58", "كتاب الأدب")
        put("bukhari_59", "كتاب الاستئذان")
        put("bukhari_60", "كتاب الدعوات")
        put("bukhari_61", "كتاب الرقاق")
        put("bukhari_62", "كتاب القدر")
        put("bukhari_63", "كتاب الأيمان والنذور")
        put("bukhari_64", "كتاب كفارات الأيمان")
        put("bukhari_65", "كتاب الفرائض")
        put("bukhari_66", "كتاب الحدود")
        put("bukhari_67", "كتاب الديات")
        put("bukhari_68", "كتاب استتابة المرتدين والمعاندين وقتالهم")
        put("bukhari_69", "كتاب الإكراه")
        put("bukhari_70", "كتاب الحيل")
        put("bukhari_71", "كتاب التعبير")
        put("bukhari_72", "كتاب الفتن")
        put("bukhari_73", "كتاب الأحكام")
        put("bukhari_74", "كتاب التمنى")
        put("bukhari_75", "كتاب أخبار الآحاد")
        put("bukhari_76", "كتاب الاعتصام بالكتاب والسنة")
        put("bukhari_77", "كتاب التوحيد")

        // ========== SHAHIH MUSLIM (56 BAB) ==========
        put("muslim_1", "المقدمة")
        put("muslim_2", "كتاب الإيمان")
        put("muslim_3", "كتاب الطهارة")
        put("muslim_4", "كتاب الحيض")
        put("muslim_5", "كتاب الصلاة")
        put("muslim_6", "كتاب الْمَسَاجِدِ وَمَوَاضِعِ الصَّلاَةِ")
        put("muslim_7", "كتاب صلاة المسافرين وقصرها")
        put("muslim_8", "كتاب الجمعة")
        put("muslim_9", "كتاب صلاة العيدين")
        put("muslim_10", "كتاب صلاة الاستسقاء‏")
        put("muslim_11", "كتاب الكسوف")
        put("muslim_12", "كتاب الجنائز")
        put("muslim_13", "كتاب الزكاة")
        put("muslim_14", "كتاب الصيام")
        put("muslim_15", "كتاب الاعتكاف")
        put("muslim_16", "كتاب الحج")
        put("muslim_17", "كتاب النكاح")
        put("muslim_18", "كتاب الرضاع")
        put("muslim_19", "كتاب الطلاق")
        put("muslim_20", "كتاب اللعان")
        put("muslim_21", "كتاب العتق")
        put("muslim_22", "كتاب البيوع")
        put("muslim_23", "كتاب المساقاة")
        put("muslim_24", "كتاب الفرائض")
        put("muslim_25", "كتاب الهبات")
        put("muslim_26", "كتاب الوصية")
        put("muslim_27", "كتاب النذر")
        put("muslim_28", "كتاب الأيمان")
        put("muslim_29", "كتاب القسامة والمحاربين والقصاص والديات")
        put("muslim_30", "كتاب الحدود")
        put("muslim_31", "كتاب الأقضية")
        put("muslim_32", "كتاب اللقطة")
        put("muslim_33", "كتاب الجهاد والسير")
        put("muslim_34", "كتاب الإمارة")
        put("muslim_35", "كتاب الصيد والذبائح وما يؤكل من الحيوان")
        put("muslim_36", "كتاب الأضاحى")
        put("muslim_37", "كتاب الأشربة")
        put("muslim_38", "كتاب اللباس والزينة")
        put("muslim_39", "كتاب الآداب")
        put("muslim_40", "كتاب السلام")
        put("muslim_41", "كتاب الألفاظ من الأدب وغيرها")
        put("muslim_42", "كتاب الشعر")
        put("muslim_43", "كتاب الرؤيا")
        put("muslim_44", "كتاب الفضائل")
        put("muslim_45", "كتاب فضائل الصحابة رضى الله تعالى عنهم")
        put("muslim_46", "كتاب البر والصلة والآداب")
        put("muslim_47", "كتاب القدر")
        put("muslim_48", "كتاب العلم")
        put("muslim_49", "كتاب الذكر والدعاء والتوبة والاستغفار")
        put("muslim_50", "كتاب التوبة")
        put("muslim_51", "كتاب صفات المنافقين وأحكامهم")
        put("muslim_52", "كتاب صفة القيامة والجنة والنار")
        put("muslim_53", "كتاب الجنة وصفة نعيمها وأهلها")
        put("muslim_54", "كتاب الفتن وأشراط الساعة")
        put("muslim_55", "كتاب الزهد والرقائق")
        put("muslim_56", "كتاب التفسير")

        // ========== SUNAN TIRMIDZI (49 BAB) ==========
        put("tirmidzi_1", "كتاب الطهارة عن رسول الله ﷺ")
        put("tirmidzi_2", "كتاب الصلاة")
        put("tirmidzi_3", "كِتَاب الْجُمُعَةِ عَنْ رَسُولِ اللَّهِ ﷺ")
        put("tirmidzi_4", "كتاب الزكاة عن رسول الله ﷺ")
        put("tirmidzi_5", "كتاب الصوم عن رسول الله ﷺ")
        put("tirmidzi_6", "كتاب الحج عن رسول الله ﷺ")
        put("tirmidzi_7", "كتاب الجنائز عن رسول الله ﷺ")
        put("tirmidzi_8", "كتاب النكاح عن رسول الله ﷺ")
        put("tirmidzi_9", "كتاب الرضاع")
        put("tirmidzi_10", "كتاب الطلاق واللعان عن رسول الله ﷺ")
        put("tirmidzi_11", "كتاب البيوع عن رسول الله ﷺ")
        put("tirmidzi_12", "كتاب الأحكام عن رسول الله ﷺ")
        put("tirmidzi_13", "كتاب الديات عن رسول الله ﷺ")
        put("tirmidzi_14", "كتاب الحدود عن رسول الله ﷺ")
        put("tirmidzi_15", "كتاب الصيد والذبائح عن رسول الله ﷺ")
        put("tirmidzi_16", "كتاب الصيد والذبائح")
        put("tirmidzi_17", "كتاب الأطعمة")
        put("tirmidzi_18", "كتاب الأحكام والفوائد")
        put("tirmidzi_19", "كتاب الأضاحى عن رسول الله ﷺ")
        put("tirmidzi_20", "كتاب النذور والأيمان عن رسول الله ﷺ")
        put("tirmidzi_21", "كتاب السير عن رسول الله ﷺ")
        put("tirmidzi_22", "كتاب فضائل الجهاد عن رسول الله ﷺ")
        put("tirmidzi_23", "كتاب الجهاد عن رسول الله ﷺ")
        put("tirmidzi_24", "كتاب اللباس")
        put("tirmidzi_25", "كتاب الأطعمة عن رسول الله ﷺ")
        put("tirmidzi_26", "كتاب الأشربة عن رسول الله ﷺ")
        put("tirmidzi_27", "كتاب البر والصلة عن رسول الله ﷺ")
        put("tirmidzi_28", "كتاب الطب عن رسول اللَّهِ ﷺ")
        put("tirmidzi_29", "كتاب الفرائض عن رسول الله ﷺ")
        put("tirmidzi_30", "كتاب الوصايا عن رسول الله ﷺ")
        put("tirmidzi_31", "كتاب الولاء والهبة عن رسول الله ﷺ")
        put("tirmidzi_32", "كتاب القدر عن رسول الله ﷺ")
        put("tirmidzi_33", "كتاب الفتن عن رسول الله ﷺ")
        put("tirmidzi_34", "كتاب الرؤيا عن رسول الله ﷺ")
        put("tirmidzi_35", "كتاب الشهادات عن رسول الله ﷺ")
        put("tirmidzi_36", "كتاب الزهد عن رسول الله ﷺ")
        put("tirmidzi_37", "كتاب صفة القيامة والرقائق والورع")
        put("tirmidzi_38", "كتاب صفة الجنة عن رسول الله ﷺ")
        put("tirmidzi_39", "كتاب صفة جهنم عن رسول الله ﷺ")
        put("tirmidzi_40", "كتاب الإيمان عن رسول الله ﷺ")
        put("tirmidzi_41", "كتاب العلم عن رسول الله ﷺ")
        put("tirmidzi_42", "كتاب الاستئذان والآداب عن رسول الله ﷺ")
        put("tirmidzi_43", "كتاب الأدب عن رسول الله ﷺ")
        put("tirmidzi_44", "كتاب الأمثال عن رسول الله ﷺ")
        put("tirmidzi_45", "كتاب ثواب القرآن عن رسول الله ﷺ")
        put("tirmidzi_46", "كتاب القراءات عن رسول الله ﷺ")
        put("tirmidzi_47", "كتاب تفسير القرآن عن رسول الله ﷺ")
        put("tirmidzi_48", "كتاب الدعوات عن رسول الله ﷺ")
        put("tirmidzi_49", "كتاب المناقب عن رسول الله ﷺ")

        // ========== SUNAN ABU DAUD (35 BAB) ==========
        put("abudaud_1", "كتاب الطهارة")
        put("abudaud_2", "كتاب الصلاة")
        put("abudaud_3", "كتاب الزكاة")
        put("abudaud_4", "كتاب اللقطة")
        put("abudaud_5", "كتاب المناسك")
        put("abudaud_6", "كتاب النكاح")
        put("abudaud_7", "كتاب الطلاق")
        put("abudaud_8", "كتاب الصوم")
        put("abudaud_9", "كتاب الجهاد")
        put("abudaud_10", "كتاب الضحايا")
        put("abudaud_11", "كتاب الصيد")
        put("abudaud_12", "كتاب الوصايا")
        put("abudaud_13", "كتاب الفرائض")
        put("abudaud_14", "كتاب الخراج والإمارة والفىء")
        put("abudaud_15", "كتاب الجنائز")
        put("abudaud_16", "كتاب الأيمان والنذور")
        put("abudaud_17", "كتاب البيوع")
        put("abudaud_18", "كتاب الأقضية")
        put("abudaud_19", "كتاب العلم")
        put("abudaud_20", "كتاب الأشربة")
        put("abudaud_21", "كتاب الأطعمة")
        put("abudaud_22", "كتاب الطب")
        put("abudaud_23", "كتاب العتق")
        put("abudaud_24", "كتاب الحروف والقراءات")
        put("abudaud_25", "كتاب الحمَّام")
        put("abudaud_26", "كتاب اللباس")
        put("abudaud_27", "كتاب الترجل")
        put("abudaud_28", "كتاب الخاتم")
        put("abudaud_29", "كتاب الفتن والملاحم")
        put("abudaud_30", "كتاب المهدى")
        put("abudaud_31", "كتاب الملاحم")
        put("abudaud_32", "كتاب الحدود")
        put("abudaud_33", "كتاب الديات")
        put("abudaud_34", "كتاب السنة")
        put("abudaud_35", "كتاب الأدب")

        // ========== SUNAN AN-NASAI (51 BAB) ==========
        put("nasai_1", "كتاب الطهارة")
        put("nasai_2", "كتاب المياه")
        put("nasai_3", "كتاب الحيض والاستحاضة")
        put("nasai_4", "كتاب الغسل والتيمم")
        put("nasai_5", "كتاب الصلاة")
        put("nasai_6", "كتاب المواقيت")
        put("nasai_7", "كتاب الأذان")
        put("nasai_8", "كتاب المساجد")
        put("nasai_9", "كتاب القبلة")
        put("nasai_10", "كتاب الإمامة")
        put("nasai_11", "كتاب الافتتاح")
        put("nasai_12", "كتاب التطبيق")
        put("nasai_13", "كتاب السهو")
        put("nasai_14", "كتاب الجمعة")
        put("nasai_15", "كتاب تقصير الصلاة فى السفر")
        put("nasai_16", "كتاب الكسوف")
        put("nasai_17", "كتاب الاستسقاء")
        put("nasai_18", "كتاب صلاة الخوف")
        put("nasai_19", "كتاب صلاة العيدين")
        put("nasai_20", "كتاب قيام الليل وتطوع النهار")
        put("nasai_21", "كتاب الجنائز")
        put("nasai_22", "كتاب الصيام")
        put("nasai_23", "كتاب الزكاة")
        put("nasai_24", "كتاب مناسك الحج")
        put("nasai_25", "كتاب الجهاد")
        put("nasai_26", "كتاب النكاح")
        put("nasai_27", "كتاب الطلاق")
        put("nasai_28", "كتاب الخيل")
        put("nasai_29", "كتاب الإحباس")
        put("nasai_30", "كتاب الوصايا")
        put("nasai_31", "كتاب النحل")
        put("nasai_32", "كتاب الهبة")
        put("nasai_33", "كتاب الرقبى")
        put("nasai_34", "كتاب العمرى")
        put("nasai_35", "كتاب الأيمان والنذور")
        put("nasai_36", "كتاب عشرة النساء")
        put("nasai_37", "كتاب تحريم الدم")
        put("nasai_38", "كتاب قسم الفىء")
        put("nasai_39", "كتاب البيعة")
        put("nasai_40", "كتاب العقيقة")
        put("nasai_41", "كتاب الفرع والعتيرة")
        put("nasai_42", "كتاب الصيد والذبائح")
        put("nasai_43", "كتاب الضحايا")
        put("nasai_44", "كتاب البيوع")
        put("nasai_45", "كتاب القسامة")
        put("nasai_46", "كتاب قطع السارق")
        put("nasai_47", "كتاب الإيمان وشرائعه")
        put("nasai_48", "كتاب الزينة من السنن")
        put("nasai_49", "كتاب آداب القضاة")
        put("nasai_50", "كتاب الاستعاذة")
        put("nasai_51", "كتاب الأشربة")

        // ========== SUNAN IBNU MAJAH (32 BAB) ==========
        put("ibnumajah_1", "كتاب المقدمة")
        put("ibnumajah_2", "كتاب الطهارة وسننها")
        put("ibnumajah_3", "كتاب الصلاة")
        put("ibnumajah_4", "كتاب الأذان والسنة فيها")
        put("ibnumajah_5", "كتاب المساجد والجماعات")
        put("ibnumajah_6", "كتاب إقامة الصلاة والسنة فيها")
        put("ibnumajah_7", "كتاب الجنائز")
        put("ibnumajah_8", "كتاب الصيام")
        put("ibnumajah_9", "كتاب الزكاة")
        put("ibnumajah_10", "كتاب النكاح")
        put("ibnumajah_11", "كتاب الطلاق")
        put("ibnumajah_12", "كتاب الكفارات")
        put("ibnumajah_13", "كتاب التجارات")
        put("ibnumajah_14", "كتاب الأحكام")
        put("ibnumajah_15", "كتاب الحدود")
        put("ibnumajah_16", "كتاب الديات")
        put("ibnumajah_17", "كتاب الوصايا")
        put("ibnumajah_18", "كتاب الفرائض")
        put("ibnumajah_19", "كتاب الجهاد")
        put("ibnumajah_20", "كتاب المناسك")
        put("ibnumajah_21", "كتاب الأضاحي")
        put("ibnumajah_22", "كتاب الذبائح")
        put("ibnumajah_23", "كتاب الصيد")
        put("ibnumajah_24", "كتاب الأطعمة")
        put("ibnumajah_25", "كتاب الأشربة")
        put("ibnumajah_26", "كتاب الطب")
        put("ibnumajah_27", "كتاب اللباس")
        put("ibnumajah_28", "كتاب الأدب")
        put("ibnumajah_29", "كتاب الدعاء")
        put("ibnumajah_30", "كتاب تعبير الرؤيا")
        put("ibnumajah_31", "كتاب الفتن")
        put("ibnumajah_32", "كتاب الزهد")
    }

    // 🟢 2. FUNGSI RESOLUSI CERDAS (Kutubus Sittah Asli -> Kamus Fiqih -> Angka Arab)
    fun resolveArabicTitle(bookSlug: String, indonesianTitle: String, chapterIndex: Int): String {
        val cleanSlug = bookSlug.lowercase().replace("-", "").replace("_", "").replace(" ", "")

        // 1. Cek dari Dataset Asli Kutubus Sittah (100% Otentik)
        val exactKey = "${cleanSlug}_$chapterIndex"
        val exactMatch = KUTUBUS_SITTAH_TITLES[exactKey]
        if (!exactMatch.isNullOrBlank()) {
            return exactMatch
        }

        // 2. Jika kitab lainnya (Ahmad, Malik, Darimi, dll), gunakan Kamus Kata Kunci Fiqih
        val cleanTitle = indonesianTitle.lowercase().trim()
        for ((keyword, arabic) in FALLBACK_FIQH_DICTIONARY) {
            if (cleanTitle.contains(keyword)) {
                return arabic
            }
        }

        // 3. Fallback Angka Arab (باب ١) jika belum ada di kamus
        return "باب ${toArabicDigits(chapterIndex)}"
    }

    private val FALLBACK_FIQH_DICTIONARY = mapOf(
        "wahyu" to "بدء الوحي",
        "iman" to "كتاب الإيمان",
        "ilmu" to "كتاب العلم",
        "wudhu" to "كتاب الوضوء",
        "mandi" to "كتاب الغسل",
        "haid" to "كتاب الحيض",
        "tayammum" to "كتاب التيمم",
        "shalat" to "كتاب الصلاة",
        "adzan" to "كتاب الأذان",
        "jumat" to "كتاب الجمعة",
        "jenazah" to "كتاب الجنائز",
        "zakat" to "كتاب الزكاة",
        "haji" to "كتاب الحج",
        "puasa" to "كتاب الصيام",
        "nikah" to "كتاب النكاح",
        "talak" to "كتاب الطلاق",
        "jual beli" to "كتاب البيوع",
        "jihad" to "كتاب الجهاد",
        "makanan" to "كتاب الأطعمة",
        "minuman" to "كتاب الأشربة",
        "pakaian" to "كتاب اللباس",
        "adab" to "كتاب الأدب",
        "doa" to "كتاب الدعاء",
        "tafsir" to "كتاب التفسير",
        "musnad wanita" to "مسند النساء",
        "wanita" to "مسند النساء",
        "anshar" to "مسند الأنصار",
        "sahabat anshar" to "مسند الأنصار",
        "sepuluh sahabat" to "العشرة المبشرون بالجنة",
        "ahlu bait" to "مسند أهل البيت",
        "ahlul bait" to "مسند أهل البيت",
        "bani hasyim" to "مسند بني هاشم",
        "quraisy" to "مسند قريش"
    )

    private fun toArabicDigits(number: Int): String {
        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return number.toString().map { char ->
            if (char.isDigit()) arabicDigits[char - '0'] else char
        }.joinToString("")
    }
}