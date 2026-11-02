package com.nusa.hadithapp.util

import com.nusa.hadithapp.domain.model.HeatmapDay
import com.nusa.hadithapp.domain.model.HeatmapWeek
import com.nusa.hadithapp.domain.model.ReadingHistoryItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object StudyHeatmapGenerator {

    private val idLocale = Locale.Builder().setLanguage("id").setRegion("ID").build()
    private val keyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayFormat = SimpleDateFormat("EEEE, d MMM yyyy", idLocale)
    private val monthFormat = SimpleDateFormat("MMM", idLocale)

    /**
     * Membangun grid heatmap aktivitas belajar hadis ala GitHub untuk [weeksCount] pekan terakhir.
     */
    fun generate(history: List<ReadingHistoryItem>, weeksCount: Int = 14): List<HeatmapWeek> {
        val countsByDate = mutableMapOf<String, Int>()
        for (item in history) {
            val dateKey = keyFormat.format(Date(item.readAt))
            countsByDate[dateKey] = (countsByDate[dateKey] ?: 0) + 1
        }

        val todayCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayKey = keyFormat.format(todayCal.time)

        // Cari hari Senin pada pekan pertama (mundur weeksCount - 1 pekan)
        val startCal = todayCal.clone() as Calendar
        val dayOfWeekToday = startCal.get(Calendar.DAY_OF_WEEK)
        // Convert Java Calendar (1 = Sun .. 7 = Sat) ke ISO (1 = Mon .. 7 = Sun)
        val isoDayOfWeekToday = if (dayOfWeekToday == Calendar.SUNDAY) 7 else dayOfWeekToday - 1

        // Mundur ke hari Senin pekan ini
        startCal.add(Calendar.DAY_OF_MONTH, -(isoDayOfWeekToday - 1))
        // Mundur sebanyak (weeksCount - 1) pekan
        startCal.add(Calendar.DAY_OF_MONTH, -((weeksCount - 1) * 7))

        val resultWeeks = mutableListOf<HeatmapWeek>()
        var lastMonthName = ""

        val cursorCal = startCal.clone() as Calendar

        for (w in 0 until weeksCount) {
            val daysInWeek = mutableListOf<HeatmapDay>()
            var weekMonthLabel: String? = null

            // Tentukan label bulan berdasarkan hari Kamis pekan ini (ISO 8601 standard)
            // Pekan baru dianggap masuk bulan M jika hari Kamis berada di bulan M
            val thursdayCal = cursorCal.clone() as Calendar
            thursdayCal.add(Calendar.DAY_OF_MONTH, 3) // Senin + 3 hari = Kamis
            val thursdayMonth = monthFormat.format(thursdayCal.time)
            if (thursdayMonth != lastMonthName) {
                weekMonthLabel = thursdayMonth
                lastMonthName = thursdayMonth
            }

            for (d in 1..7) {
                val dateKey = keyFormat.format(cursorCal.time)
                val count = countsByDate[dateKey] ?: 0
                val isToday = (dateKey == todayKey)
                val isFuture = cursorCal.after(todayCal)

                val level = when {
                    isFuture || count == 0 -> 0
                    count in 1..2 -> 1
                    count in 3..5 -> 2
                    count in 6..9 -> 3
                    else -> 4
                }

                // Ganti "Minggu" menjadi "Ahad" agar konsisten dengan singkatan "Ahd" di grid
                val formattedDisplayDate = displayFormat.format(cursorCal.time).replace("Minggu", "Ahad")

                daysInWeek.add(
                    HeatmapDay(
                        dateString = dateKey,
                        displayDate = formattedDisplayDate,
                        dayOfWeek = d,
                        count = count,
                        level = level,
                        isToday = isToday,
                        isFuture = isFuture
                    )
                )

                cursorCal.add(Calendar.DAY_OF_MONTH, 1)
            }

            resultWeeks.add(
                HeatmapWeek(
                    weekNumber = w,
                    monthLabel = weekMonthLabel,
                    days = daysInWeek
                )
            )
        }

        return resultWeeks
    }

    /**
     * Menghitung streak konsistensi membaca hadis berturut-turut.
     */
    fun calculateStreak(history: List<ReadingHistoryItem>): Int {
        if (history.isEmpty()) return 0

        val readDates = history.map { keyFormat.format(Date(it.readAt)) }.toSet()

        val cal = Calendar.getInstance()
        val todayKey = keyFormat.format(cal.time)

        cal.add(Calendar.DAY_OF_MONTH, -1)
        val yesterdayKey = keyFormat.format(cal.time)

        var streak = 0
        val checkCal = Calendar.getInstance()

        // Jika hari ini belum membaca, tapi kemarin membaca -> streak tetap berlanjut dari kemarin
        if (!readDates.contains(todayKey)) {
            if (!readDates.contains(yesterdayKey)) {
                return 0
            }
            checkCal.add(Calendar.DAY_OF_MONTH, -1)
        }

        while (true) {
            val key = keyFormat.format(checkCal.time)
            if (readDates.contains(key)) {
                streak++
                checkCal.add(Calendar.DAY_OF_MONTH, -1)
            } else {
                break
            }
        }

        return streak
    }
}
