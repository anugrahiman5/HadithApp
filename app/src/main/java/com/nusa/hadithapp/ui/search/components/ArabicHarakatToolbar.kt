package com.nusa.hadithapp.ui.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Data class merepresentasikan tombol karakter atau harakat Arab
 */
data class ArabicKeyItem(
    val char: String,
    val label: String,
    val displayPreview: String = char
)

/**
 * Toolbar pembantu input Arab & Harakat untuk mempermudah pencarian teks hadits berharakat atau tanpa harakat
 */
@Composable
fun ArabicHarakatToolbar(
    visible: Boolean,
    onInsertChar: (String) -> Unit,
    onStripHarakat: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Daftar harakat dan karakter khusus bahasa Arab
    val harakatList = listOf(
        ArabicKeyItem("\u064E", "Fathah", "ـَ"),
        ArabicKeyItem("\u0650", "Kasrah", "ـِ"),
        ArabicKeyItem("\u064F", "Dhammah", "ـُ"),
        ArabicKeyItem("\u064B", "Fathatain", "ـً"),
        ArabicKeyItem("\u064D", "Kasratain", "ـٍ"),
        ArabicKeyItem("\u064C", "Dammatain", "ـٌ"),
        ArabicKeyItem("\u0651", "Tasydid", "ـّ"),
        ArabicKeyItem("\u0652", "Sukun", "ـْ"),
        ArabicKeyItem("\u0670", "Alif Khanjariyah", "ـٰ"),
        ArabicKeyItem("\u0621", "Hamzah", "ء"),
        ArabicKeyItem("\u0623", "Alif Hamzah Atas", "أ"),
        ArabicKeyItem("\u0625", "Alif Hamzah Bawah", "إ"),
        ArabicKeyItem("\u0622", "Alif Maddah", "آ"),
        ArabicKeyItem("\u0671", "Alif Waslah", "ٱ"),
        ArabicKeyItem("\u0629", "Ta Marbuthah", "ة"),
        ArabicKeyItem("\u0649", "Alif Maqshurah", "ى")
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tombol Hapus Harakat (Strip Diacritics)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onStripHarakat() }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CleaningServices,
                        contentDescription = "Hapus Harakat",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.height(14.dp)
                    )
                    Text(
                        text = "Gundul",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )

                // Daftar Tombol Harakat Scrollable
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    harakatList.forEach { keyItem ->
                        Box(
                            modifier = Modifier
                                .widthIn(min = 36.dp)
                                .height(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onInsertChar(keyItem.char) }
                                .padding(horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = keyItem.displayPreview,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = FontFamily.Default
                            )
                        }
                    }
                }
            }
        }
    }
}
