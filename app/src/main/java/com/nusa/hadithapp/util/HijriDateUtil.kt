package com.nusa.hadithapp.util

import android.os.Build
import android.util.Log
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

object HijriDateUtil {

    private const val TAG = "HijriDateUtil"

    // Month names berdasarkan Islamic calendar
    private val HIJRI_MONTH_NAMES = arrayOf(
        "Muharram", "Safar", "Rabiul Awal", "Rabiul Akhir",
        "Jumadil Awal", "Jumadil Akhir", "Rajab", "Sya'ban",
        "Ramadhan", "Syawal", "Dzulqa'dah", "Dzulhijjah"
    )

    /**
     * Ambil tanggal Hijri saat ini dalam format "DD Bulan YYYY H"
     * @return String tanggal Hijri atau fallback jika error
     */
    fun getCurrentHijriDate(): String {
        return getHijriDate(LocalDate.now())
    }

    /**
     * Convert tanggal Gregorian ke Hijri
     * @param gregorianDate Tanggal dalam kalender Gregorian
     * @return String tanggal Hijri dalam format "DD Bulan YYYY H"
     */
    fun getHijriDate(gregorianDate: LocalDate = LocalDate.now()): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val hijrahDate = HijrahDate.from(gregorianDate)
                val day = hijrahDate.get(ChronoField.DAY_OF_MONTH)
                val month = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
                val year = hijrahDate.get(ChronoField.YEAR)

                // Validasi month (1-12)
                val monthName = getMonthName(month)

                "$day $monthName $year H"
            } catch (e: Exception) {
                Log.e(TAG, "Error converting Hijri date: ${e.message}", e)
                getDefaultHijriDate()
            }
        } else {
            Log.w(TAG, "Android version < O, using fallback Hijri date")
            getDefaultHijriDate()
        }
    }

    /**
     * Ambil nama bulan Hijri
     * @param monthNumber Nomor bulan (1-12)
     * @return Nama bulan atau "Bulan Tidak Dikenal" jika invalid
     */
    fun getMonthName(monthNumber: Int): String {
        return if (monthNumber in 1..12) {
            HIJRI_MONTH_NAMES[monthNumber - 1]
        } else {
            Log.w(TAG, "Invalid month number: $monthNumber")
            "Bulan Tidak Dikenal"
        }
    }

    /**
     * Dapatkan tanggal Hijri default (fallback)
     * Note: Harus di-update secara berkala
     */
    private fun getDefaultHijriDate(): String {
        val currentYear = LocalDate.now().year
        val estimatedHijriYear = 1440 + (currentYear - 2019)
        return "$estimatedHijriYear H"
    }

    /**
     * Get bulan Hijri names list
     */
    fun getMonthNames(): List<String> = HIJRI_MONTH_NAMES.toList()
}