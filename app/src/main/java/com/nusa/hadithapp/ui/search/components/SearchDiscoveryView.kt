package com.nusa.hadithapp.ui.search.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class PopularTopic(
    val emoji: String,
    val title: String,
    val searchKeyword: String
)

private val POPULAR_TOPICS = listOf(
    PopularTopic("🤲", "Niat & Keikhlasan", "Niat"),
    PopularTopic("🕌", "Shalat Berjamaah", "Shalat"),
    PopularTopic("💎", "Sabar & Tawakal", "Sabar"),
    PopularTopic("🤝", "Akhlak & Adab", "Akhlak"),
    PopularTopic("💖", "Berbakti Orang Tua", "Orang Tua"),
    PopularTopic("🌙", "Puasa & Ramadhan", "Puasa"),
    PopularTopic("💰", "Sedekah & Zakat", "Sedekah"),
    PopularTopic("🛡️", "Thaharah & Bersuci", "Wudhu"),
    PopularTopic("📿", "Dzikir & Doa", "Dzikir"),
    PopularTopic("📚", "Menuntut Ilmu", "Ilmu"),
    PopularTopic("⚖️", "Kejujuran & Muamalah", "Jujur"),
    PopularTopic("🕊️", "Taubat & Istighfar", "Taubat")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchDiscoveryView(
    recentSearches: List<String>,
    onQueryClick: (String) -> Unit,
    onRemoveRecentQuery: (String) -> Unit,
    onClearAllRecent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. BAGIAN PENCARIAN TERAKHIR
        if (recentSearches.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Pencarian Terakhir",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    TextButton(
                        onClick = onClearAllRecent,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Hapus Semua",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    recentSearches.forEach { query ->
                        InputChip(
                            selected = false,
                            onClick = { onQueryClick(query) },
                            label = { Text(query, fontSize = 12.sp) },
                            shape = RoundedCornerShape(18.dp),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Hapus",
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable { onRemoveRecentQuery(query) }
                                )
                            },
                            colors = InputChipDefaults.inputChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        )
                    }
                }
            }
        }

        // 2. BAGIAN TOPIK POPULER & REKOMENDASI TEMATIK
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Topik Populer & Pilihan",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                POPULAR_TOPICS.forEach { topic ->
                    SuggestionChip(
                        onClick = { onQueryClick(topic.searchKeyword) },
                        label = {
                            Text(
                                text = "${topic.emoji} ${topic.title}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                    )
                }
            }
        }

        // 3. TIPS PENCARIAN CEPAT
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tips Cepat: Ketik nama kitab dan nomor hadis untuk langsung membaca, contoh: Bukhari 1, Muslim 24, atau Abu Daud 100.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
