package com.nusa.hadithapp.domain.model

data class HeatmapDay(
    val dateString: String,      // "2026-10-30"
    val displayDate: String,     // "Jumat, 30 Okt 2026"
    val dayOfWeek: Int,          // 1 = Senin .. 7 = Ahad
    val count: Int,              // Jumlah hadis yang dibaca
    val level: Int,              // 0..4 (0: 0, 1: 1-2, 2: 3-5, 3: 6-9, 4: >=10)
    val isToday: Boolean = false,
    val isFuture: Boolean = false
)

data class HeatmapWeek(
    val weekNumber: Int,
    val monthLabel: String? = null, // e.g. "Agu", "Sep", "Okt" jika minggu ini awal bulan
    val days: List<HeatmapDay>     // 7 hari (Senin .. Ahad)
)
