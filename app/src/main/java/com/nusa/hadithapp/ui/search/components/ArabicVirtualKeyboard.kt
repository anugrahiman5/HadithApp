package com.nusa.hadithapp.ui.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Keyboard Virtual Arab Lengkap (Hijaiyah + Harakat)
 * Membantu pengguna yang di HP-nya tidak terpasang bahasa Arab agar bisa mengetik teks Arab langsung di aplikasi.
 */
@Composable
fun ArabicVirtualKeyboard(
    visible: Boolean,
    onKeyClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mode Shift untuk berganti antara Huruf Hijaiyah dan Harakat/Karakter Tambahan
    var isShiftActive by remember { mutableStateOf(false) }

    // Baris 1: Angka Arab / Simbol
    val row1 = listOf("١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩", "٠", "؟")

    // Baris 2: Huruf Atas
    val row2Normal = listOf("ض", "ص", "ث", "ق", "ف", "غ", "ع", "ه", "خ", "ح", "ج", "د")
    val row2Shift = listOf("َ", "ً", "ُ", "ٌ", "لإ", "إ", "‘", "÷", "×", "؛", "<", ">")

    // Baris 3: Huruf Tengah
    val row3Normal = listOf("ش", "س", "ي", "ب", "ل", "ا", "ت", "ن", "م", "ك", "ط")
    val row3Shift = listOf("ِ", "ٍ", "]", "[", "لأ", "أ", "ـ", "،", "/", ":", "\"")

    // Baris 4: Huruf Bawah
    val row4Normal = listOf("ئ", "ء", "ؤ", "ر", "لا", "ى", "ة", "و", "ز", "ظ")
    val row4Shift = listOf("ْ", "ّ", "ٰ", "ٱ", "لآ", "آ", "ـ", "،", "!", "؟")

    // Quick Harakat baris atas dengan Dotted Circle (◌) agar bentuk harakat terlihat jelas dan tajam
    val quickHarakat = listOf(
        Pair("\u25CC\u064E", "\u064E"), // Fathah ◌َ
        Pair("\u25CC\u0650", "\u0650"), // Kasrah ◌ِ
        Pair("\u25CC\u064F", "\u064F"), // Dhammah ◌ُ
        Pair("\u25CC\u064B", "\u064B"), // Tanwin Fathah ◌ً
        Pair("\u25CC\u064D", "\u064D"), // Tanwin Kasrah ◌ٍ
        Pair("\u25CC\u064C", "\u064C"), // Tanwin Dhammah ◌ٌ
        Pair("\u25CC\u0651", "\u0651"), // Tasydid ◌ّ
        Pair("\u25CC\u0652", "\u0652"), // Sukun ◌ْ
        Pair("\u25CC\u0670", "\u0670")  // Alif Khanjariyah ◌ٰ
    )

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Header Toolbar Keyboard: Quick Harakat + Tutup
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Hapus Harakat (Gundul)
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f))
                            .clickable { onClearClick() }
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CleaningServices,
                                contentDescription = "Bersihkan Teks",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.height(13.dp)
                            )
                            Text(
                                text = "Bersih",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Shortcut Harakat Cepat
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        quickHarakat.forEach { (preview, harakatChar) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .clickable { onKeyClick(harakatChar) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = preview,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Tombol Tutup Keyboard
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { onCloseClick() }
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardHide,
                            contentDescription = "Sembunyikan Keyboard",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.height(16.dp)
                        )
                    }
                }

                // Baris 1: Angka Arab
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    row1.forEach { key ->
                        ArabicKeyButton(
                            text = key,
                            modifier = Modifier.weight(1f),
                            onClick = { onKeyClick(key) }
                        )
                    }
                }

                // Baris 2: Baris Atas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    val activeRow2 = if (isShiftActive) row2Shift else row2Normal
                    activeRow2.forEach { key ->
                        ArabicKeyButton(
                            text = key,
                            modifier = Modifier.weight(1f),
                            onClick = { onKeyClick(key) }
                        )
                    }
                }

                // Baris 3: Baris Tengah
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    val activeRow3 = if (isShiftActive) row3Shift else row3Normal
                    activeRow3.forEach { key ->
                        ArabicKeyButton(
                            text = key,
                            modifier = Modifier.weight(1f),
                            onClick = { onKeyClick(key) }
                        )
                    }
                }

                // Baris 4: Baris Bawah + Tombol Shift & Backspace
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Shift
                    Box(
                        modifier = Modifier
                            .weight(1.3f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isShiftActive) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                1.dp,
                                if (isShiftActive) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { isShiftActive = !isShiftActive },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SHIFT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isShiftActive) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    val activeRow4 = if (isShiftActive) row4Shift else row4Normal
                    activeRow4.forEach { key ->
                        ArabicKeyButton(
                            text = key,
                            modifier = Modifier.weight(1f),
                            onClick = { onKeyClick(key) }
                        )
                    }

                    // Tombol Backspace / Hapus
                    Box(
                        modifier = Modifier
                            .weight(1.3f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { onDeleteClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Hapus",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.height(18.dp)
                        )
                    }
                }

                // Baris 5: Spasi & Cari
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Huruf Tambahan (ذ)
                    ArabicKeyButton(
                        text = "ذ",
                        modifier = Modifier.weight(1.2f),
                        onClick = { onKeyClick("ذ") }
                    )

                    // Tombol Spasi (Panjang)
                    Box(
                        modifier = Modifier
                            .weight(5f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { onKeyClick(" ") },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SpaceBar,
                                contentDescription = "Spasi",
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.height(16.dp)
                            )
                            Text(
                                text = "SPASI",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    // Tombol Cari / Search
                    Box(
                        modifier = Modifier
                            .weight(2.5f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { onSearchClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Cari",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.height(16.dp)
                            )
                            Text(
                                text = "CARI",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArabicKeyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                RoundedCornerShape(6.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
