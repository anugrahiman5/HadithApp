package com.nusa.hadithapp.ui.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.HadithSearchItem
import com.nusa.hadithapp.util.HadithTextDefaults
import com.nusa.hadithapp.util.formatHadithText

@Composable
fun SearchResultCard(
    item: HadithSearchItem,
    searchKeyword: String,
    onClick: () -> Unit
) {
    val textColors = HadithTextDefaults.colors()
    val isArabicQuery = searchKeyword.any { it in '\u0600'..'\u06FF' }

    val formattedArabic = remember(item.arabicText, searchKeyword, isArabicQuery, textColors) {
        val keywords = searchKeyword.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
        if (isArabicQuery && keywords.isNotEmpty()) {
            formatHadithText(
                text = item.arabicText,
                keywords = keywords,
                isSearchMode = true,
                colors = textColors
            )
        } else {
            androidx.compose.ui.text.AnnotatedString(item.arabicText)
        }
    }

    val formattedTranslation = remember(item.translation, searchKeyword, isArabicQuery, textColors) {
        val keywords = searchKeyword.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
        formatHadithText(
            text = item.translation,
            keywords = keywords,
            isSearchMode = !isArabicQuery && keywords.isNotEmpty(),
            colors = textColors
        )
    }


    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${item.bookName} No. ${item.number}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = item.chapterName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            if (item.grade.isNotBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = item.grade,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = formattedArabic,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = formattedTranslation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (item.narrator.isNotBlank()) {
                Text(
                    text = "👤 ${item.narrator} (Sahabat)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}