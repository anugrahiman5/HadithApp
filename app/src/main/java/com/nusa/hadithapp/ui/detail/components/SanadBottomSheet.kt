package com.nusa.hadithapp.ui.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.SanadPerawi
import com.nusa.hadithapp.domain.model.SimilarHadith
import com.nusa.hadithapp.domain.model.getQualityZoneInfo
import com.nusa.hadithapp.ui.detail.SimilarHadithUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SanadBottomSheet(
    bookSlug: String,
    sanadJalurList: List<List<SanadPerawi>>,
    similarHadithState: SimilarHadithUiState,
    initialTabIndex: Int = 0,
    onRequestSimilarHadith: () -> Unit,
    onSimilarHadithClick: (bookSlug: String, number: Int) -> Unit,
    onPerawiClick: (SanadPerawi) -> Unit,
    onOpenTreeDialog: () -> Unit,
    onOpenQualityDialog: () -> Unit,
    onOpenCompare: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) }
    var selectedJalurIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Sanad", "Hadits Serupa")
    val currentJalur = sanadJalurList.getOrNull(selectedJalurIndex) ?: emptyList()

    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 1 && similarHadithState is SimilarHadithUiState.Empty) {
            onRequestSimilarHadith()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTabIndex) {
                0 -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            itemsIndexed(sanadJalurList) { index, _ ->
                                val isSelected = selectedJalurIndex == index
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedJalurIndex = index },
                                    label = { Text("Jalur ${index + 1}") },
                                    shape = CircleShape,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }

                        IconButton(onClick = onOpenTreeDialog) {
                            Icon(
                                imageVector = Icons.Default.AccountTree,
                                contentDescription = "Lihat Bagan Pohon Sanad",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(onClick = onOpenQualityDialog) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Legenda Kredibilitas Perawi",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        itemsIndexed(currentJalur) { index, perawi ->
                            PerawiSanadCard(
                                perawi = perawi,
                                bookSlug = bookSlug,
                                onClick = { onPerawiClick(perawi) }
                            )

                            if (index < currentJalur.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> {
                    when (similarHadithState) {
                        is SimilarHadithUiState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(36.dp))
                                    Text(
                                        text = "Mencari hadits dengan matan serupa (takhrij)...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        is SimilarHadithUiState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = similarHadithState.message,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    OutlinedButton(onClick = onRequestSimilarHadith) {
                                        Text("Coba Lagi")
                                    }
                                }
                            }
                        }
                        is SimilarHadithUiState.Success -> {
                            if (similarHadithState.items.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(260.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MenuBook,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Text(
                                            text = "Tidak ditemukan hadits pembanding di kitab lain.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            } else {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (onOpenCompare != null) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 12.dp)
                                                .clickable {
                                                    onDismiss()
                                                    onOpenCompare()
                                                },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                            ),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(36.dp)
                                                            .clip(CircleShape)
                                                            .background(MaterialTheme.colorScheme.primary),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.onPrimary,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                    }
                                                    Column {
                                                        Text(
                                                            text = "Buka Komparasi Matan Lengkap",
                                                            style = MaterialTheme.typography.titleSmall,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                        Text(
                                                            text = "Analisis variasi lafaz, redaksi dan takhrij jalur",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            fontSize = 11.sp,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }

                                    Text(
                                        text = "Ditemukan ${similarHadithState.items.size} hadits serupa dari kitab lain:",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(380.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(similarHadithState.items) { item ->
                                            SimilarHadithCard(
                                                item = item,
                                                onClick = {
                                                    onSimilarHadithClick(item.bookSlug, item.number)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        SimilarHadithUiState.Empty -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(32.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SimilarHadithCard(
    item: SimilarHadith,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = item.bookName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Hadits Nomor ${item.number}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Text(
                text = "Buka >",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun PerawiSanadCard(
    perawi: SanadPerawi,
    bookSlug: String,
    onClick: () -> Unit
) {
    val zoneInfo = getQualityZoneInfo(perawi.quality)
    val riwayatInfo = getRiwayatCount(perawi, bookSlug)

    val formattedWafat = perawi.tahunWafat?.takeIf { it.isNotBlank() }?.let { tahun ->
        val cleanTahun = tahun.trim()
        if (cleanTahun.contains("H", ignoreCase = true)) cleanTahun else "$cleanTahun H"
    }

    val metadata = buildString {
        if (perawi.kalangan.isNotBlank()) {
            append(perawi.kalangan)
        }
        if (formattedWafat != null) {
            if (isNotEmpty()) append(" - ")
            append("wafat $formattedWafat")
        }
        if (riwayatInfo != null && riwayatInfo.first > 0) {
            if (isNotEmpty()) append(" - ")
            append("${riwayatInfo.first} riwayat di ${riwayatInfo.second}")
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = zoneInfo.color.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(zoneInfo.color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = zoneInfo.symbol,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = zoneInfo.textColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = perawi.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (metadata.isBlank()) "Detail perawi tidak tersedia" else metadata,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun getRiwayatCount(perawi: SanadPerawi, bookSlug: String): Pair<Int, String>? {
    val cleanSlug = bookSlug.lowercase()
    return when {
        cleanSlug.contains("bukhari") -> perawi.rBukhari?.let { Pair(it, "Bukhari") }
        cleanSlug.contains("muslim") -> perawi.rMuslim?.let { Pair(it, "Muslim") }
        cleanSlug.contains("daud") -> perawi.rAbuDaud?.let { Pair(it, "Abu Daud") }
        cleanSlug.contains("tirmidzi") -> perawi.rTirmidzi?.let { Pair(it, "Tirmidzi") }
        cleanSlug.contains("nasai") -> perawi.rNasai?.let { Pair(it, "Nasa'i") }
        cleanSlug.contains("majah") -> perawi.rIbnuMajah?.let { Pair(it, "Ibnu Majah") }
        cleanSlug.contains("ahmad") -> perawi.rAhmad?.let { Pair(it, "Ahmad") }
        cleanSlug.contains("malik") -> perawi.rMalik?.let { Pair(it, "Malik") }
        cleanSlug.contains("darimi") -> perawi.rDarimi?.let { Pair(it, "Darimi") }
        else -> null
    }
}
