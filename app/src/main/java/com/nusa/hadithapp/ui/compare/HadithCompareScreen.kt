package com.nusa.hadithapp.ui.compare

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.ui.components.dialogs.PerawiDetailDialog
import com.nusa.hadithapp.ui.compare.components.ComparedHadithCard
import com.nusa.hadithapp.ui.compare.components.MatanMatrixCard
import com.nusa.hadithapp.ui.compare.components.ReferenceHadithCard
import com.nusa.hadithapp.ui.compare.components.TakhrijSummaryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HadithCompareScreen(
    viewModel: HadithCompareViewModel,
    onBackClick: () -> Unit,
    onNavigateToHadithDetail: (bookSlug: String, number: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedPerawi by viewModel.selectedPerawi.collectAsState()
    val perawiProfileState by viewModel.perawiProfileState.collectAsState()
    val scholarCommentState by viewModel.scholarCommentState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hadis Serupa",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Rujukan Paralel & Komparasi Matan",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(32.dp)
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(44.dp)
                            )
                            Text(
                                text = "Menganalisis Varian Matan & Riwayat...",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Mengumpulkan jalur takhrij dari berbagai kitab hadits mu'tabar",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                uiState.errorMessage != null && uiState.referenceHadith == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.88f)
                                .padding(24.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(text = "⚠️", fontSize = 42.sp)
                                Text(
                                    text = "Gagal Memuat Komparasi",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = uiState.errorMessage ?: "Terjadi kesalahan.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Button(
                                    onClick = {
                                        // Trigger reload
                                        uiState.referenceHadith?.let {
                                            viewModel.loadComparison(it.bookSlug, it.number)
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text("Coba Lagi")
                                }
                            }
                        }
                    }
                }

                uiState.referenceHadith != null -> {
                    val refHadith = uiState.referenceHadith!!

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 1. Kartu Hadits Acuan
                        item(key = "reference_card") {
                            ReferenceHadithCard(
                                hadith = refHadith,
                                onRawiClick = { viewModel.selectPerawi(it) }
                            )
                        }

                        // 2. Kontrol Filter & Switch Highlight
                        item(key = "filter_and_toggle") {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Filter Kitab Pembanding:",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        item {
                                            val isAllSelected = uiState.selectedBookFilter == null
                                            FilterChip(
                                                selected = isAllSelected,
                                                onClick = { viewModel.filterByBook(null) },
                                                label = {
                                                    Text(
                                                        text = "Semua (${uiState.bookCounts["Semua"] ?: uiState.totalJalurCount})",
                                                        fontSize = 12.sp,
                                                        fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                            )
                                        }

                                        items(uiState.bookCounts.keys.filter { it != "Semua" }.toList()) { bookKey ->
                                            val isSelected = uiState.selectedBookFilter == bookKey
                                            val count = uiState.bookCounts[bookKey] ?: 0
                                            FilterChip(
                                                selected = isSelected,
                                                onClick = { viewModel.filterByBook(bookKey) },
                                                label = {
                                                    Text(
                                                        text = "$bookKey ($count)",
                                                        fontSize = 12.sp,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                            )
                                        }
                                    }

                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(end = 8.dp)
                                        ) {
                                            Text(
                                                text = "Sorot Perbedaan Lafaz (Highlight)",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Beri warna kuning pada kata yang berlainan dari hadis acuan",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Switch(
                                            checked = uiState.isHighlightEnabled,
                                            onCheckedChange = { viewModel.toggleHighlight(it) }
                                        )
                                    }
                                }
                            }
                        }

                        // 3. Ringkasan Takhrij
                        if (uiState.takhrijNotes.isNotBlank()) {
                            item(key = "takhrij_notes") {
                                TakhrijSummaryCard(notes = uiState.takhrijNotes)
                            }
                        }

                        // 4. Header Daftar Riwayat Pembanding
                        item(key = "section_header") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Riwayat Pembanding (${uiState.displayedHadiths.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // 5. Kartu-kartu Hadits Pembanding
                        if (uiState.displayedHadiths.isEmpty()) {
                            item(key = "empty_filtered") {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    )
                                ) {
                                    Text(
                                        text = "Tidak ada riwayat pembanding untuk filter kitab yang dipilih.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(20.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            itemsIndexed(
                                items = uiState.displayedHadiths,
                                key = { _, item -> item.id }
                            ) { index, item ->
                                ComparedHadithCard(
                                    item = item,
                                    baseArabicText = refHadith.arabicText,
                                    index = index + 1,
                                    isHighlightEnabled = uiState.isHighlightEnabled,
                                    onCopyClick = {
                                        val textToCopy = buildString {
                                            appendLine("${item.bookName} No. ${item.number}")
                                            appendLine("Bab: ${item.babName.ifBlank { item.kitabName }}")
                                            appendLine()
                                            appendLine(item.arabicText)
                                            appendLine()
                                            appendLine(item.translation)
                                        }
                                        clipboardManager.setText(AnnotatedString(textToCopy))
                                        Toast.makeText(context, "Hadis disalin ke clipboard", Toast.LENGTH_SHORT).show()
                                    },
                                    onOpenHadithClick = {
                                        onNavigateToHadithDetail(item.bookSlug, item.number)
                                    },
                                    onRawiClick = { viewModel.selectPerawi(it) }
                                )
                            }
                        }

                        // Tombol Muat Riwayat Tersisa (Jika total > 6 riwayat)
                        if (uiState.remainingToLoadCount > 0) {
                            item(key = "load_remaining_btn") {
                                OutlinedButton(
                                    onClick = { viewModel.loadRemainingHadiths() },
                                    enabled = !uiState.isLoadingRemaining,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                                ) {
                                    if (uiState.isLoadingRemaining) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text(text = "Memuat riwayat lainnya...")
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text(
                                            text = "Muat ${uiState.remainingToLoadCount} Riwayat Serupa Lainnya",
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }

                        // 6. Matan Matrix Table
                        if (uiState.matanMatrix.isNotEmpty()) {
                            item(key = "matan_matrix") {
                                MatanMatrixCard(rows = uiState.matanMatrix)
                            }
                        }

                        // 7. Spacer bawah
                        item(key = "bottom_spacer") {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }

    // Dialog Detail Profil Perawi ketika diklik pada rantai sanad
    selectedPerawi?.let { perawi ->
        PerawiDetailDialog(
            perawi = perawi,
            perawiProfileState = perawiProfileState,
            scholarCommentState = scholarCommentState,
            onDismiss = { viewModel.dismissPerawi() },
            onFetchBookHadithNumbers = { slug ->
                viewModel.loadPerawiHadithNumbers(slug, perawi.kodeRawi)
            },
            onNavigateToHadith = { targetSlug, targetNumber, _, _, _ ->
                viewModel.dismissPerawi()
                onNavigateToHadithDetail(targetSlug, targetNumber)
            }
        )
    }
}
