package com.nusa.hadithapp.ui.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.HadithGradeFilter

@Composable
fun GradeFilterChips(
    selectedGrade: HadithGradeFilter,
    allCount: Int,
    shahihCount: Int,
    hasanCount: Int,
    dhaifCount: Int,
    onGradeSelected: (HadithGradeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(end = 8.dp)
            ) {
                item {
                    val isSelected = selectedGrade == HadithGradeFilter.ALL
                    FilterChip(
                        selected = isSelected,
                        onClick = { onGradeSelected(HadithGradeFilter.ALL) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.FilterList,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = {
                            Text(
                                text = "Semua ($allCount)",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }

                item {
                    val isSelected = selectedGrade == HadithGradeFilter.SHAHIH
                    FilterChip(
                        selected = isSelected,
                        onClick = { onGradeSelected(HadithGradeFilter.SHAHIH) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFF0F6D4A) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF0F6D4A).copy(alpha = 0.15f),
                            selectedLabelColor = Color(0xFF0F6D4A)
                        ),
                        label = {
                            Text(
                                text = "Shahih ($shahihCount)",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }

                item {
                    val isSelected = selectedGrade == HadithGradeFilter.HASAN
                    FilterChip(
                        selected = isSelected,
                        onClick = { onGradeSelected(HadithGradeFilter.HASAN) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFFB45309) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFB45309).copy(alpha = 0.15f),
                            selectedLabelColor = Color(0xFFB45309)
                        ),
                        label = {
                            Text(
                                text = "Hasan ($hasanCount)",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }

                if (dhaifCount > 0) {
                    item {
                        val isSelected = selectedGrade == HadithGradeFilter.DHAIF
                        FilterChip(
                            selected = isSelected,
                            onClick = { onGradeSelected(HadithGradeFilter.DHAIF) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.WarningAmber,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                                selectedLabelColor = MaterialTheme.colorScheme.error
                            ),
                            label = {
                                Text(
                                    text = "Dha'if ($dhaifCount)",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
