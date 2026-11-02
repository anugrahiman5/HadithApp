package com.nusa.hadithapp.ui.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nusa.hadithapp.domain.model.HadithSearchItem

@Composable
fun BookFilterChips(
    allBooks: List<String>,
    totalCount: Int,
    resultsPerBook: Map<String, List<HadithSearchItem>>,
    selectedFilterBook: String?,
    onBookSelected: (String?) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedFilterBook == null,
                    onClick = { onBookSelected(null) },
                    label = { Text("Semua Kitab ($totalCount)") }
                )
            }
            items(allBooks) { bookSlug ->
                val count = resultsPerBook[bookSlug]?.size ?: 0
                val formattedBookName = bookSlug.replace(Regex("(?<!^)(?=[A-Z])"), " ")
                FilterChip(
                    selected = selectedFilterBook == bookSlug,
                    onClick = { onBookSelected(bookSlug) },
                    label = { Text("$formattedBookName ($count)") }
                )
            }
        }
    }
}