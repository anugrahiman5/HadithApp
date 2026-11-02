package com.nusa.hadithapp.ui.search.components

/**
 * Helper cerdas untuk input teks dan harakat bahasa Arab.
 * Mencegah bug harakat bertumpuk tak wajar (seperti fathah + kasrah + dhammah pada satu huruf).
 */
object ArabicInputHelper {

    // Kumpulan harakat vokal Arab dasar
    private val VOWELS = setOf(
        '\u064B', // Tanwin Fathah (ـً)
        '\u064C', // Tanwin Dhammah (ـٌ)
        '\u064D', // Tanwin Kasrah (ـٍ)
        '\u064E', // Fathah (ـَ)
        '\u064F', // Dhammah (ـُ)
        '\u0650', // Kasrah (ـِ)
        '\u0652', // Sukun (ـْ)
        '\u0670'  // Alif Khanjariyah / Dagger Alif (ـٰ)
    )

    private const val TASYDID = '\u0651' // Tasydid / Shaddah (ـّ)

    /**
     * Menyisipkan karakter huruf atau harakat ke teks dengan kaidah ortografi Arab yang benar.
     */
    fun appendKey(currentText: String, newKey: String): String {
        if (newKey.isEmpty()) return currentText
        val newChar = newKey.first()

        // 1. Jika bukan harakat/tasydid (huruf hijaiyah biasa, angka, spasi, tanda baca), langsung append
        if (newChar !in VOWELS && newChar != TASYDID) {
            return currentText + newKey
        }

        // 2. Jika teks masih kosong atau huruf terakhir adalah spasi:
        // Harakat tidak boleh berdiri sendiri tanpa huruf; sandarkan otomatis pada Alif 'ا'
        if (currentText.isEmpty() || currentText.last() == ' ') {
            return currentText + "ا" + newChar
        }

        val lastChar = currentText.last()

        // 3. Kasus jika tombol yang ditekan adalah TASYDID (ـّ)
        if (newChar == TASYDID) {
            // Jika sudah ada tasydid pada posisi akhir, abaikan agar tidak dobel tasydid
            if (lastChar == TASYDID || (currentText.length >= 2 && currentText[currentText.length - 2] == TASYDID)) {
                return currentText
            }

            // Jika karakter terakhir adalah vokal (misal 'بَ'), sisipkan tasydid sebelum vokal ('بَّ')
            if (lastChar in VOWELS) {
                val prefix = currentText.dropLast(1)
                return prefix + TASYDID + lastChar
            }

            // Jika huruf terakhir konsonan biasa, tempelkan tasydid
            return currentText + TASYDID
        }

        // 4. Kasus jika tombol yang ditekan adalah VOKAL (Fathah, Kasrah, Dhammah, Sukun, Tanwin)
        if (newChar in VOWELS) {
            // Jika karakter terakhir SUDAH memiliki vokal (misal 'بَ' lalu ditekan kasrah):
            // GANTI vokal lama dengan vokal yang baru -> menjadi 'بِ' (BUKAN 'بَِ')
            if (lastChar in VOWELS) {
                return currentText.dropLast(1) + newChar
            }

            // Jika karakter terakhir adalah Tasydid (misal 'بّ'), tempelkan vokal setelah tasydid -> 'بَّ'
            if (lastChar == TASYDID) {
                return currentText + newChar
            }

            // Jika huruf terakhir konsonan biasa (misal 'ب'), tempelkan vokal -> 'بَ'
            return currentText + newChar
        }

        return currentText + newKey
    }
}
