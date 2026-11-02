package com.nusa.hadithapp.ui.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.HeatmapDay
import com.nusa.hadithapp.domain.model.HeatmapWeek

@Composable
fun ProfileHeatmapCard(
    heatmapWeeks: List<HeatmapWeek>,
    currentStreakDays: Int,
    totalRecentReadCount: Int,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    var selectedDay by remember {
        mutableStateOf(heatmapWeeks.flatMap { it.days }.find { it.isToday })
    }
    val scrollState = rememberScrollState()

    val cellSize = 14.dp
    val cellSpacing = 3.5.dp

    // Auto-scroll ke paling kanan (pekan terbaru) saat pertama kali dimuat & default ke hari ini
    LaunchedEffect(heatmapWeeks) {
        if (heatmapWeeks.isNotEmpty()) {
            if (selectedDay == null) {
                selectedDay = heatmapWeeks.flatMap { it.days }.find { it.isToday }
            }
            scrollState.scrollTo(scrollState.maxValue)
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Judul & Badge Streak
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF2E7D32).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoGraph,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Peta Konsistensi Belajar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$totalRecentReadCount hadis dibaca dalam ${heatmapWeeks.size} pekan",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (currentStreakDays > 0) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE65100).copy(alpha = 0.15f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "$currentStreakDays Hari Aktif",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF9800)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

            // Grid Heatmap dengan horizontal scroll
            val dayLabels = listOf("Sen", "", "Rab", "", "Jum", "", "Ahd")
            val labelWidth = 28.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(cellSpacing)
                ) {
                    // 1. Header Bulan
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(cellSpacing),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(labelWidth))
                        heatmapWeeks.forEach { week ->
                            Box(
                                modifier = Modifier.width(cellSize),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (!week.monthLabel.isNullOrBlank()) {
                                    Text(
                                        text = week.monthLabel,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                                        softWrap = false,
                                        modifier = Modifier.wrapContentWidth(unbounded = true)
                                    )
                                }
                            }
                        }
                    }

                    // 2. Baris Hari (Senin s.d. Ahad)
                    dayLabels.forEachIndexed { dayIdx, label ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(cellSpacing),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.width(labelWidth),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (label.isNotBlank()) {
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                                        maxLines = 1
                                    )
                                }
                            }

                            heatmapWeeks.forEach { week ->
                                val day = week.days.getOrNull(dayIdx)
                                if (day != null) {
                                    val isSelected = (selectedDay?.dateString == day.dateString)
                                    val baseColor = getHeatmapColor(day.level, isDark)
                                    val cellColor = if (day.isFuture) baseColor.copy(alpha = 0.25f) else baseColor

                                    Box(
                                        modifier = Modifier
                                            .size(cellSize)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(cellColor)
                                            .then(
                                                when {
                                                    isSelected -> Modifier.border(
                                                        2.dp,
                                                        if (isDark) Color(0xFF55D6A0) else Color(0xFF2E7D32),
                                                        RoundedCornerShape(3.dp)
                                                    )
                                                    day.isToday -> Modifier.border(
                                                        1.2.dp,
                                                        if (isDark) Color(0xFF55D6A0) else Color(0xFF2E7D32),
                                                        RoundedCornerShape(3.dp)
                                                    )
                                                    else -> Modifier
                                                }
                                            )
                                            .clickable(enabled = !day.isFuture) { selectedDay = day }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.size(cellSize))
                                }
                            }
                        }
                    }
                }
            }

            // Info Detail Hari yang Dipilih
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val activeDay = selectedDay ?: heatmapWeeks.flatMap { it.days }.find { it.isToday }
                    if (activeDay != null) {
                        Text(
                            text = "${activeDay.displayDate}${if (activeDay.isToday) " (Hari ini)" else if (activeDay.isFuture) " (Mendatang)" else ""}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = when {
                                activeDay.isFuture -> "Hari belum tiba"
                                activeDay.count > 0 -> "${activeDay.count} hadis dibaca"
                                else -> "Belum ada bacaan"
                            },
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (activeDay.count > 0) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Ketuk salah satu kotak untuk rincian tanggal",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Legend / Keterangan Warna ala GitHub
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Intensitas Bacaan",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Sedikit",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    (0..4).forEach { level ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(getHeatmapColor(level, isDark))
                        )
                    }
                    Text(
                        text = "Banyak",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun getHeatmapColor(level: Int, isDark: Boolean): Color {
    return if (isDark) {
        when (level) {
            1 -> Color(0xFF14532D) // Level 1: Hijau lembut gelap terbaca
            2 -> Color(0xFF16A34A) // Level 2: Hijau emerald sedang
            3 -> Color(0xFF22C55E) // Level 3: Hijau cerah
            4 -> Color(0xFF4ADE80) // Level 4: Hijau menyala segar
            else -> Color(0xFF1E293B) // Level 0: Slate netral
        }
    } else {
        when (level) {
            1 -> Color(0xFFBBF7D0) // Level 1: Mint pastel
            2 -> Color(0xFF86EFAC) // Level 2: Hijau muda segar
            3 -> Color(0xFF22C55E) // Level 3: Hijau daun
            4 -> Color(0xFF15803D) // Level 4: Forest emerald pekat
            else -> Color(0xFFE2E8F0) // Level 0: Abu-abu bersih
        }
    }
}
