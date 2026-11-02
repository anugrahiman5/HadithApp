package com.nusa.hadithapp.util

import android.util.Log
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.nusa.hadithapp.R

private const val TAG = "FontProvider"

/**
 * Dapatkan Indonesian font family berdasarkan nama
 * @param fontName Nama font (Roboto, Montserrat, etc)
 * @return FontFamily atau default jika tidak ditemukan
 */
fun getIndonesianFontFamily(fontName: String): FontFamily {
    return try {
        when (fontName.lowercase().trim()) {
            "roboto" -> FontFamily(Font(R.font.roboto))
            "montserrat" -> FontFamily(Font(R.font.montserrat))
            "open sans" -> FontFamily(Font(R.font.opensans))
            "ubuntu" -> FontFamily(Font(R.font.ubuntu))
            "kodchasan" -> FontFamily(Font(R.font.kodchasan))
            "sanchez" -> FontFamily(Font(R.font.sanchez))
            else -> {
                Log.w(TAG, "Unknown Indonesian font: $fontName, using default")
                FontFamily.Default
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error loading Indonesian font: $fontName", e)
        FontFamily.Default
    }
}

/**
 * Dapatkan Arabic font family berdasarkan nama
 * @param fontName Nama font (Noto Naskh, Lateef, Scheherazade, Uthmanic, etc)
 * @return FontFamily dengan fallback ke Noto Naskh
 */
fun getArabicFontFamily(fontName: String = "amiri"): FontFamily {
    return try {
        when (fontName.lowercase().trim()) {
            "lateef" -> FontFamily(Font(R.font.lateef))
            "noto naskh", "amiri", "amiri naskh" -> FontFamily(Font(R.font.noto_naskh))
            "scheherazade" -> FontFamily(Font(R.font.scheherazade))
            "uthman taha" -> FontFamily(Font(R.font.uthman_taha))
            "uthmanic", "uthmanic hafs" -> FontFamily(Font(R.font.uthmanic_hafs))
            else -> {
                Log.w(TAG, "Unknown Arabic font: $fontName, using Noto Naskh")
                FontFamily(Font(R.font.noto_naskh))
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error loading Arabic font: $fontName", e)
        FontFamily(Font(R.font.noto_naskh))
    }
}

/**
 * List semua Indonesian fonts yang tersedia
 */
val INDONESIAN_FONTS = listOf(
    "Roboto",
    "Montserrat",
    "Open Sans",
    "Ubuntu",
    "Kodchasan",
    "Sanchez"
)

/**
 * List semua Arabic fonts yang tersedia
 */
val ARABIC_FONTS = listOf(
    "Amiri Naskh",
    "Scheherazade",
    "Uthmanic Hafs",
    "Lateef",
    "Uthman Taha"
)
