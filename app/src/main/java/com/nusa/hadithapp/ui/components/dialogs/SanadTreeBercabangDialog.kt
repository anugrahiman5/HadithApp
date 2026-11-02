package com.nusa.hadithapp.ui.components.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nusa.hadithapp.domain.model.SanadPerawi
import com.nusa.hadithapp.domain.model.getQualityZoneInfo

/**
 * Dialog Bagan Pohon Sanad Bercabang
 * Mengadopsi arsitektur visual Gambar 3:
 * 1. Teks perawi dan informasi derajat/wafat berkontras tinggi dan jelas terbaca.
 * 2. Visualisasi percabangan bergaris (Branching Fork Connector) yang presisi antar-jalur.
 * 3. Pewarnaan elegan dengan aksen garis di sisi kanan kartu perawi.
 */
@Composable
fun SanadTreeBercabangDialog(
    sanadJalurList: List<List<SanadPerawi>>,
    onDismiss: () -> Unit,
    onSelectPerawi: (SanadPerawi) -> Unit,
    bookTitle: String = "",
    hadithNumber: Int = 0
) {
    val presentQualities = remember(sanadJalurList) {
        sanadJalurList.flatten()
            .map { it.quality }
            .distinct()
            .sorted()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                // Header Dialog
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (bookTitle.isNotBlank()) "Sanad $bookTitle ${if (hadithNumber > 0) hadithNumber else ""}" else "Bagan Pohon Sanad",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (sanadJalurList.size > 1) {
                                "${sanadJalurList.size} Jalur Transmisi Riwayat (Bercabang)"
                            } else {
                                "1 Jalur Transmisi Riwayat (Muttashil)"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Legenda Derajat Dinamis (Hanya menampilkan level perawi yang ada pada hadits ini)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    presentQualities.forEach { qualityLevel ->
                        val zone = getQualityZoneInfo(qualityLevel)
                        val shortLabel = when (qualityLevel) {
                            1 -> "Shahabat"
                            2 -> "Tsiqatun Tsiqah"
                            3 -> "Tsiqah"
                            4 -> "Shaduq"
                            5 -> "Shaduq Dha'if"
                            6 -> "Dha'if Ringan"
                            7 -> "Dha'if"
                            8 -> "Dha'if Jiddan"
                            9 -> "Matruk"
                            10 -> "Maudhu'"
                            11 -> "Majhul"
                            12 -> "Mubham"
                            else -> zone.groupName.substringBefore(" (")
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = zone.color.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, zone.color.copy(alpha = 0.6f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(zone.color, CircleShape)
                                )
                                Text(
                                    text = shortLabel,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(12.dp))

                // Area Scroll Diagram (Vertikal & Horizontal)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .horizontalScroll(rememberScrollState()),
                    contentAlignment = Alignment.TopCenter
                ) {
                    if (sanadJalurList.isEmpty()) {
                        Text(
                            text = "Data sanad tidak tersedia untuk hadits ini.",
                            modifier = Modifier.padding(32.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else if (sanadJalurList.size == 1) {
                        // Jalur Tunggal (Single Path)
                        SingleJalurColumn(
                            jalur = sanadJalurList.first(),
                            onSelectPerawi = onSelectPerawi
                        )
                    } else {
                        // Multi-Jalur Bercabang (Branching Path Tree)
                        MultiJalurTreeGraph(
                            sanadJalurList = sanadJalurList,
                            onSelectPerawi = onSelectPerawi
                        )
                    }
                }
            }
        }
    }
}

/**
 * Diagram Sanad untuk 1 Jalur Tunggal
 */
@Composable
private fun SingleJalurColumn(
    jalur: List<SanadPerawi>,
    onSelectPerawi: (SanadPerawi) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        jalur.forEachIndexed { index, perawi ->
            PerawiTreeNodeCard(
                perawi = perawi,
                onClick = { onSelectPerawi(perawi) }
            )

            if (index < jalur.size - 1) {
                VerticalConnectorArrow()
            }
        }
    }
}

/**
 * Diagram Pohon Sanad Bercabang Multi-Jalur (Sesuai Gambar 3)
 */
@Composable
private fun MultiJalurTreeGraph(
    sanadJalurList: List<List<SanadPerawi>>,
    onSelectPerawi: (SanadPerawi) -> Unit
) {
    // 1. Temukan perawi yang sama di awal jalur (Common Prefix / Madar)
    val minLength = sanadJalurList.minOfOrNull { it.size } ?: 0
    var divergeIndex = 0

    while (divergeIndex < minLength) {
        val firstRawiId = sanadJalurList.first()[divergeIndex].kodeRawi
        val isAllSame = sanadJalurList.all { it[divergeIndex].kodeRawi == firstRawiId }
        if (!isAllSame) break
        divergeIndex++
    }

    val commonPerawis = remember(sanadJalurList, divergeIndex) {
        if (divergeIndex > 0) sanadJalurList.first().take(divergeIndex) else emptyList()
    }

    val branchedJalurs = remember(sanadJalurList, divergeIndex) {
        sanadJalurList.map { it.drop(divergeIndex) }
    }

    val columnWidth = 230.dp
    val spacing = 20.dp
    val branchCount = branchedJalurs.size
    val totalBranchWidth = (columnWidth * branchCount) + (spacing * (branchCount - 1))

    Column(
        modifier = Modifier.padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Render Silsilah Bersama di Atas (Sebelum Percabangan)
        commonPerawis.forEachIndexed { index, perawi ->
            PerawiTreeNodeCard(
                perawi = perawi,
                onClick = { onSelectPerawi(perawi) }
            )

            if (index < commonPerawis.size - 1 || branchedJalurs.any { it.isNotEmpty() }) {
                VerticalConnectorArrow()
            }
        }

        // Render Garis Percabangan (Fork Connector) jika ada cabang
        if (branchedJalurs.any { it.isNotEmpty() }) {
            BranchForkConnector(
                branchCount = branchCount,
                columnWidth = columnWidth,
                spacing = spacing,
                modifier = Modifier.width(totalBranchWidth)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Kolom Cabang Berdampingan
            Row(
                modifier = Modifier.width(totalBranchWidth),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalAlignment = Alignment.Top
            ) {
                branchedJalurs.forEachIndexed { branchIndex, branchList ->
                    Column(
                        modifier = Modifier.width(columnWidth),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "Jalur ${branchIndex + 1}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }

                        branchList.forEachIndexed { idx, perawi ->
                            PerawiTreeNodeCard(
                                perawi = perawi,
                                onClick = { onSelectPerawi(perawi) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (idx < branchList.size - 1) {
                                VerticalConnectorArrow()
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Kartu Perawi Bersih dengan Garis Aksen Warna Derajat di Tepi Kanan (Desain Gambar 3)
 */
@Composable
private fun PerawiTreeNodeCard(
    perawi: SanadPerawi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val zoneInfo = getQualityZoneInfo(perawi.quality)
    val cleanName = remember(perawi.name) {
        perawi.name.replace("\"", "").replace("\\", "").trim()
    }

    Card(
        modifier = modifier
            .widthIn(min = 230.dp, max = 280.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = cleanName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                // Baris 1: Keterangan Kalangan / Generasi Perawi
                if (perawi.kalangan.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = perawi.kalangan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Baris 2: Tahun Wafat Perawi (Dibuat baris terpisah agar tidak terpotong elipsis)
                perawi.tahunWafat?.takeIf { it.isNotBlank() }?.let { wafat ->
                    val cleanYear = if (wafat.contains("H", ignoreCase = true)) wafat.trim() else "${wafat.trim()} H"
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "wafat $cleanYear",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }

            // Aksen Garis Kualitas di Tepi Kanan (Gaya Gambar 3)
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(
                        color = zoneInfo.color,
                        shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                    )
            )
        }
    }
}

/**
 * Garis Panah Konektor Vertikal (Silsilah Guru -> Murid)
 */
@Composable
private fun VerticalConnectorArrow(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(10.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f))
        )
        Icon(
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = "Silsilah ke murid",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}

/**
 * Garis Konektor Percabangan (Fork Line)
 * Dihitung presisi agar garis horizontal dan vertikal jatuh tepat di tengah setiap kolom cabang
 */
@Composable
private fun BranchForkConnector(
    branchCount: Int = 2,
    columnWidth: Dp = 230.dp,
    spacing: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.85f)
    val density = LocalDensity.current

    Box(
        modifier = modifier.height(36.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeW = 2.dp.toPx()
            val midY = size.height / 2f
            val centerX = size.width / 2f
            val colWidthPx = with(density) { columnWidth.toPx() }
            val spacingPx = with(density) { spacing.toPx() }

            // 1. Batang vertikal atas (dari guru bersama)
            drawLine(
                color = lineColor,
                start = Offset(centerX, 0f),
                end = Offset(centerX, midY),
                strokeWidth = strokeW
            )

            // Hitung koordinat pusat kolom pertama dan kolom terakhir
            val firstCenter = colWidthPx / 2f
            val lastCenter = ((branchCount - 1) * (colWidthPx + spacingPx)) + (colWidthPx / 2f)

            // 2. Garis horizontal membentang tepat di antara pusat kolom cabang
            drawLine(
                color = lineColor,
                start = Offset(firstCenter, midY),
                end = Offset(lastCenter, midY),
                strokeWidth = strokeW
            )

            // 3. Garis turun ke pusat masing-masing kolom cabang
            for (i in 0 until branchCount) {
                val branchX = (i * (colWidthPx + spacingPx)) + (colWidthPx / 2f)
                drawLine(
                    color = lineColor,
                    start = Offset(branchX, midY),
                    end = Offset(branchX, size.height),
                    strokeWidth = strokeW
                )
            }
        }
    }
}

/**
 * Item Legenda Kualitas Perawi
 */
@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .background(color, CircleShape)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
